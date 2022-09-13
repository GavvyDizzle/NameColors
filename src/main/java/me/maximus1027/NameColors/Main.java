package me.maximus1027.NameColors;

import me.maximus1027.NameColors.colors.ColorManager;
import me.maximus1027.NameColors.commands.AdminCommandManager;
import me.maximus1027.NameColors.commands.PlayerCommandManager;
import me.maximus1027.NameColors.database.Configuration;
import me.maximus1027.NameColors.database.DataSourceProvider;
import me.maximus1027.NameColors.database.DbSetup;
import me.maximus1027.NameColors.gui.GUIManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;

public class Main extends JavaPlugin {
    private static Main instance;

    private ColorManager colorManager;
    private GUIManager guiManager;
    private AdminCommandManager adminCommandManager;

    private DataSource dataSource;
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

        instance = this;

        colorManager = new ColorManager(dataSource, this);
        getServer().getPluginManager().registerEvents(colorManager, this);

        guiManager = new GUIManager();
        getServer().getPluginManager().registerEvents(guiManager, this);

        adminCommandManager = new AdminCommandManager();
        Objects.requireNonNull(getCommand("namecoloradmin")).setExecutor(adminCommandManager);

        Objects.requireNonNull(getCommand("namecolor")).setExecutor(new PlayerCommandManager());


        new Configuration(this);
    }

    public static Main getInstance() {
        return instance;
    }

    public ColorManager getColorManager() {
        return colorManager;
    }

    public GUIManager getGuiManager() {
        return guiManager;
    }

    public AdminCommandManager getAdminCommandManager() {
        return adminCommandManager;
    }
}
