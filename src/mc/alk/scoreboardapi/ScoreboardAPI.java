package mc.alk.scoreboardapi;

import mc.alk.scoreboardapi.api.SAPIFactory;
import mc.alk.scoreboardapi.api.SScoreboard;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class ScoreboardAPI extends JavaPlugin{

	static private String pluginname;
	static private String version;

	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();

		pluginname = pdfFile.getName();
		version = pdfFile.getVersion();

		this.getCommand("scoreboardapi").setExecutor(new ScoreboardAPIExecutor());
	}

	@Override
	public void onDisable() {

	}

    public static SScoreboard createScoreboard(String name) {
        return SAPIFactory.createScoreboard(name);
    }

    public static SScoreboard createSAPIScoreboard(String name) {
        return SAPIFactory.createSAPIScoreboard(name);
    }

}
