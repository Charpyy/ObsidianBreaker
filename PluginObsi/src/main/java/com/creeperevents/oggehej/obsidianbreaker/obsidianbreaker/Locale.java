package com.creeperevents.oggehej.obsidianbreaker.obsidianbreaker;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Handles the player messages depending on the lang.yml
 * 
 * @author oggehej
 */
public enum Locale {
	DURABILITY("&6Durability:"),
	DURABILITY_LEFT("&3{0} &6of &3{1}"),
	UNLIMITED("&3Unlimited"),
	UNLIMITED_VOID("&3Unlimited (Void Protection)"),
	NO_PERMISSION("&4No permission!"),
	INVALID_ARGUMENTS("&4Invalid arguments!"),
	CONFIG_RELOADED("&6Config reloaded!"),
	RELOAD_CONFIG("Reload the config");

	private String def;
	private static YamlConfiguration LANG;

	Locale(String def) {
		this.def = def;
	}

	/**
	 * Set the {@code YamlConfiguration} to use
	 * 
	 * @param config The configuration to set
	 */
	static void setFile(YamlConfiguration config) {
		LANG = config;
	}

	/**
	 * @return Formatted {@code String}
	 */
	@Override
	public String toString() {
		try {
			return ChatColor.translateAlternateColorCodes('&', LANG.getString(name()));
		} catch(Exception e) {
			return def;
		}
	}

	/**
	 * Get the default value of the path
	 * 
	 * @return The default value of the path
	 */
	public String getDefault() {
		return this.def;
	}
	
	/**
	 * Load/reload the lang.yml file.
	 */
	static void setupLocale(ObsidianBreaker plugin) {
		File lang = new File(plugin.getDataFolder(), "lang.yml");

		if (!lang.exists())
			try {
				lang.createNewFile();
			} catch(IOException e) {
				plugin.printError("Couldn't create lang.yml!", e);
				return;
			}

		YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);

		for(Locale item : Locale.values())
			if (conf.getString(item.name()) == null)
				conf.set(item.name(), item.getDefault());

		Locale.setFile(conf);

		try {
			conf.save(lang);
		} catch(IOException e) {
			plugin.printError("Couldn't save lang.yml!", e);
		}
	}
}
