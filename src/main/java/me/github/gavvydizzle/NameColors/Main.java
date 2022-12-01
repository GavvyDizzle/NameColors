package me.github.gavvydizzle.NameColors;

import me.github.gavvydizzle.NameColors.gui.GUIManager;
import me.github.gavvydizzle.NameColors.colors.ColorManager;
import me.github.gavvydizzle.NameColors.commands.AdminCommandManager;
import me.github.gavvydizzle.NameColors.commands.PlayerCommandManager;
import me.github.gavvydizzle.NameColors.database.Configuration;
import me.github.gavvydizzle.NameColors.database.DataSourceProvider;
import me.github.gavvydizzle.NameColors.database.DbSetup;
import me.github.gavvydizzle.NameColors.papi.MyExpansion;
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

        try {
            new MyExpansion().register();
        }
        catch (Exception e) {
            getLogger().severe("[NameColors] This plugin requires PlaceholderAPI!");
            getServer().getPluginManager().disablePlugin(this);
        }
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
