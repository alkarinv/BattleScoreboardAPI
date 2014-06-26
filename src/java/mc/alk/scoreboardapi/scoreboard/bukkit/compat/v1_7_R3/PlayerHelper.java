package mc.alk.scoreboardapi.scoreboard.bukkit.compat.v1_7_R3;

import mc.alk.scoreboardapi.scoreboard.bukkit.compat.IPlayerHelper;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

/**
 * @author alkarin
 */
public class PlayerHelper implements IPlayerHelper {

    @Override
    public UUID getID(OfflinePlayer player) {
        return player.getUniqueId();
    }

    @Override
    public UUID getID(String name) {
        return new UUID(0, name.hashCode());
    }

}
