package me.sirimperivm.spigot.utils;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.entities.State;
import me.sirimperivm.spigot.utils.colors.Colors;
import me.sirimperivm.spigot.utils.others.Errors;
import me.sirimperivm.spigot.utils.others.Logger;
import me.sirimperivm.spigot.utils.tables.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("all")
public class LoaderManager {

    private Main plugin;
    private Colors colors;
    private Logger log;
    private ConfigManager configManager;
    private Errors errors;
    private DataManager data;

    private boolean debug;

    private Tasks tasks;
    private int bukkitTask;

    private State state;
    private boolean canRestart;

    public LoaderManager(Main plugin) {
        this.plugin = plugin;
        this.state = State.READY;

        colors = plugin.getColors();
        log = plugin.getLog();
        configManager = plugin.getConfigManager();
        errors = plugin.getErrors();
        data = plugin.getData();

        tasks = data.getTasks();
        debug = configManager.getSettings().getBoolean("settings.general.debug", true);
        bukkitTask = -1;
        canRestart = false;

        checkPossibleTasks();
    }

    public void checkPossibleTasks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                int lastTaskId = tasks.getLastTaskId();
                if (lastTaskId == -1) {
                    cancel();
                    if (canRestart) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
                    }
                    return;
                }
                setState(State.LOADING);
                loadWorld(lastTaskId);
            }
        }.runTaskLater(plugin, 10L);
    }

    public void loadWorld(int taskId) {
        if (bukkitTask != -1) Bukkit.getScheduler().cancelTask(bukkitTask);

        String worldName = tasks.getWorldName(taskId);
        int areaSize = tasks.getAreaSize(taskId);
        String playerStarter = tasks.getPlayerStarter(taskId);

        if (debug) log.debug("Caricando i chunk della task: " + taskId + ", per il mondo: " + worldName + ", proprietario: " + playerStarter + ", area: " + areaSize + "!");
        long tickSpeed = configManager.getSettings().getLong("settings.chunk-loading.tick-speed");
        int chunkMinX = (-areaSize) >> 4;
        int chunkMaxX = (areaSize) >> 4;
        int chunkMinZ = (-areaSize) >> 4;
        int chunkMaxZ = (areaSize) >> 4;

        World world = Bukkit.getWorld(worldName);

        bukkitTask = new BukkitRunnable() {
            int x = chunkMinX;
            int z = chunkMinZ;

            @Override
            public void run() {
                if (x > chunkMaxX) {
                    x = chunkMinX;
                    z++;
                }

                if (worldName == null || world == null) {
                    log.fail("Il mondo registrato nella task non esiste.");
                    cancel();
                    tasks.deleteTask(taskId);
                    int newTask = tasks.getLastTaskId();
                    if (newTask == -1) {
                        canRestart = true;
                    } else {
                        canRestart = false;
                    }
                    checkPossibleTasks();
                    return;
                }

                if (z > chunkMaxZ) {
                    if (debug) log.debug("Caricamento chunk per la task: " + taskId + " eseguita con successo.");
                    cancel();
                    tasks.deleteTask(taskId);
                    int newTask = tasks.getLastTaskId();
                    if (newTask == -1) {
                        canRestart = true;
                    } else {
                        canRestart = false;
                    }
                    checkPossibleTasks();
                    return;
                }

                if (debug) log.debug("Caricamento chunk... X: " + x + ", Z: " + z + "!");
                Chunk chunk = world.getChunkAt(x, z);
                if (!chunk.isLoaded()) {
                    chunk.load();
                }

                x++;
            }
        }.runTaskTimer(plugin, 0L, tickSpeed).getTaskId();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
