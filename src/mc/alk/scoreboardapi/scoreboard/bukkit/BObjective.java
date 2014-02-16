package mc.alk.scoreboardapi.scoreboard.bukkit;

import mc.alk.scoreboardapi.api.SEntry;
import mc.alk.scoreboardapi.api.SScoreboard;
import mc.alk.scoreboardapi.api.STeam;
import mc.alk.scoreboardapi.scoreboard.SAPIDisplaySlot;
import mc.alk.scoreboardapi.scoreboard.SAPIObjective;
import mc.alk.scoreboardapi.scoreboard.SAPIPlayerEntry;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class BObjective extends SAPIObjective{
	Objective o;

	public BObjective(SScoreboard board, String name, String criteria) {
		this(board,name,criteria,50);
	}

	public BObjective(String name, String criteria, int priority) {
		this(null,name, criteria,priority);
	}

	public BObjective(SScoreboard board, String name, String criteria, int priority) {
		super(name, criteria,priority);
		if (board != null)
			setScoreBoard(board);
	}

	@Override
	public void setDisplayName(String displayName){
		super.setDisplayName(displayName);
		if (o == null)
			return;
        _setDisplayName();
    }

	@Override
	public void setDisplayNameSuffix(String suffix){
		super.setDisplayNameSuffix(suffix);
		if (o == null)
			return;
        _setDisplayName();
	}

    @Override
    public void setDisplayNamePrefix(String prefix){
        super.setDisplayNameSuffix(prefix);
        if (o == null)
            return;
        _setDisplayName();
    }

    private void _setDisplayName(){
        if (displayNamePrefix.length() + getDisplayName().length() + displayNameSuffix.length() <= 32){
            o.setDisplayName(displayNamePrefix+getDisplayName()+displayNameSuffix);
        } else {
            o.setDisplayName(displayNamePrefix+getDisplayName()+displayNameSuffix);
        }
    }

    @Override
	public void setScoreBoard(SScoreboard board) {
		if (!(board instanceof BScoreboard))
			throw new IllegalStateException("To use BukkitObjectives you must use BukkitScoreboards");
		super.setScoreBoard(board);

		o = ((BScoreboard)board).board.getObjective(name);
		if (o == null)
			o = ((BScoreboard)board).board.registerNewObjective(name,criteria);
		setDisplayName(getDisplayName());
	}

	@Override
	public boolean setPoints(SEntry l, int points) {
		super.setPoints(l, points);
		this.entries.add(l);
		if (l instanceof STeam){
			if (this.isDisplayTeams())
				setScore(l,points);
		} else if (l instanceof SAPIPlayerEntry){
			if (this.isDisplayPlayers())
				setScore(l,points);
		} else {
			setScore(l,points);
		}
		return true;
	}

	private void setScore(SEntry e, int points){
		if (points != 0){
			o.getScore(e.getOfflinePlayer()).setScore(points);
		} else {
			o.getScore(e.getOfflinePlayer()).setScore(1);
			o.getScore(e.getOfflinePlayer()).setScore(0);
		}
	}

	@Override
	public int getPoints(SEntry l) {
		OfflinePlayer p = l.getOfflinePlayer();
		return o.getScore(p).getScore();
	}

	@Override
	public void setDisplaySlot(SAPIDisplaySlot slot) {
		super.setDisplaySlot(slot);
		if (scoreboard == null)
			return;
		if (o != null && scoreboard.getObjective(slot)==this){
			o.setDisplaySlot(toDisplaySlot(slot));
		}
	}

	@Override
	public void setDisplayPlayers(boolean display){
		if (display == isDisplayPlayers())
			return;
		this.displayPlayers = display;
		if (this.scoreboard != null ){
			for (SEntry entry : this.scoreboard.getEntries()){
				if (!display && contains(entry) && entry instanceof SAPIPlayerEntry){
					removeEntry(entry);
				} else if (display && entry instanceof STeam){
					for (OfflinePlayer p: ((STeam)entry).getPlayers()){
						SEntry e = scoreboard.getOrCreateEntry(p);
						addEntry(e,0);
					}
				}
			}
		}
	}

	@Override
	public void setDisplayTeams(boolean display){
		if (display == isDisplayTeams())
			return;
		this.displayTeams = display;
		if (this.scoreboard != null ){
			for (SEntry entry : this.scoreboard.getEntries()){
				if (!display && contains(entry) && entry instanceof STeam){
					removeEntry(entry);
					entries.add(entry); // dont display, but keep it as an entry
				}
			}
		}
	}

	public static DisplaySlot toDisplaySlot(SAPIDisplaySlot slot) {
		switch (slot){
		case BELOW_NAME:
			return DisplaySlot.BELOW_NAME;
		case PLAYER_LIST:
			return DisplaySlot.PLAYER_LIST;
		case SIDEBAR:
			return DisplaySlot.SIDEBAR;
		case NONE:
			return null;
		default:
			return null;
		}
	}

	@SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    @Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("&6 --- "+this.getName() +" : "+this.getDisplayName()+"&4 : " + this.getPriority()+"\n");
		if (scoreboard == null){
			sb.append("&4 Bukkit scoreboard still not set!!");
			return sb.toString();
		}
		Collection<SEntry> es = scoreboard.getEntries();
		if (o == null){
			sb.append("&4 Bukkit Objective still not set!!");
			return sb.toString();
		}
		List<SEntry> zeroes = new ArrayList<SEntry>();
		List<SEntry> skipped = new ArrayList<SEntry>();
		for (SEntry e: es){
			//			OfflinePlayer p = this.getDisplaySlot()==SAPIDisplaySlot.PLAYER_LIST ? e.getPlayerListName() : e.getOfflinePlayer();
			if (!this.contains(e)){
				skipped.add(e);
				continue;
			}
			Set<Score> scores = o.getScoreboard().getScores(e.getOfflinePlayer());
			for (Score score : scores){
				if (score.getObjective().equals(o)){
					if (score.getScore() != 0){
						if (e instanceof BukkitTeam){
							BukkitTeam bt = ((BukkitTeam)e);
							sb.append("&e " + e.getID() +" : " + e.getDisplayName() +" = "+ score.getScore() +"  &eteamMembers=\n");
							for (OfflinePlayer p : bt.getPlayers()){
								SEntry ep = this.getScoreboard().getOrCreateEntry(p);
								String c = this.contains(ep) ? "&e" : "&8";
								sb.append(
										"  "+c+"- &f" +bt.getPrefix()+p.getName()+bt.getSuffix()+c+" = " + o.getScore(p).getScore()+"\n");
							}
						} else {
							sb.append("&6 " + e.getID() +" : " + e.getDisplayName() +" = "+ score.getScore()+"\n");
						}
					} else {
						zeroes.add(e);
					}
				}
			}
		}
		if (!skipped.isEmpty()){
			sb.append(" &cSkipped Entries: ");
			for (SEntry e: skipped){
				sb.append("&6 "+e.getID()+":"+e.getDisplayName()+"&e,");}
			sb.append("\n");
		}
		if (!zeroes.isEmpty()){
			sb.append(" &eZero Entries: ");
			for (SEntry e: zeroes){
				sb.append("&6 '"+e.getID()+"':'"+e.getDisplayName()+"'&e,");}
			sb.append("\n");
		}
		return sb.toString();
	}

}
