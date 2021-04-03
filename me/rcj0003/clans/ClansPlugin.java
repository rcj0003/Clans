package me.rcj0003.clans;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import me.rcj0003.clans.api.ClanFactoryPlugin;
//import me.rcj0003.clans.commands.AdminCommand;
import me.rcj0003.clans.commands.CreateCommand;
import me.rcj0003.clans.commands.DemoteCommand;
import me.rcj0003.clans.commands.DisbandCommand;
import me.rcj0003.clans.commands.InfoCommand;
import me.rcj0003.clans.commands.InviteCommand;
import me.rcj0003.clans.commands.InviteListCommand;
import me.rcj0003.clans.commands.JoinCommand;
import me.rcj0003.clans.commands.KickCommand;
import me.rcj0003.clans.commands.LeaveCommand;
import me.rcj0003.clans.commands.MOTDCommand;
import me.rcj0003.clans.commands.PromoteCommand;
import me.rcj0003.clans.commands.RevokeInviteCommand;
import me.rcj0003.clans.commands.SetNameCommand;
import me.rcj0003.clans.commands.ShopCommand;
import me.rcj0003.clans.commands.TagCommand;
import me.rcj0003.clans.commands.ToggleChatCommand;
import me.rcj0003.clans.commands.TopCommand;
import me.rcj0003.clans.commands.TransferLeaderCommand;
import me.rcj0003.clans.config.MessageConfiguration;
import me.rcj0003.clans.group.ClanFactory;
import me.rcj0003.clans.group.ClanPerk;
import me.rcj0003.clans.group.ClanService;
import me.rcj0003.clans.group.exceptions.ClanFactorySetupException;
import me.rcj0003.clans.listeners.AnniListener;
import me.rcj0003.clans.listeners.ClansListener;
import me.rcj0003.clans.shop.ClanCommandAction;
import me.rcj0003.clans.shop.PerkPurchaseAction;
import me.rcj0003.clans.shop.ShopItem;
import me.rcj0003.clans.shop.SlotIncreaseAction;
//import me.rcj0003.clans.papi.ClanCurrencyPlaceholder;
//import me.rcj0003.clans.papi.ClanHasCurrencyPlaceholder;
//import me.rcj0003.clans.papi.ClansPlaceholderExpansion;
import me.rcj0003.clans.utils.SQLHelper;
import me.rcj0003.clans.utils.Utils.ConfigUtils;
import me.rcj0003.clans.utils.command.SuperCommand;
import me.rcj0003.clans.utils.gui.GuiHandler;

public class ClansPlugin extends JavaPlugin implements ClanFactoryPlugin {
	private static ClansPlugin instance;

	private ClanFactory clanFactory = new DummyClanFactory();
	private ClanService clanService;
	private MessageConfiguration messageConfig;
	private SuperCommand command;
	private GuiHandler guiHandler;
	private Map<Integer, ShopItem> shopItems;

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
			Bukkit.getLogger().log(Level.INFO,
					"Please configure Clans. If you have, verify that you have set 'configured' to true in the configuration file.");
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
		Bukkit.getPluginManager().registerEvents(new AnniListener(clanService, messageConfig), this);

		Bukkit.getLogger().log(Level.INFO, "Setting up shop...");
		guiHandler = new GuiHandler();
		Bukkit.getPluginManager().registerEvents(guiHandler, this);

		shopItems = new HashMap<>();
		setUpXPBoosterCommand(shopItems);
		shopItems.put(2,
				new ShopItem(2, Material.EMERALD, "motd", "&c&lMOTD Perk",
						new String[] { "&7Allows clan to set MOTD." },
						new PerkPurchaseAction(this, clanService, ClanPerk.MOTD, 5000)));
		shopItems.put(3,
				new ShopItem(3, Material.REDSTONE, "tag", "&c&lTag Perk",
						new String[] { "&7Allows clan members to set their tag." },
						new PerkPurchaseAction(this, clanService, ClanPerk.TAG, 5000)));
		shopItems.put(5,
				new ShopItem(5, Material.BEACON, "capacity", "&c&lIncrease Capacity",
						new String[] { "&7Allows you to increase clan capacity.", "&71st Purchase: &c+2 members",
								"&72nd Purchase: &c+2 members", "&73rd Purchase: &c+3 members" },
						new SlotIncreaseAction(this, clanService)));

