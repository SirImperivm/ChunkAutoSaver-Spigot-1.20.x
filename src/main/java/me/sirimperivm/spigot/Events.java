package me.sirimperivm.spigot;

import me.sirimperivm.spigot.entities.State;
import me.sirimperivm.spigot.utils.ConfigManager;
import me.sirimperivm.spigot.utils.DataManager;
import me.sirimperivm.spigot.utils.LoaderManager;
import me.sirimperivm.spigot.utils.ModuleManager;
import me.sirimperivm.spigot.utils.colors.Colors;
import me.sirimperivm.spigot.utils.others.Errors;
import me.sirimperivm.spigot.utils.others.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

@SuppressWarnings("all")
public class Events implements Listener {

    private Main plugin;
    private Colors colors;
    private Logger log;
    private ConfigManager configManager;
    private Errors errors;
    private DataManager data;
    private LoaderManager loaderManager;
    private ModuleManager modules;

    public Events(Main plugin) {
        this.plugin = plugin;
        colors = plugin.getColors();
        log = plugin.getLog();
        configManager = plugin.getConfigManager();
        errors = plugin.getErrors();
        data = plugin.getData();
        loaderManager = plugin.getLoaderManager();
        modules = plugin.getModules();
    }

    @EventHandler
    public void onConnect(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        State state = loaderManager.getState();

        if (state != State.READY) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, configManager.getTranslatedString(configManager.getMessages(), "cant-join"));
        }
    }
}
