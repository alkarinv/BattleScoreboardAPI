package mc.alk.scoreboardapi.scoreboard;

import mc.alk.scoreboardapi.api.SAPI;
import mc.alk.scoreboardapi.api.SEntry;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class SAPIEntry implements SEntry{
	private final String id;
    private String displayName;
    private String displayNameSuffix;
    private String displayNamePrefix;
    private String combinedDisplayName;

	public SAPIEntry(String id, String displayName){
		this.id = id;
        setDisplayName(displayName);
	}

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

    public String toString() {
        return "[SAPIEntry " + this.getID() + " : " + this.getDisplayName() + "]";
    }
}
