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

	public static SAPIObjective createObjective(String name, String criteria,
			SAPIDisplaySlot slot, int priority) {
		SAPIObjective o = hasBukkitScoreboard ? new BObjective(name,criteria,priority) :
			new SAPIObjective(name,criteria,priority);
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
