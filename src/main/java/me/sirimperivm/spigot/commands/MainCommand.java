package me.sirimperivm.spigot.commands;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.utils.ConfigManager;
import me.sirimperivm.spigot.utils.DataManager;
import me.sirimperivm.spigot.utils.ModuleManager;
import me.sirimperivm.spigot.utils.colors.Colors;
import me.sirimperivm.spigot.utils.others.Errors;
import me.sirimperivm.spigot.utils.others.Logger;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class MainCommand implements CommandExecutor, TabCompleter {

    private Main plugin;
    private Colors colors;
    private Logger log;
    private ConfigManager configManager;
    private Errors errors;
    private DataManager data;
    private ModuleManager modules;

    public MainCommand(Main plugin) {
        this.plugin = plugin;
        colors = plugin.getColors();
        log = plugin.getLog();
        configManager = plugin.getConfigManager();
        errors = plugin.getErrors();
        data = plugin.getData();
        modules = plugin.getModules();
    }

    private void getUsage(CommandSender s, int page) {
        modules.createHelp(s, "main-command", page);
    }

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (errors.noPermCommand(s, configManager.getSettings().getString("permissions.commands.main-command.main"))) {
            return false;
        } else {
            if (a.length == 0) {
                getUsage(s, 1);
            } else if (a.length == 1) {
                if (a[0].equalsIgnoreCase("reload")) {
                    if (errors.noPermCommand(s, configManager.getSettings().getString("permissions.commands.main-command.reload"))) {
                        return false;
                    } else {
                        configManager.loadAll();
                        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "plugin-reloaded"));
                    }
                } else {
                    getUsage(s, 1);
                }
            } else if (a.length == 2) {
                if (a[0].equalsIgnoreCase("help")) {
                    String pageTarget = a[1];
                    if (!modules.containsOnlyNumbers(pageTarget)) {
                        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "invalid-args.number-required").replace("{arg}", pageTarget));
                        return false;
                    }
                    int page = Integer.parseInt(pageTarget);
                    getUsage(s, page);
                } else {
                    getUsage(s, 1);
                }
            } else if (a.length == 3) {
                if (a[0].equalsIgnoreCase("new")) {
                    if (errors.noPermCommand(s, configManager.getSettings().getString("permissions.commands.main-command.new"))) {
                        return false;
                    } else {
                        if (errors.noConsole(s)) {
                            return false;
                        } else {
                            Player p = (Player) s;
                            String worldName = a[1];
                            if (Bukkit.getWorld(worldName) == null) {
                                p.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "world-not-exists").replace("{world}", worldName));
                                return false;
                            }
                            World world = Bukkit.getWorld(worldName);

                            String areaTarget = a[2];
                            if (!modules.containsOnlyNumbers(areaTarget)) {
                                s.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "invalid-args.number-required").replace("{arg}", areaTarget));
                                return false;
                            }
                            int area = Integer.parseInt(areaTarget)*2;

                            world.getWorldBorder().setCenter(0, 0);
                            world.getWorldBorder().setSize(area);
                            data.getTasks().insertTask(worldName, p.getName(), area);

                            p.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "task-added"));
                        }
                    }
                } else {
                    getUsage(s, 1);
                }
            } else {
                getUsage(s, 1);
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) {

        if (s.hasPermission(configManager.getSettings().getString("permissions.commands.main-command.main"))) {
            if (a.length == 1) {
                List<String> toReturn = new ArrayList<>();
                toReturn.add("help");
                if (s.hasPermission(configManager.getSettings().getString("permissions.commands.main-command.reload"))) {
                    toReturn.add("reload");
                }
                if (s.hasPermission(configManager.getSettings().getString("permissions.commands.main-command.new"))) {
                    toReturn.add("new");
                }
                return toReturn;
            } else if (a.length == 2) {
                List<String> toReturn = new ArrayList<>();
                if (a[0].equalsIgnoreCase("help")) {
                    for (int i=0; i<999; i++) {
                        toReturn.add(String.valueOf(i));
                    }
                }
                if (a[0].equalsIgnoreCase("new")) {
                    if (s.hasPermission(configManager.getSettings().getString("permissions.commands.main-command.new"))) {
                        for (World world : Bukkit.getWorlds()) {
                            toReturn.add(world.getName());
                        }
                    }
                }
                return toReturn;
            } else if (a.length == 3) {
                List<String> toReturn = new ArrayList<>();
                if (a[0].equalsIgnoreCase("new")) {
                    if (s.hasPermission(configManager.getSettings().getString("permissions.commands.main-command.new"))) {
                        for (int i=0; i<999; i++) {
                            toReturn.add(String.valueOf(i));
                        }
                    }
                }
                return toReturn;
            }
        }
        return new ArrayList<>();
    }
}
