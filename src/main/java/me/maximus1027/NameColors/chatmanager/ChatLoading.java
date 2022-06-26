package me.maximus1027.NameColors.chatmanager;

import me.maximus1027.NameColors.Main;
import me.maximus1027.NameColors.Players.PlayerColors;
import me.maximus1027.NameColors.Utils.Misc;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatLoading implements Listener {
    private static String colorCode;

    public static void load() {
        FileConfiguration config = Main.getInstance().getConfig();

        colorCode = config.getString("settings.default");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnChat(AsyncPlayerChatEvent e) {
        String format = e.getFormat();

        if (PlayerColors.isStyleSet(e.getPlayer().getUniqueId())) {
            format = format.replace("%1$s", PlayerColors.getPlayerUsername(e.getPlayer().getUniqueId()));
        } else {
            format = format.replace("%1$s", Misc.conv("&" + colorCode + e.getPlayer().getDisplayName()));
        }

        e.setFormat(format);
    }
}
