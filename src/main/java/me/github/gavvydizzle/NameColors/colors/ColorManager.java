package me.github.gavvydizzle.NameColors.colors;

import com.github.mittenmc.serverutils.Colors;
import me.github.gavvydizzle.NameColors.Main;
import me.github.gavvydizzle.NameColors.pattern.Pattern;
import me.github.gavvydizzle.NameColors.database.PlayerData;
import me.github.gavvydizzle.NameColors.pattern.SolidPattern;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ColorManager implements Listener {

    private final ConcurrentHashMap<UUID, String> coloredPlayerNames;
    private final ConcurrentHashMap<UUID, String> playerNameColorIDs;
    private final HashMap<String, NameColor> nameColorHashMap;
    private final ArrayList<NameColor> nameColorArrayList;
    private Pattern defaultPattern;
    private final PlayerData playerData;

    private String selectionMessage, permissionDeniedMessage;

    public ColorManager(DataSource source, Plugin plugin) {
        this.playerData = new PlayerData(plugin, source);
        coloredPlayerNames = new ConcurrentHashMap<>();
        playerNameColorIDs = new ConcurrentHashMap<>();
        nameColorHashMap = new HashMap<>();
        nameColorArrayList = new ArrayList<>();
        reload();
    }

    public void reload() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            FileConfiguration config = Main.getInstance().getConfig();
            config.options().copyDefaults(true);
            config.addDefault("settings.default", "gray");
            config.addDefault("messages.on-enable", "&aEnabled: &e%namecolor%");
            config.addDefault("messages.no-access", "&c&l(!) You do not have permission to equip this.");
            config.addDefault("namecolors", new HashMap<>());
            loadPatterns(config);
            Main.getInstance().saveConfig();

            Main.getInstance().getGuiManager().reloadAllGUIs();

            if (nameColorHashMap.containsKey(config.getString("settings.default"))) {
                defaultPattern = nameColorHashMap.get(config.getString("settings.default")).getPattern();
            }
            else {
                defaultPattern = new SolidPattern("7");
            }

            if (defaultPattern == null) {
                defaultPattern = new SolidPattern("7");
            }

            selectionMessage = Colors.conv(config.getString("messages.on-enable"));
            permissionDeniedMessage = Colors.conv(config.getString("messages.no-access"));

            recolorNames();
        });
    }

    private void loadPatterns(FileConfiguration config) {
        nameColorHashMap.clear();
        nameColorArrayList.clear();

        if (config.getConfigurationSection("namecolors") == null) {
            Main.getInstance().getLogger().warning("No name colors are defined! Default will be set to gray (&7)");
            return;
        }

        for (String key : Objects.requireNonNull(config.getConfigurationSection("namecolors")).getKeys(false)) {
            String path = "namecolors." + key;

            config.addDefault(path + ".display-name", "name");
            config.addDefault(path + ".lore", new ArrayList<>());
            config.addDefault(path + ".item", "LIME_DYE");
            config.addDefault(path + ".item-locked", "GRAY_DYE");
            config.addDefault(path + ".pattern", "7");

            if (key.length() > 16) {
                Main.getInstance().getLogger().warning("The key '" + key + "' is longer than 16 characters. You must keep the id 16 characters or less. This name color is being ignored!");
                continue;
            }

            if (nameColorHashMap.containsKey(key)) {
                Main.getInstance().getLogger().warning("The key '" + key + "' is already in use. Any duplicates are being ignored!");
                continue;
            }

            NameColor nameColor;
            try {
                nameColor = new NameColor(
                        key,
                        config.getString(path + ".display-name"),
                        config.getStringList(path + ".lore"),
                        config.getString(path + ".item-locked"),
                        config.getString(path + ".item"),
                        Objects.requireNonNull(config.getString(path + ".pattern")),
                        config.getBoolean(path + ".hidden")
                );
            } catch (Exception e) {
                Main.getInstance().getLogger().severe("An error occurred when loading '" + key + "'. It is being ignored!");
                e.printStackTrace();
                continue;
            }

            nameColorHashMap.put(key, nameColor);
            nameColorArrayList.add(nameColor);
        }
    }

    /**
     * Updates the color of a name if existed before AND after the reload.
     * This will not update all names because it would potentially remove the selection if an error occurred when reloading.
     */
    private void recolorNames() {
        for (Map.Entry<UUID, String> entry : coloredPlayerNames.entrySet()) {
            if (playerNameColorIDs.containsKey(entry.getKey())) {
                String id = playerNameColorIDs.get(entry.getKey());
                Player player = Bukkit.getPlayer(entry.getKey());

                if (player != null && nameColorHashMap.containsKey(id)) {
                    coloredPlayerNames.put(entry.getKey(), nameColorHashMap.get(id).getPattern().withPattern(player.getName()));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerChat(AsyncPlayerChatEvent e) {
        e.setFormat(e.getFormat().replace("%1$s", getPlayerNameFormatted(e.getPlayer())));
    }

    public String getPlayerNameFormatted(Player player) {
        if (coloredPlayerNames.containsKey(player.getUniqueId())) {
            return coloredPlayerNames.get(player.getUniqueId());
        }

        coloredPlayerNames.put(player.getUniqueId(), defaultPattern.withPattern(player.getName()));
        return defaultPattern.withPattern(player.getName());
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            String pat = playerData.getPlayerPatternID(e.getPlayer());

            if (pat == null) {
                coloredPlayerNames.put(e.getPlayer().getUniqueId(), defaultPattern.withPattern(e.getPlayer().getName()));
            }
            else {
                if (nameColorHashMap.containsKey(pat)) {
                    coloredPlayerNames.put(e.getPlayer().getUniqueId(), nameColorHashMap.get(pat).getPattern().withPattern(e.getPlayer().getName()));
                }
                else {
                    coloredPlayerNames.put(e.getPlayer().getUniqueId(), defaultPattern.withPattern(e.getPlayer().getName()));
                    Main.getInstance().getLogger().warning(e.getPlayer().getName() + " had the saved pattern '" + pat + "'. This pattern no longer exists so they were given the default pattern.");
                }
            }
        });
    }

    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent e) {
        if (!coloredPlayerNames.containsKey(e.getPlayer().getUniqueId())) return;
        coloredPlayerNames.remove(e.getPlayer().getUniqueId());

        if (playerNameColorIDs.containsKey(e.getPlayer().getUniqueId())) {
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () ->
                    playerData.savePlayerPattern(e.getPlayer(), playerNameColorIDs.remove(e.getPlayer().getUniqueId())));
        }
    }

    /**
     * Handles when a player changes their name color. This method also does a permission check to see if the player can select it
     * @param player The player
     * @param index The number name color clicked, given by page and slot
     */
    public void onPatternSwap(Player player, int index) {
        NameColor nameColor;
        try {
            nameColor = nameColorArrayList.get(index);
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "Failed to find this name color. Please alert an admin!");
            e.printStackTrace();
            return;
        }

        if (!player.hasPermission(nameColor.getPermission())) {
            player.sendMessage(permissionDeniedMessage);
            return;
        }

        setNameColor(player, nameColor);
        player.sendMessage(selectionMessage.replace("%namecolor%", nameColor.getId()));
    }

    public void setNameColor(Player player, NameColor nameColor) {
        if (nameColor == null) return;

        coloredPlayerNames.put(player.getUniqueId(), nameColor.getPattern().withPattern(player.getName()));
        playerNameColorIDs.put(player.getUniqueId(), nameColor.getId());
    }


    public NameColor getNameColorByID(String id) {
        return nameColorHashMap.get(id.toLowerCase());
    }

    public ArrayList<NameColor> getNameColorArrayList() {
        return nameColorArrayList;
    }

    public Set<String> getNameColorIDs() {
        return nameColorHashMap.keySet();
    }
}
