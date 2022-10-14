package me.maximus1027.NameColors.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.maximus1027.NameColors.Main;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MyExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "namecolors";
    }

    @Override
    public @NotNull String getAuthor() {
        return "GavvyDizzle";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {

        if (player.isOnline() && params.equalsIgnoreCase("name")) {
            return Main.getInstance().getColorManager().getPlayerNameFormatted(Objects.requireNonNull(player.getPlayer()));
        }
        return null;
    }
}