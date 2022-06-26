package me.maximus1027.NameColors.Players;

import me.maximus1027.NameColors.Database.PlayerData;
import me.maximus1027.NameColors.Utils.Misc;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.UUID;

public class PlayerColors implements Listener {
    private static final HashMap<UUID, String> PlayerNames = new HashMap<>();
    private static final HashMap<UUID, String> PlayerPatterns = new HashMap<>();


    /*
      Set player's username via pattern
     */
    public static void setPlayerPattern(UUID uuid, String pattern) {
        PlayerPatterns.put(uuid, pattern);
    }

    /*
    Set player's username
 */
    public static void setPlayerUserame(UUID uuid, String pattern) {
        PlayerNames.put(uuid, Misc.conv(parsePattern(Bukkit.getPlayer(uuid).getName(), pattern)));
    }

    public static void removePlayer(UUID uuid) {
        PlayerNames.remove(uuid);
        PlayerPatterns.remove(uuid);
    }

    public static String getPlayerUsername(UUID uuid) {
        return PlayerNames.get(uuid);
    }

    public static String getPlayerPattern(UUID uuid) {
        return PlayerPatterns.get(uuid);
    }

    public static Boolean isStyleSet(UUID uuid) {
        return PlayerNames.containsKey(uuid);
    }

    public static Boolean isPatternSet(UUID uuid) {
        return PlayerPatterns.containsKey(uuid);
    }


    private static String parsePattern(String name, String pattern) {
        String[] patternArr = pattern.split(" ");
        String coloredName = "";
        for (int i = 0; i < name.length(); i++) {

            for (String p : patternArr[i % patternArr.length].split("")) {
                coloredName += "&" + p;
            }
            coloredName += name.charAt(i) + "";
        }
        return coloredName + "&r";
    }

    private final PlayerData playerData;

    public PlayerColors(DataSource source, Plugin plugin) {
        this.playerData = new PlayerData(plugin, source);
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        String pattern = playerData.getPlayerPattern(e.getPlayer());

        System.out.println(e.getPlayer().getName() + " PATTERN: " + pattern);

        if (pattern == null || pattern == "") {
            return;
        }

        setPlayerPattern(e.getPlayer().getUniqueId(), pattern);
        setPlayerUserame(e.getPlayer().getUniqueId(), pattern);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        assert getPlayerPattern(e.getPlayer().getUniqueId()) != null;

        playerData.savePlayerPattern(e.getPlayer());

        removePlayer(e.getPlayer().getUniqueId());
    }
}
