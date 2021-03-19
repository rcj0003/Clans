package me.rcj0003.clans.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Utils {
	/**
	 * A set of utilities for dealing with plug-in configuration files.
	 */
	public static class ConfigUtils {
		/**
		 * Automatically fills in missing fields from the configuration.
		 * @param plugin The plug-in to update the configuration for.
		 */
		public static void updateConfiguration(Plugin plugin) {
			plugin.reloadConfig();
			plugin.getConfig().set("version", plugin.getConfig().getDefaults().get("version"));

			Set<String> nodes = plugin.getConfig().getKeys(true);
			
			for (String defaultNode : plugin.getConfig().getDefaults().getKeys(true))
				if (!nodes.contains(defaultNode))
					plugin.getConfig().set(defaultNode, plugin.getConfig().getDefaults().get(defaultNode));

			plugin.saveConfig();
		}

		/**
		 * Loads a configuration of a plug-in.
		 * @param plugin The plug-in you want to load the configuration of.
		 * @param configName The name of the configuration in the data folder for the plug-in.
		 * @return A FileConfiguration object with the configuration data.
		 * @throws FileNotFoundException The exception will be throw if the file does not exist.
		 */
		public static FileConfiguration getConfiguration(Plugin plugin, String configName) throws FileNotFoundException {
			File file = new File(plugin.getDataFolder() + File.separator + configName);
			if (!file.exists())
				throw new FileNotFoundException("The specified configuration file does not exist.");
			return YamlConfiguration.loadConfiguration(file);
		}
		
		/**
		 * @param plugin The plug-in you want to load the configuration of.
		 * @param configName The name of the configuration in the data folder for the plug-in.
		 * @return A boolean indicating if the configuration file exists.
		 */
		public static boolean doesConfigurationExist(Plugin plugin, String configName) {
			File file = new File(plugin.getDataFolder() + File.separator + configName);
			return file.exists();
		}

		/**
		 * Loads a YAML configuration.
		 * @param fileName The path of the configuration you want to load.
		 * @return A FileConfiguration object with the configuration data.
		 * @throws FileNotFoundException The exception will be throw if the file does not exist.
		 */
		public static FileConfiguration getConfiguration(String fileName) throws FileNotFoundException {
			File file = new File(fileName.replace("/", File.separator));
			if (!file.exists())
				throw new FileNotFoundException("The specified configuration file does not exist.");
			return YamlConfiguration.loadConfiguration(file);
		}
	}
	
	/**
	 * A set of utilities for dealing with strings.
	 */
	public static class StringUtils {
		/**
		 * Converts Bukkit-friendly color codes (using & as the color character) to Minecraft color codes.
		 * @param string String to convert color codes for.
		 */
		public static String convertColorCodes(String string) {
			return string.replace((char) 38, (char) 167);
		}

		/**
		 * Converts Bukkit-friendly color codes (using & as the color character) to Minecraft color codes.
		 * @param array Array of strings to convert color codes for.
		 */
		public static String[] convertColorCodes(String[] array) {
			for (int offset = 0; offset < array.length; offset++)
				array[offset] = convertColorCodes(array[offset]);
			return array;
		}
	}
}