package me.github.gavvydizzle.NameColors.commands.admincommands;

import com.github.mittenmc.serverutils.SubCommand;
import me.github.gavvydizzle.NameColors.Main;
import me.github.gavvydizzle.NameColors.colors.NameColor;
import me.github.gavvydizzle.NameColors.commands.AdminCommandManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class AdminSelectNameColorCommand extends SubCommand {

    public AdminSelectNameColorCommand(AdminCommandManager commandManager) {
        setName("selectNameColor");
        setDescription("Directly set a player's name color");
        setSyntax("/" + commandManager.getCommandDisplayName() + " selectNameColor <player> <id>");
        setColoredSyntax(ChatColor.YELLOW + getSyntax());
        setPermission(commandManager.getPermissionPrefix() + getName().toLowerCase());
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(getColoredSyntax());
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Invalid player: " + args[1]);
            return;
        }

        NameColor nameColor = Main.getInstance().getColorManager().getNameColorByID(args[2]);
        if (nameColor == null) {
            sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a valid name color!");
            return;
        }

        Main.getInstance().getColorManager().setNameColor(player, nameColor);

        player.sendMessage(ChatColor.GREEN + "Your name color has been updated to " + nameColor.getId());
        if (player != sender) {
            sender.sendMessage(ChatColor.GREEN + "Successfully updated " + player.getName() + "'s name color to " + nameColor.getId());
        }
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        ArrayList<String> list = new ArrayList<>();

        if (args.length == 2) {
            return null;
        }
        else if (args.length == 3) {
            StringUtil.copyPartialMatches(args[2], Main.getInstance().getColorManager().getNameColorIDs(), list);
        }

        return list;
    }

}