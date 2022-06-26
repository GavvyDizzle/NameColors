package me.maximus1027.NameColors.Database;

import me.maximus1027.NameColors.Players.PlayerColors;
import me.maximus1027.NameColors.Utils.UUIDConverter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerData extends PluginDataHolder {
    public PlayerData(Plugin plugin, DataSource source) {
        super(plugin, source);
    }

    public String getPlayerPattern(Player player) {
        Connection conn;
        try {
            conn = conn();
        }
        catch (SQLException e) {
            logSQLError("Could not connect to the database", e);
            return null;
        }

        String pattern = "";

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT pattern FROM name_colors WHERE uuid = ?;");
            stmt.setBytes(1, UUIDConverter.convert(player.getUniqueId()));
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                pattern = resultSet.getString("pattern");
            }
            else {
                pattern = "";
            }
        } catch (SQLException e) {
            logSQLError("Could not retrieve player's username style pattern", e);
            return null;
        }


        return pattern;
    }

    public void savePlayerPattern(Player player) {
        if (!PlayerColors.isPatternSet(player.getUniqueId())) {
            return;
        }

        try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                "REPLACE name_colors(uuid, pattern)  VALUES(?,?);"
        )) {
            stmt.setBytes(1, UUIDConverter.convert(player.getUniqueId()));
            stmt.setString(2, PlayerColors.getPlayerPattern(player.getUniqueId()));
            stmt.execute();

        } catch (SQLException e) {
            logSQLError("Could not save player's username style pattern", e);
        }
    }

}
