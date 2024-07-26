package me.sirimperivm.spigot.utils.tables;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.utils.ConfigManager;
import me.sirimperivm.spigot.utils.DataManager;
import me.sirimperivm.spigot.utils.ModuleManager;
import me.sirimperivm.spigot.utils.colors.Colors;
import me.sirimperivm.spigot.utils.others.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("all")
public class Tasks {

    private DataManager data;
    private Main plugin;
    private Colors colors;
    private Logger log;
    private ConfigManager configManager;
    private ModuleManager moduleManager;

    private Connection conn;
    private String table;

    private int newTaskId;
    private int lastTaskId;

    public Tasks(DataManager data) {
        this.data = data;
        plugin = data.getPlugin();
        colors = plugin.getColors();
        log = plugin.getLog();
        configManager = plugin.getConfigManager();
        moduleManager = plugin.getModules();

        conn = data.getConn();
        table = "tasks";

        createTable();
        lastTaskId = getLastTaskId();
        newTaskId = lastTaskId;
        if (newTaskId < 0) {
            newTaskId+=2;
        }
        else {
            newTaskId++;
        }
    }

    private void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS " + table + "(\n" +
                "\tTaskID INTEGER,\n" +
                "\tWorldName VARCHAR(120) NOT NULL,\n" +
                "\tAreaSize INTEGER NOT NULL,\n" +
                "\tPlayerStarter VARCHAR(50) NOT NULL" +
                ");";
        try {
            PreparedStatement state = conn.prepareStatement(query);
            state.execute();
        } catch (SQLException e) {
            log.fail("Non è stato possibile creare la tabella " + table + "!");
            e.printStackTrace();
        }
    }

    public void insertTask(String worldName, String playerName, int area) {
        String query = "INSERT INTO " + table + "(TaskID, WorldName, AreaSize, PlayerStarter) VALUES (?, ?, ?, ?)";


        try {
            PreparedStatement state = conn.prepareStatement(query);
            state.setInt(1, newTaskId);
            state.setString(2, worldName);
            state.setInt(3, area);
            state.setString(4, playerName);
            state.executeUpdate();
        } catch (SQLException e) {
            log.fail("Impossibile inserire la task: " + worldName + " - " + playerName + " - " + area + "!");
            e.printStackTrace();
        }
    }

    public int getLastTaskId() {
        int taskId = -1;
        String query = "SELECT TaskID AS Task FROM " + table + " ORDER BY (TaskID) ASC LIMIT 1";

        try {
            PreparedStatement state = conn.prepareStatement(query);
            ResultSet rs = state.executeQuery();
            while (rs.next()) {
                taskId = rs.getInt("Task");
                break;
            }
        } catch (SQLException e) {
            log.fail("Impossibile ottenere l'ultima task generata.");
            e.printStackTrace();
        }
        return taskId;
    }

    public String getWorldName(int taskId) {
        String worldName = null;
        String query = "SELECT WorldName FROM " + table + " WHERE TaskID=?";

        try {
            PreparedStatement state = conn.prepareStatement(query);
            state.setInt(1, taskId);
            ResultSet rs = state.executeQuery();
            while (rs.next()) {
                worldName = rs.getString("WorldName");
                break;
            }
        } catch (SQLException e) {
            log.fail("Impossibile ottenere il nome del mondo della task " + taskId + "!");
            e.printStackTrace();
        }
        return worldName;
    }

    public int getAreaSize(int taskId) {
        int areaSize = -1;
        String query = "SELECT AreaSize FROM " + table + " WHERE TaskID=?";

        try {
            PreparedStatement state = conn.prepareStatement(query);
            state.setInt(1, taskId);
            ResultSet rs = state.executeQuery();
            while (rs.next()) {
                areaSize = rs.getInt("AreaSize");
                break;
            }
        } catch (SQLException e) {
            log.fail("Impossibile ottenere l'area della task " + taskId + "!");
            e.printStackTrace();
        }
        return areaSize;
    }

    public String getPlayerStarter(int taskId) {
        String playerStarter = null;
        String query = "SELECT PlayerStarter FROM " + table + " WHERE TaskID=?";

        try {
            PreparedStatement state = conn.prepareStatement(query);
            state.setInt(1, taskId);
            ResultSet rs = state.executeQuery();
            while (rs.next()) {
                playerStarter = rs.getString("PlayerStarter");
                break;
            }
        } catch (SQLException e) {
            log.fail("Impossibile ottenere il nome del player che ha startato la task " + taskId + "!");
            e.printStackTrace();
        }
        return playerStarter;
    }

    public void deleteTask(int taskId) {
        String query = "DELETE FROM " + table + " WHERE TaskID=?";

        try {
            PreparedStatement state = conn.prepareStatement(query);
            state.setInt(1, taskId);
            state.executeUpdate();
        } catch (SQLException e) {
            log.fail("Impossibile rimuovere la task " + taskId + "!");
            e.printStackTrace();
        }
    }
}
