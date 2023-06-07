package me.github.gavvydizzle.NameColors.commands;

import com.github.mittenmc.serverutils.CommandManager;
import me.github.gavvydizzle.NameColors.commands.admincommands.AdminHelpCommand;
import me.github.gavvydizzle.NameColors.commands.admincommands.AdminReloadCommand;
import me.github.gavvydizzle.NameColors.commands.admincommands.AdminSelectNameColorCommand;
import me.github.gavvydizzle.NameColors.commands.admincommands.AdminTestNameColorCommand;
import org.bukkit.command.PluginCommand;

public class AdminCommandManager extends CommandManager {

    public AdminCommandManager(PluginCommand command) {
        super(command);

        registerCommand(new AdminHelpCommand(this));
        registerCommand(new AdminReloadCommand(this));
        registerCommand(new AdminSelectNameColorCommand(this));
        registerCommand(new AdminTestNameColorCommand(this));
        sortCommands();
    }
}