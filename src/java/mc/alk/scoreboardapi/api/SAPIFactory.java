package mc.alk.scoreboardapi.api;

import mc.alk.scoreboardapi.scoreboard.SAPIDisplaySlot;
import mc.alk.scoreboardapi.scoreboard.SAPIObjective;
import mc.alk.scoreboardapi.scoreboard.SAPIScoreboard;
import mc.alk.scoreboardapi.scoreboard.bukkit.BObjective;
import mc.alk.scoreboardapi.scoreboard.bukkit.BScoreboard;

public class SAPIFactory {
	private static boolean hasBukkitScoreboard = false;
	static{
		try {
			Class.forName("org.bukkit.scoreboard.Scoreboard");
			SAPIFactory.hasBukkitScoreboard = true;
		} catch (ClassNotFoundException e) {
			SAPIFactory.hasBukkitScoreboard = false;
		}
	}
    /**
     * Create a new Objective
     * @param id id of the Objective
     * @param displayName how to display the Objective
     * @param criteria What is the criteria
     * @param slot Which display slot to use [SIDEBAR, PLAYER_LIST, BELOW_NAME]
     * @param priority lower means this Objective will be shown in the same slot over another with higher priority
     * @return The new Objective
     */
	public static SAPIObjective createObjective(String id, String displayName, String criteria,
			SAPIDisplaySlot slot, int priority) {
		SAPIObjective o = hasBukkitScoreboard ? new BObjective(id,displayName,criteria,priority) :
			new SAPIObjective(id,displayName,criteria,priority);
		o.setDisplaySlot(slot);
		return o;
	}

    public static SScoreboard createScoreboard(String name) {
        return hasBukkitScoreboard ? new BScoreboard(name) :  new SAPIScoreboard(name);
    }

    public static SScoreboard createSAPIScoreboard(String name) {
        return new SAPIScoreboard(name);
    }

}
