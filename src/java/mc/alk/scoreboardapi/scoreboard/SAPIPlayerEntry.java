package mc.alk.scoreboardapi.scoreboard;

import mc.alk.scoreboardapi.api.SEntry;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class SAPIPlayerEntry implements SEntry{
	OfflinePlayer p;
    private String displayName;
    private String displayNameSuffix;
    private String displayNamePrefix;
    private String combinedDisplayName;


    public SAPIPlayerEntry(OfflinePlayer p){
		this.p = p;
		setDisplayName(p.getName());
	}

	public SAPIPlayerEntry(OfflinePlayer p, String display) {
		this.p = p;
		setDisplayName(display);
	}

	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(combinedDisplayName);
	}

    @Override
	public String getID() {
		return p.getName();
	}

    @Override
    public String getDisplayName(){
        return combinedDisplayName;
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
                (displayNameSuffix != null ? displayNameSuffix.length() : 0) > 15) {
            int size = (displayNamePrefix != null ? displayNamePrefix.length() : 0) +
                    (displayNameSuffix != null ? displayNameSuffix.length() : 0);
            this.combinedDisplayName = (displayNamePrefix != null ? displayNamePrefix : "")+
                    displayName.substring(0, 16 - size) + (displayNameSuffix != null ? displayNameSuffix : "");
        } else {
            this.combinedDisplayName = (displayNamePrefix != null ? displayNamePrefix : "")+
                    displayName + (displayNameSuffix != null ? displayNameSuffix : "");
        }
    }
}
