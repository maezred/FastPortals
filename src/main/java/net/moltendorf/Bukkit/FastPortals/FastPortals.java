package net.moltendorf.Bukkit.FastPortals;

	import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by moltendorf on 15/06/29.
 *
 * @author moltendorf
 */
public class FastPortals extends JavaPlugin {
	// Main instance.
	private static FastPortals instance = null;

	protected static FastPortals getInstance() {
		return instance;
	}

	// Variable data.
	protected Settings settings = null;

	@Override
	public void onEnable() {
		instance = this;

		// Construct new settings.
		settings = new Settings();

		if (settings.isEnabled()) {
			getServer().getPluginManager().registerEvents(new Listeners(), this);
		}
	}

	@Override
	public void onDisable() {
		instance = null;
	}
}
