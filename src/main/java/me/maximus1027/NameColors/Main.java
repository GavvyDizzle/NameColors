package me.maximus1027.NameColors;

import me.maximus1027.NameColors.Database.Configuration;
import me.maximus1027.NameColors.Database.DataSourceProvider;
import me.maximus1027.NameColors.Database.DbSetup;
import me.maximus1027.NameColors.Database.PlayerData;
import me.maximus1027.NameColors.Gui.NameColorsUI;
import me.maximus1027.NameColors.Gui.OpenGuiCommand;
import me.maximus1027.NameColors.Players.PlayerColors;
import me.maximus1027.NameColors.chatmanager.ChatLoading;
import org.bukkit.plugin.java.JavaPlugin;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

public class Main extends JavaPlugin {
    private static Main main;

    private DataSource dataSource;
    private PlayerData data;
    private boolean mySQLSuccessful;

    @Override
    public void onLoad() {
        Configuration configuration = new Configuration(this);
        mySQLSuccessful = true;

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        try {
            dataSource = DataSourceProvider.initMySQLDataSource(this, configuration.getDatabase());
        } catch (SQLException e) {
            getLogger().log(Level.SEVERE, "Could not establish database connection", e);
            mySQLSuccessful = false;
        }

        try {
            DbSetup.initDb(this, dataSource);
        } catch (SQLException | IOException e) {
            getLogger().log(Level.SEVERE, "Could not init database.", e);
            mySQLSuccessful = false;
        }
    }

    @Override
    public void onEnable() {

        if (!mySQLSuccessful) {
            getLogger().log(Level.SEVERE, "Database connection failed. Disabling plugin");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }



        getServer().getPluginManager().registerEvents(new NameColorsUI(null), this);
        getServer().getPluginManager().registerEvents(new ChatLoading(), this);
        getServer().getPluginManager().registerEvents(new PlayerColors(dataSource, this), this);
        getCommand("namecolors").setExecutor(new OpenGuiCommand());



        main = this;

        NameColorsUI.Load();
        ChatLoading.load();
        new Configuration(this);
    }

    @Override
    public void onDisable() {

    }

    public static Main getInstance() {
        return main;
    }
}
