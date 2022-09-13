package me.maximus1027.NameColors.commands.admincommands;

import com.github.mittenmc.serverutils.SubCommand;
import me.maximus1027.NameColors.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AdminReloadCommand extends SubCommand {

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reloads all data from the config";
    }

    @Override
    public String getSyntax() {
        return "/namecoloradmin reload";
    }

    @Override
    public String getColoredSyntax() {
        return ChatColor.YELLOW + "Usage: " + getSyntax();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        try {
            Main.getInstance().reloadConfig();
            Main.getInstance().getColorManager().reload();
            Main.getInstance().getGuiManager().reloadAllGUIs();
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "[NameColors] Encountered an error when reloading. Check the console");
            e.printStackTrace();
            return;
        }

        sender.sendMessage(ChatColor.GREEN + "[NameColors] Reloaded");
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return new ArrayList<>();
    }

}