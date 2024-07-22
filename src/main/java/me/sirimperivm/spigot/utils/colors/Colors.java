package me.sirimperivm.spigot.utils.colors;

import cz.foresttech.api.ColorAPI;
import me.sirimperivm.spigot.Main;

@SuppressWarnings("all")
public class Colors {

    private Main plugin;

    public Colors(Main plugin) {
        this.plugin = plugin;
    }

    public String translateString(String target) {
        return ColorAPI.colorize(target);
    }
}