		Bukkit.getLogger().log(Level.INFO, "Registering commands...");
		command = new SuperCommand("Clans", "clans", "clans.command", messageConfig.constructWrapper("wrapper"));
		getCommand("clans").setExecutor(command);
		command.registerCommands(
				// new AdminCommand(this, clanService, messageConfig, "admin",
				// "clans.commands.admin",
				// messageConfig.constructWrapper("wrapper"), command),
				new CreateCommand(this, clanService, messageConfig),
				new DemoteCommand(this, clanService, messageConfig),
				new DisbandCommand(this, clanService, messageConfig), new InfoCommand(this, clanService, messageConfig),
				new InviteCommand(this, clanService, messageConfig), new InviteListCommand(clanService, messageConfig),
				new JoinCommand(this, clanService, messageConfig), new KickCommand(this, clanService, messageConfig),
				new LeaveCommand(this, clanService, messageConfig), new MOTDCommand(this, clanService, messageConfig),
				new PromoteCommand(this, clanService, messageConfig),
				new RevokeInviteCommand(this, clanService, messageConfig),
				new SetNameCommand(this, clanService, messageConfig), new TagCommand(this, clanService, messageConfig),
				new ToggleChatCommand(clanService, messageConfig), new TopCommand(this, clanService, messageConfig),
				new TransferLeaderCommand(this, clanService, messageConfig),
				new ShopCommand(clanService, messageConfig, guiHandler, shopItems));

		// Bukkit.getLogger().log(Level.INFO, "Setting up PAPI...");
		// new ClansPlaceholderExpansion(this, clanService, new
		// ClanCurrencyPlaceholder(),
		// new ClanHasCurrencyPlaceholder()).register();

		Bukkit.getLogger().log(Level.INFO, "Initialization complete. (Operation took "
				+ (System.currentTimeMillis() - startTime) + " ms to complete.)");
	}

	public void onDisable() {
		long startTime = System.currentTimeMillis();
		Bukkit.getLogger().log(Level.INFO, "Beginning shutdown of plugin...");

		instance = null;

		Bukkit.getLogger().log(Level.INFO, "Shutting down clan service...");
		clanService.shutdown();

		try {
			Bukkit.getLogger().log(Level.INFO, "Shutting down clan factory...");
			clanFactory.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			Bukkit.getLogger().log(Level.SEVERE, "Shutdown of clan factory failed.");
		}

		Bukkit.getLogger().log(Level.INFO, "Cancelling remaining tasks...");
		Bukkit.getScheduler().cancelTasks(this);

		Bukkit.getLogger().log(Level.INFO,
				"Shutdown complete. (Operation took " + (System.currentTimeMillis() - startTime) + " ms to complete.)");
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
		SQLHelper sqlHelper = new SQLHelper(getConfig().getString("database.ip"), getConfig().getInt("database.port"),
				getConfig().getString("database.database"), getConfig().getString("database.username"),
				getConfig().getString("database.password"));

		try (Connection connection = sqlHelper.openConnection()) {
			connection.prepareStatement("SELECT 1").execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return new SQLClanFactory(sqlHelper, initialClanCapacity);
	}

	private void setUpXPBoosterCommand(Map<Integer, ShopItem> items) {
		SQLHelper sqlHelper = new SQLHelper(getConfig().getString("database.ip"), getConfig().getInt("database.port"),
				getConfig().getString("database.database"), getConfig().getString("database.username"),
				getConfig().getString("database.password"));

		try (Connection connection = sqlHelper.openConnection()) {
			connection.prepareStatement("SELECT 1").execute();
		} catch (SQLException e) {
			return;
		}

		items.put(0, new ShopItem(0, Material.NETHER_STAR, "xpbooster", "&c&lClan XP Booster (2x XP, 2 Hours)",
				new String[] { "&7Purchases XP booster for all clan members online.",
						"&7Only leaders and officers can purchase this.", "&c1 Day Cooldown" },
				new ClanCommandAction(this, clanService, sqlHelper, "xpbooster",
						"playerdata %player_name% addglobalbooster XP_MULTIPLIER 7200000 2", 2000, 24 * 60 * 60 * 1000)));
	}
}