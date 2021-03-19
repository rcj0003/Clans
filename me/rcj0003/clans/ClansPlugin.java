package me.rcj0003.clans;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import me.rcj0003.clans.api.ClanFactoryPlugin;
import me.rcj0003.clans.config.MessageConfiguration;
import me.rcj0003.clans.group.ClanFactory;
import me.rcj0003.clans.group.ClanService;
import me.rcj0003.clans.group.exceptions.ClanFactorySetupException;
import me.rcj0003.clans.listeners.ClansListener;
import me.rcj0003.clans.utils.SQLHelper;
import me.rcj0003.clans.utils.Utils.ConfigUtils;

public class ClansPlugin extends JavaPlugin implements ClanFactoryPlugin {
	private static ClansPlugin instance;
	
	private ClanFactory clanFactory = new DummyClanFactory();
	private ClanService clanService;
	private MessageConfiguration messageConfig;
	
	public static ClansPlugin getInstance() {
		return instance;
	}
	
	public void onEnable() {
		instance = this;
		long startTime = System.currentTimeMillis();
		Bukkit.getLogger().log(Level.INFO, "Initializing...");
		Bukkit.getLogger().log(Level.INFO, "Loading configuration files...");
		
		saveDefaultConfig();
		
		if (!getConfig().getBoolean("configured")) {
			Bukkit.getLogger().log(Level.INFO, "Please configure Clans. If you have, verify that you have set 'configured' to true in the configuration file.");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		
		try {
			saveResource("messages.yml", false);
			messageConfig = new MessageConfiguration(ConfigUtils.getConfiguration(this, "messages.yml"));
		} catch (Exception e) {
			e.printStackTrace();
			Bukkit.getLogger().log(Level.SEVERE, "Failed to load configuration files, disabling plugin...");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		
		try {
			Bukkit.getLogger().log(Level.INFO, "Initializing clan factory...");
			clanFactory = loadClanFactory();
			clanFactory.initalize();
		} catch (ClanFactorySetupException e) {
			Bukkit.getLogger().log(Level.INFO, "An error occured while initializing clan factory, disabling plugin...");
			e.printStackTrace();
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		
		Bukkit.getLogger().log(Level.INFO, "Initializing clan service...");
		clanService = new ClanService(this, clanFactory);
		
		Bukkit.getLogger().log(Level.INFO, "Registering event listeners...");
		Bukkit.getPluginManager().registerEvents(new ClansListener(clanService, messageConfig), this);
		
		Bukkit.getLogger().log(Level.INFO, "Registering commands...");
		
		Bukkit.getLogger().log(Level.INFO, "Initialization complete. (Operation took "
				+ (System.currentTimeMillis() - startTime) + " ms to complete.)");
	}
	
	public void onDisable() {
		long startTime = System.currentTimeMillis();
		Bukkit.getLogger().log(Level.INFO, "Beginning shutdown of plugin...");
		
		instance = null;
		
		try {
			Bukkit.getLogger().log(Level.INFO, "Shutting down clan factory...");
			clanFactory.shutdown();	
		}
		catch (Exception e) {
			e.printStackTrace();
			Bukkit.getLogger().log(Level.SEVERE, "Shutdown of clan factory failed.");
		}
		
		Bukkit.getLogger().log(Level.INFO, "Cancelling remaining tasks...");
		Bukkit.getScheduler().cancelTasks(this);
		
		Bukkit.getLogger().log(Level.INFO, "Shutdown complete. (Operation took "
				+ (System.currentTimeMillis() - startTime) + " ms to complete.)");
	}
	
	private ClanFactory loadClanFactory() {
		ClanFactoryPlugin selectedFactoryPlugin = this;
		
		for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
			if (plugin instanceof ClanFactoryPlugin) {
				ClanFactoryPlugin factoryPlugin = (ClanFactoryPlugin) plugin;
				
				if (factoryPlugin.getClanFactoryPriority() > selectedFactoryPlugin.getClanFactoryPriority())
					selectedFactoryPlugin = factoryPlugin;
			}
		}
		
		return selectedFactoryPlugin.getNewClanFactory(getConfig().getInt("initial-clan-capacity", 5));
	}
	
	
	public ClanService getClanService() {
		return clanService;
	}

	public int getClanFactoryPriority() {
		return 0;
	}

	public ClanFactory getNewClanFactory(int initialClanCapacity) {
		SQLHelper sqlHelper = new SQLHelper(getConfig().getString("database.ip"),
				getConfig().getInt("database.port"), getConfig().getString("database.database"),
				getConfig().getString("database.username"), getConfig().getString("database.password"));
		
		try (Connection connection = sqlHelper.openConnection()) {
			connection.prepareStatement("SELECT 1").execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return new SQLClanFactory(sqlHelper, initialClanCapacity);
	}
}