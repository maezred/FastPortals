package net.moltendorf.Bukkit.FastPortals;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by moltendorf on 15/06/29.
 *
 * @author moltendorf
 */
public class Settings {
	protected static class WorldSettings {
		final public String name;
		final public int    radius;
		final public double multiplier;
		final public String connection;

		private WorldSettings(final ConfigurationSection worldSection) {
			name = worldSection.getName();

			if (worldSection.isConfigurationSection("connection")) {
				final ConfigurationSection connectionSection = worldSection.getConfigurationSection("connection");

				if (connectionSection.isString("world")) {
					radius = connectionSection.getInt("radius", 128);
					multiplier = connectionSection.getDouble("multiplier", 1);
					connection = connectionSection.getString("world");
				} else {
					radius = 0;
					multiplier = 0;
					connection = null;
				}
			} else {
				radius = 0;
				multiplier = 0;
				connection = null;
			}
		}
	}

	protected static Settings getInstance() {
		return FastPortals.getInstance().settings;
	}

	final private FileConfiguration config;
	private       boolean           dirty;

	public boolean isEnabled() {
		return enabled;
	}

	private boolean enabled = true; // Whether or not the plugin is enabled at all; interface mode.

	public WorldSettings getWorld(final String world) {
		return worlds.get(world);
	}

	final private Map<String, WorldSettings> worlds = new HashMap<>();

	public Settings() {
		final FastPortals instance = FastPortals.getInstance();
		final Logger      log      = instance.getLogger();

		// Make sure the default configuration is saved.
		instance.saveDefaultConfig();

		config = instance.getConfig();

		if (config.isBoolean("enabled")) {
			enabled = config.getBoolean("enabled", enabled);
		} else {
			set("enabled", enabled);
		}

		final ConfigurationSection worldsSection = config.getConfigurationSection("worlds");

		for (final String world : worldsSection.getKeys(false)) {
			worlds.put(world, new WorldSettings(worldsSection.getConfigurationSection(world)));
		}

		save();
	}

	private void save() {
		if (dirty) {
			FastPortals.getInstance().saveConfig();
			dirty = false;
		}
	}

	private void set(final String path, final Object value) {
		config.set(path, value);
		dirty = true;
	}
}
