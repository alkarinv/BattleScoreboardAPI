package mc.alk.scoreboardapi.scoreboard;

import mc.alk.scoreboardapi.api.STeam;
import org.bukkit.OfflinePlayer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SAPITeam extends SAPIEntry implements STeam{
	protected SAPIScoreboard board;

	public SAPITeam(SAPIScoreboard board, String id, String displayName) {
		super(id, displayName);
		this.board = board;
	}

	public void addPlayer(OfflinePlayer p) {
		this.board.createEntry(p);
	}

	@Override
	public void addPlayers(Collection<? extends OfflinePlayer> players) {
		for (OfflinePlayer p: players){
			addPlayer(p);
		}
	}

	public void removePlayer(OfflinePlayer p) {
		this.board.removeEntry(p);
	}

	public Set<OfflinePlayer> getPlayers() {
		return new HashSet<OfflinePlayer>(0);
	}

	public void setPrefix(String prefix){
		/* do nothing */
	}

	public void setSuffix(String suffix){
		/* do nothing */
	}

	@Override
	public String getPrefix() {
		return null;
	}

	@Override
	public String getSuffix() {
		return null;
	}

    @Override
    public int size() {
        return 0;
    }


}
