package mc.alk.scoreboardapi.scoreboard.bukkit;

import mc.alk.scoreboardapi.api.SObjective;
import mc.alk.scoreboardapi.scoreboard.SAPIObjective;
import mc.alk.scoreboardapi.scoreboard.SAPITeam;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Team;

import java.util.Collection;
import java.util.Set;

public class BukkitTeam extends SAPITeam {
	Team team;

	public BukkitTeam(BScoreboard board, Team team) {
		super(board, team.getName(), team.getDisplayName());
		this.team = team;
	}

	@Override
	public void addPlayers(Collection<? extends OfflinePlayer> players) {
		for (OfflinePlayer p : players){
			team.addPlayer(p);
		}
		if (board != null){
			for (SObjective o : board.getObjectives()){
				if (o.isDisplayPlayers() && o.contains(this)){
					for (OfflinePlayer player: team.getPlayers()){
						o.addEntry(player, 0);
					}
				}
			}
		}
	}

	@Override
	public void addPlayer(OfflinePlayer p) {
		super.addPlayer(p);
		team.addPlayer(p);
		if (board != null){
			for (SObjective o : board.getObjectives()){
				if (o.isDisplayPlayers() && o.contains(this)){
					o.addEntry(p, 0);
				}
			}
		}
	}

	@Override
	public void removePlayer(OfflinePlayer p){
		super.removePlayer(p);
		team.removePlayer(p);
	}

	@Override
	public Set<OfflinePlayer> getPlayers() {
		return team.getPlayers();
	}

	@Override
	public void setPrefix(String prefix){
		prefix = SAPIObjective.colorChat(prefix);
		super.setPrefix(prefix);
		team.setPrefix(prefix);
	}

	@Override
	public void setSuffix(String suffix){
		suffix = SAPIObjective.colorChat(suffix);
		super.setSuffix(suffix);
		team.setSuffix(suffix);
	}

	@Override
	public String getPrefix() {
		return team.getPrefix();
	}

	@Override
	public String getSuffix() {
		return team.getSuffix();
	}

    @Override
    public int size() {
        return team.getSize();
    }
}
