package me.maximus1027.NameColors.commands.admincommands;

import com.github.mittenmc.serverutils.SubCommand;
import me.maximus1027.NameColors.Main;
import me.maximus1027.NameColors.colors.NameColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class AdminSelectNameColorCommand extends SubCommand {

    @Override
    public String getName() {
        return "selectNameColor";
    }

    @Override
    public String getDescription() {
        return "Set a name color for the player regardless of permissions";
    }

    @Override
    public String getSyntax() {
        return "/namecoloradmin selectNameColor <player> <id>";
    }

    @Override
    public String getColoredSyntax() {
        return ChatColor.YELLOW + "Usage: " + getSyntax();
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
    public List<String> getSubcommandArguments(Player player, String[] args) {
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