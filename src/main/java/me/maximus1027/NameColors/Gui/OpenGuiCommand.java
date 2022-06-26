package me.maximus1027.NameColors.Gui;

import me.maximus1027.NameColors.Main;
import me.maximus1027.NameColors.Utils.Misc;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class OpenGuiCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (args.length > 0 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("namecolors.reload")) {
            player.sendMessage(Misc.conv("&aNameColors has been reloaded."));
            Main.getInstance().reloadConfig();
            NameColorsUI.Load();
            return false;
        }

        new NameColorsUI(player);

        return false;
    }
}
