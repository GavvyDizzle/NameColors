package me.maximus1027.NameColors.database;

import com.github.mittenmc.serverutils.UUIDConverter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerData extends PluginDataHolder {
    public PlayerData(Plugin plugin, DataSource source) {
        super(plugin, source);
    }

    public String getPlayerPatternID(Player player) {
        Connection conn;
        try {
            conn = conn();
        }
        catch (SQLException e) {
            logSQLError("Could not connect to the database", e);
            return null;
        }

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT patternID FROM name_colors WHERE uuid = ?;");
            stmt.setBytes(1, UUIDConverter.convert(player.getUniqueId()));
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("patternID");
            }
        } catch (SQLException e) {
            logSQLError("Could not retrieve player's patternID", e);
            return null;
        }

        return null;
    }

    public void savePlayerPattern(Player player, String patternID) {
        Connection conn;
        try {
            conn = conn();
        }
        catch (SQLException e) {
            logSQLError("Could not connect to the database", e);
            return;
        }

        try {
            PreparedStatement stmt = conn.prepareStatement("REPLACE name_colors(uuid, patternID) VALUES(?,?);");
            stmt.setBytes(1, UUIDConverter.convert(player.getUniqueId()));
            stmt.setString(2, patternID);
            stmt.execute();
        } catch (SQLException e) {
            logSQLError("Could not save player's patternID", e);
        }
    }

    public void savePlayerPatterns(ConcurrentHashMap<UUID, String> map) {
        Connection conn;
        try {
            conn = conn();
        }
        catch (SQLException e) {
            logSQLError("Could not connect to the database", e);
            return;
        }

        try {
            PreparedStatement stmt = conn.prepareStatement("REPLACE name_colors(uuid, patternID) VALUES(?,?);");

            for (UUID uuid : map.keySet()) {
                if (map.get(uuid) == null) continue;
                stmt.setBytes(1, UUIDConverter.convert(uuid));
                stmt.setString(2, map.get(uuid));
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            logSQLError("Could not save all players' patternIDs", e);
        }
    }

}
