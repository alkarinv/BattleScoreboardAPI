package mc.alk.scoreboardapi.scoreboard.bukkit;

import mc.alk.scoreboardapi.api.SEntry;
import mc.alk.scoreboardapi.api.SObjective;
import mc.alk.scoreboardapi.scoreboard.SAPIDisplaySlot;
import mc.alk.scoreboardapi.scoreboard.SAPIObjective;
import mc.alk.scoreboardapi.scoreboard.SAPIScoreboard;
import mc.alk.scoreboardapi.scoreboard.SAPITeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class BScoreboard extends SAPIScoreboard{

	protected Scoreboard board;

	HashMap<String,Scoreboard> oldBoards = new HashMap<String,Scoreboard>();

	public BScoreboard(String name) {
		super(name);
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		board = manager.getNewScoreboard();
	}

	@Override
	public SAPIObjective registerNewObjective(String id, String displayName, String criteria,
			SAPIDisplaySlot slot) {
		BObjective o =  new BObjective(this,id, displayName,criteria);
		o.setDisplayName(displayName);
		o.setDisplaySlot(slot);
		registerNewObjective(o);
		return o;
	}

    @Override
	public void setScoreboard(Player p) {
		if (p.getScoreboard() != null || !oldBoards.containsKey(p.getName()))
			oldBoards.put(p.getName(), p.getScoreboard());
		p.setScoreboard(this.board);
	}

    @Override
	public void removeScoreboard(Player player) {
		try {
			if (oldBoards.containsKey(player.getName())){
				player.setScoreboard(oldBoards.remove(player.getName()));
			} else {
				player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

    private class BoardUpdate{
        BoardUpdate(HashMap<Objective, Integer> scores, Team team){ this.scores=scores; this.team = team;}
        HashMap<Objective, Integer> scores;
        Team team;
    }

    private BoardUpdate clearBoard(SEntry e){
        HashMap<Objective, Integer> oldScores = new HashMap<Objective, Integer>();
        Set<Score> scores = board.getScores(e.getOfflinePlayer());
        for (Score score : scores) {
            oldScores.put(score.getObjective(), score.getScore());
        }
        board.resetScores(e.getOfflinePlayer());
        Team t = board.getPlayerTeam(e.getOfflinePlayer());
        if (t != null)
            t.removePlayer(e.getOfflinePlayer());

        return new BoardUpdate(oldScores, t);
    }

    private void updateBoard(SEntry e, BoardUpdate bu){
        if (bu.team != null){
            bu.team.addPlayer(e.getOfflinePlayer());
        }
        for (Entry<Objective, Integer> entry : bu.scores.entrySet()) {
            if (entry.getValue() == 0) {
                entry.getKey().getScore(e.getOfflinePlayer()).setScore(1);
            }
            entry.getKey().getScore(e.getOfflinePlayer()).setScore(entry.getValue());
        }
    }

    @Override
    public void setEntryDisplayName(SEntry e, String name) {
        BoardUpdate bu = clearBoard(e);
        super.setEntryDisplayName(e, name);
        updateBoard(e, bu);
    }

    @Override
    public void setEntryNamePrefix(SEntry e, String name) {
        BoardUpdate bu = clearBoard(e);
        super.setEntryNamePrefix(e, name);
        updateBoard(e, bu);
    }

    @Override
    public void setEntryNameSuffix(SEntry e, String name) {
        BoardUpdate bu = clearBoard(e);
        super.setEntryNameSuffix(e, name);
        updateBoard(e, bu);
    }

    @Override
	public SEntry removeEntry(SEntry e) {
		board.resetScores(e.getOfflinePlayer());
		return super.removeEntry(e);
	}

	public Scoreboard getBukkitScoreboard(){
		return board;
	}

	@Override
	public SAPITeam createTeamEntry(String id, String displayName) {
		if (this.board.getTeam(id) != null)
			return this.getTeam(id);
		Team t = this.board.registerNewTeam(id);
		t.setDisplayName(displayName);
		BukkitTeam st = new BukkitTeam(this,t);
		handler.registerEntry(st);
		return st;
	}

	@Override
	public SAPITeam getTeam(String id) {
		SEntry e = handler.getEntry(id);
		return (e == null || !(e instanceof SAPITeam)) ? null : (SAPITeam)e;
	}

	public void addAllEntries(SObjective objective) {
		for (SEntry entry : handler.getEntries()){
			objective.addEntry(entry, 0);
		}
	}
}
