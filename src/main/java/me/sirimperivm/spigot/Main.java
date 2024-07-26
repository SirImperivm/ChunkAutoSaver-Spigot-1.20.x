package me.sirimperivm.spigot;

import me.sirimperivm.spigot.commands.MainCommand;
import me.sirimperivm.spigot.utils.ConfigManager;
import me.sirimperivm.spigot.utils.DataManager;
import me.sirimperivm.spigot.utils.LoaderManager;
import me.sirimperivm.spigot.utils.ModuleManager;
import me.sirimperivm.spigot.utils.colors.Colors;
import me.sirimperivm.spigot.utils.others.Errors;
import me.sirimperivm.spigot.utils.others.Logger;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getPluginManager;

@SuppressWarnings("all")
public final class Main extends JavaPlugin {

    private Main plugin;
    private Colors colors;
    private Logger log;
    private ConfigManager configManager;
    private Errors errors;
    private DataManager data;
    private LoaderManager loaderManager;
    private ModuleManager modules;

    @Override
    public void onEnable() {
        plugin = this;
        colors = new Colors(plugin);
        log = new Logger(plugin, "ChunkAutoSaver", "CAS-Debug");
        configManager = new ConfigManager(plugin);
        errors = new Errors(plugin);
        data = new DataManager(plugin);
        loaderManager = new LoaderManager(plugin);
        modules = new ModuleManager(plugin);

        getCommand("chunkautosaver").setExecutor(new MainCommand(plugin));
        getCommand("chunkautosaver").setTabCompleter(new MainCommand(plugin));
        getPluginManager().registerEvents(new Events(plugin), plugin);

        log.success("Plugin attivato correttamente.");
    }

    @Override
    public void onDisable() {
        data.closeConnection();
        log.success("Plugin disattivato correttamente.");
    }

    public void disablePlugin() {
        getPluginManager().disablePlugin(this);
    }

    public Main getPlugin() {
        return plugin;
    }

    public Colors getColors() {
        return colors;
    }

    public Logger getLog() {
        return log;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public Errors getErrors() {
        return errors;
    }

    public DataManager getData() {
        return data;
    }

    public LoaderManager getLoaderManager() {
        return loaderManager;
    }

    public ModuleManager getModules() {
        return modules;
    }
}
