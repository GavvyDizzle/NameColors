package me.maximus1027.NameColors.commands.admincommands;

import com.github.mittenmc.serverutils.SubCommand;
import me.maximus1027.NameColors.Main;
import me.maximus1027.NameColors.colors.NameColor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class AdminTestNameColorCommand extends SubCommand {

    @Override
    public String getName() {
        return "testNameColor";
    }

    @Override
    public String getDescription() {
        return "Test what the name color formatting will do to text";
    }

    @Override
    public String getSyntax() {
        return "/namecoloradmin testNameColor <id> <message>";
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

        if (args.length > 3) {
            sender.sendMessage(ChatColor.RED + "No spaces are allowed in the message portion");
            return;
        }

        NameColor nameColor = Main.getInstance().getColorManager().getNameColorByID(args[1]);
        if (nameColor == null) {
            sender.sendMessage(ChatColor.RED + "'" + args[1] + "' is not a valid name color!");
            return;
        }

        sender.sendMessage("Output: " + nameColor.getPattern().withPattern(args[2]));
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        ArrayList<String> list = new ArrayList<>();

        if (args.length == 2) {
            StringUtil.copyPartialMatches(args[1], Main.getInstance().getColorManager().getNameColorIDs(), list);
        }

        return list;
    }

}