package me.sirimperivm.spigot.utils.others;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.utils.colors.Colors;

@SuppressWarnings("all")
public class Logger {

    private Main plugin;
    private Colors colors;

    private String pluginPrefix, debugPrefix;

    public Logger(Main plugin, String pluginPrefix, String debugPrefix) {
        this.debugPrefix = debugPrefix;
        this.plugin = plugin;
        this.pluginPrefix = pluginPrefix;

        colors = plugin.getColors();
    }

    public void success(String message) {
        plugin.getServer().getConsoleSender().sendMessage(colors.translateString("&2[" + pluginPrefix + "] " + message));
    }

    public void info(String message) {
        plugin.getServer().getConsoleSender().sendMessage(colors.translateString("&e[" + pluginPrefix + "] " + message));
    }

    public void debug(String message) {
        plugin.getServer().getConsoleSender().sendMessage(colors.translateString("&6[" + debugPrefix + "] " + message));
    }

    public void fail(String message) {
        plugin.getServer().getConsoleSender().sendMessage(colors.translateString("&c[" + pluginPrefix + "] " + message));
    }
}
