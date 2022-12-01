package me.github.gavvydizzle.NameColors.commands.admincommands;

import com.github.mittenmc.serverutils.SubCommand;
import me.github.gavvydizzle.NameColors.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AdminHelpCommand extends SubCommand {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Opens this help menu";
    }

    @Override
    public String getSyntax() {
        return "/namecoloradmin help";
    }

    @Override
    public String getColoredSyntax() {
        return ChatColor.YELLOW + "Usage: " + getSyntax();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.GOLD +  "-----(NameColors Admin Commands)-----");
        ArrayList<SubCommand> subCommands = Main.getInstance().getAdminCommandManager().getSubcommands();
        for (SubCommand subCommand : subCommands) {
            sender.sendMessage(ChatColor.GOLD + subCommand.getSyntax() + " - " + ChatColor.YELLOW + subCommand.getDescription());
        }
        sender.sendMessage(ChatColor.GOLD +  "-----(Vouchers Admin Commands)-----");
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return new ArrayList<>();
    }

}