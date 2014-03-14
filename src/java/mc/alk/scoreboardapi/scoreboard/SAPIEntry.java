package mc.alk.scoreboardapi.scoreboard;

import mc.alk.scoreboardapi.SAPIUtil;
import mc.alk.scoreboardapi.api.SAPI;
import mc.alk.scoreboardapi.api.SEntry;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class SAPIEntry implements SEntry, Comparable<SEntry>{
	private final String id;
    private String displayName;
    private String displayNameSuffix;
    private String displayNamePrefix;
    private String combinedDisplayName;

	public SAPIEntry(String id, String displayName){
		this.id = id;
        setDisplayName(displayName);
	}

	@Override
    public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(combinedDisplayName);
	}

	public OfflinePlayer getPlayerListName(){
		return Bukkit.getOfflinePlayer(combinedDisplayName);
	}

    @Override
	public String getID() {
		return id;
	}

    @Override
    public String getDisplayName(){
        return combinedDisplayName;
    }

    @Override
    public String getBaseDisplayName(){
        return displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = SAPIObjective.colorChat(displayName);
        _setDisplayName();
    }

    @Override
    public void setDisplayNameSuffix(String suffix) {
        displayNameSuffix = SAPIObjective.colorChat(suffix);
        if (displayNameSuffix.length() > 8) {
            displayNameSuffix = displayNameSuffix.substring(0, 9);
        }
        _setDisplayName();
    }

    @Override
    public String getDisplayNameSuffix() {
        return displayNameSuffix;
    }

    @Override
    public void setDisplayNamePrefix(String suffix) {
        displayNamePrefix = SAPIObjective.colorChat(suffix);
        if (displayNamePrefix.length() > 8) {
            displayNamePrefix = displayNamePrefix.substring(0, 9);
        }
        _setDisplayName();
    }

    @Override
    public String getDisplayNamePrefix() {
        return displayNamePrefix;
    }

    protected void _setDisplayName() {
        this.combinedDisplayName = SAPIUtil.createLimitedString(displayNamePrefix, displayName,
                displayNameSuffix, SAPI.MAX_NAMESIZE);
    }

    public String toString() {
        return "[SAPIEntry " + this.getID() + " : " + this.getDisplayName() + "]";
    }

    @Override
    public int compareTo(SEntry o) {
        return this.getID().compareTo(o.getID());
    }
}
