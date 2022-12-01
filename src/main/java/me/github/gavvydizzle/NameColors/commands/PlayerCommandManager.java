package me.github.gavvydizzle.NameColors.commands;

import me.github.gavvydizzle.NameColors.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerCommandManager implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        if (sender instanceof Player) {
            Main.getInstance().getGuiManager().openNameColorListInventory(((Player) sender).getPlayer());
        }
        return true;
    }
}