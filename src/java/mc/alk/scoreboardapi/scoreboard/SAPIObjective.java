package mc.alk.scoreboardapi.scoreboard;

import mc.alk.scoreboardapi.api.SAPI;
import mc.alk.scoreboardapi.api.SEntry;
import mc.alk.scoreboardapi.api.SObjective;
import mc.alk.scoreboardapi.api.SScoreboard;
import mc.alk.scoreboardapi.api.STeam;
import org.bukkit.OfflinePlayer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

public class SAPIObjective implements SObjective{
	protected final String id;
	protected String criteria;
    protected String combinedDisplayName;
	protected String displayName;
    protected String displayNameSuffix;
    protected String displayNamePrefix;
	protected SAPIDisplaySlot slot;

    protected SScoreboard scoreboard;

	/// Used for Team support
	protected boolean displayPlayers;
	protected boolean displayTeams;

	// 1-1000 scale, not strictly enforced
	// the lower priorities will not be preempted when set
	int priority;

	protected Set<SEntry> entries = new HashSet<SEntry>();
    protected TreeMap<Integer,SEntry> scores = new TreeMap<Integer,SEntry>(Collections.reverseOrder());


    public SAPIObjective(String id, String displayName, String criteria) {
		this(id, displayName,criteria,50);
	}

	public SAPIObjective(String id, String displayName, String criteria, int priority) {
        this.id = id;
        this.criteria = colorChat(criteria);
        this.priority = priority;
        setDisplayName(displayName);
        displayPlayers = true;
        displayTeams = true;
        slot = SAPIDisplaySlot.NONE;
    }

	public SScoreboard getScoreboard() {
		return scoreboard;
	}

	public int getPriority(){
		return priority;
	}

	public String getID(){
		return id;
	}

	public String getCriteria() {
		return criteria;
	}

    public String getBaseDisplayName() {
        return displayName;
    }

	public String getDisplayName(){
		return combinedDisplayName;
	}

	public void setDisplayName(String displayName){
		this.displayName = colorChat(displayName);
        _setDisplayName();
    }

    @Override
    public String getDisplayNameSuffix() {
        return displayNameSuffix;
    }

    @Override
	public void setDisplayNameSuffix(String suffix) {
		this.displayNameSuffix = colorChat(suffix);
        _setDisplayName();
	}

    @Override
    public String getDisplayNamePrefix() {
        return displayNamePrefix;
    }

    @Override
    public void setDisplayNamePrefix(String prefix) {
        this.displayNamePrefix = colorChat(prefix);
        _setDisplayName();
    }

    public void setDisplaySlot(SAPIDisplaySlot slot) {
		this.slot = slot;
		if (scoreboard != null){
			scoreboard.setDisplaySlot(slot, this,true);
		}
	}

	public SAPIDisplaySlot getDisplaySlot() {
		return this.slot;
	}

	public void setDisplayPlayers(boolean display) {
		this.displayPlayers = display;
	}

	public void setDisplayTeams(boolean display) {
		this.displayTeams = display;
	}

	public boolean isDisplayPlayers(){
		return this.displayPlayers;
	}

	public void setScoreBoard(SScoreboard sapiScoreboard) {
		this.scoreboard = sapiScoreboard;
	}

	public boolean setPoints(SEntry e, int points) {
//        if (!scores.containsKey(e))
        return false;
    }

	public boolean setTeamPoints(STeam team, int points) {
		if (displayTeams){
			setPoints(team,points);
		}
		if (displayPlayers){
			for (OfflinePlayer p: team.getPlayers()){
				SEntry e = scoreboard.getOrCreateEntry(p);
				setPoints(e,points);
			}
		}
		return true;
	}

	public boolean setPoints(String id, int points) {
		if (scoreboard == null)
			return false;
		SEntry l = scoreboard.getEntry(id);
		if (l == null)
			return false;
		setPoints(l,points);
		return true;
	}

	public int getPoints(String id) {
		SEntry l = scoreboard.getEntry(id);
		if (l == null)
			return -1;
		return getPoints(l);
	}

	protected int getPoints(SEntry entry) {
		return -1;
	}

	public static String colorChat(String msg) {return msg.replace('&', (char) 167);}

	@Override
	public SEntry addEntry(OfflinePlayer p, int points) {
		return addEntry(p.getName(),points);
	}

	@Override
	public SEntry addEntry(String id, int points) {
		SEntry e = scoreboard.getEntry(id);
		if (e == null){
			if (getScoreboard() != null)
				e = scoreboard.createEntry(id, id);
			else
				throw new IllegalStateException("You cannot add an entry that hasnt already been created " + id);
		}
		addEntry(e,points);
		return e;
	}

	@Override
	public boolean addEntry(SEntry entry, int points) {
		setPoints(entry,points);
		if (entry instanceof STeam){
			return addTeam((STeam)entry, points);
		}
		return entries.add(entry);
	}

	@Override
	public SEntry removeEntry(OfflinePlayer player) {
		SEntry e = scoreboard.getEntry(player);
		if (e == null)
			return null;
		return removeEntry(e);
	}

	@Override
	public SEntry removeEntry(String id) {
		SEntry e = scoreboard.getEntry(id);
		if (e == null)
			return null;
		return removeEntry(e);
	}

	@Override
	public SEntry removeEntry(SEntry entry) {
		return entries.remove(entry) ? entry : null;
	}

	public STeam addTeam(String id, int points) {
		STeam t = scoreboard.getTeam(id);
		if (t != null){
			addTeam(t,points);
		}
		return t;
	}

	public boolean addTeam(STeam entry, int points) {
		entries.add(entry);
		for (OfflinePlayer e: entry.getPlayers()){
			entries.add(scoreboard.getOrCreateEntry(e));
		}
		if (isDisplayTeams()){
			setPoints(entry, points);
		}
		if (isDisplayPlayers()){
			for (OfflinePlayer e: entry.getPlayers()){
				setPoints(scoreboard.getOrCreateEntry(e),points);
			}
		}
		return true;
	}

	public boolean isDisplayTeams() {
		return this.displayTeams;
	}

	public boolean contains(SEntry e) {
		return entries.contains(e);
	}

    private void _setDisplayName(){
        if ((displayNamePrefix != null ? displayNamePrefix.length() : 0) +
                displayName.length() +
                (displayNameSuffix != null ? displayNameSuffix.length() : 0) > SAPI.MAX_NAMESIZE) {
            int size = (displayNamePrefix != null ? displayNamePrefix.length() : 0) +
                    (displayNameSuffix != null ? displayNameSuffix.length() : 0);
            this.combinedDisplayName = (displayNamePrefix != null ? displayNamePrefix : "")+
                    displayName.substring(0, SAPI.MAX_NAMESIZE - size) + (displayNameSuffix != null ? displayNameSuffix : "");
        } else {
            this.combinedDisplayName = (displayNamePrefix != null ? displayNamePrefix : "")+
                    displayName + (displayNameSuffix != null ? displayNameSuffix : "");
        }
    }


}
