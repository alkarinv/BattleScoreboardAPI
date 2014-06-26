package mc.alk.scoreboardapi.scoreboard.bukkit.compat.v1_7_R2;

import mc.alk.scoreboardapi.scoreboard.bukkit.compat.IPlayerHelper;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

/**
 * @author alkarin
 */
public class PlayerHelper implements IPlayerHelper {

    @Override
    public UUID getID(OfflinePlayer player) {
        return new UUID(0, player.getName().hashCode());
    }

    @Override
    public UUID getID(String name) {
        return new UUID(0, name.hashCode());
    }
}
