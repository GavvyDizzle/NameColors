package me.github.gavvydizzle.NameColors.commands.admincommands;

import com.github.mittenmc.serverutils.SubCommand;
import me.github.gavvydizzle.NameColors.Main;
import me.github.gavvydizzle.NameColors.commands.AdminCommandManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class AdminReloadCommand extends SubCommand {

    public AdminReloadCommand(AdminCommandManager commandManager) {
        setName("reload");
        setDescription("Reloads this plugin");
        setSyntax("/" + commandManager.getCommandDisplayName() + " reload");
        setColoredSyntax(ChatColor.YELLOW + getSyntax());
        setPermission(commandManager.getPermissionPrefix() + getName().toLowerCase());
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
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

}