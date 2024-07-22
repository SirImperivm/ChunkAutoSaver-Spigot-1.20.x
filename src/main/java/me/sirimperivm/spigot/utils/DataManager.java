package me.sirimperivm.spigot.utils;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.utils.others.Logger;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SuppressWarnings("all")
public class DataManager {

    private Main plugin;
    private Logger log;

    private boolean connected;

    public DataManager(Main plugin) {
        this.plugin = plugin;
        log = plugin.getLog();

        setup();
    }

    private Connection conn;

    private boolean createConnection() {
        try {
            File folder = plugin.getDataFolder();
            if (!folder.exists()) folder.mkdir();
            File dbFile = new File(folder, "data.db");

            conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
            connected = true;
            return true;
        } catch (SQLException e) {
            log.fail("Non è stato possibile connettersi al database, il plugin si disattiverà.");
            e.printStackTrace();
            connected = false;
            return false;
        }
    }

    private void setup() {
        if (!createConnection()) {
            plugin.disablePlugin();
        }
    }

    public void closeConnection() {
        if (connected) {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                log.fail("Non è stato possibile disconnettersi dal database.");
                e.printStackTrace();
            }
        }
    }
}
