package me.rcj0003.clans.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.rcj0003.clans.config.MessageConfiguration;
import me.rcj0003.clans.config.MessageType;
import me.rcj0003.clans.events.ClanInviteEvent;
import me.rcj0003.clans.group.Clan;
import me.rcj0003.clans.group.ClanMember;
import me.rcj0003.clans.group.ClanRole;
import me.rcj0003.clans.group.ClanService;
import me.rcj0003.clans.group.exceptions.ClanDoesNotExistException;
import me.rcj0003.clans.utils.MessageBuilder;
import me.rcj0003.clans.utils.command.CommandUser;
import me.rcj0003.clans.utils.command.arguments.Argument;
import me.rcj0003.clans.utils.command.arguments.ArgumentSubCommand;
import me.rcj0003.clans.utils.command.arguments.PlayerArgument;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;

public class InviteCommand extends ArgumentSubCommand {
	private static final String[] DESCRIPTION = new String[] { "Invites a player to your clan (if you have appropriate permissions to do so)." };
	private static final Argument<?>[] arguments = new Argument<?>[] { new PlayerArgument() };
	private Plugin plugin;
	private ClanService clanService;
	private MessageConfiguration config;
	
	public InviteCommand(Plugin plugin, ClanService clanService, MessageConfiguration config) {
		this.plugin = plugin;
		this.clanService = clanService;
		this.config = config;
	}
	
	public boolean getRequiresPlayer() {
		return true;
	}

	public int getMinimumArguments() {
		return 1;
	}

	public String getName() {
		return "invite";
	}

	public String[] getDescription() {
		return DESCRIPTION;
	}

	public String getUsage() {
		return "[Player]";
	}

	public Argument<?>[] getArgumentTypes() {
		return arguments;
	}

	public void executeVerified(CommandUser user, Object[] values) {
		Clan clan = clanService.getClanForPlayer(user.getPlayer());

		if (clan == null) {
			user.sendFormattedMessage(config.getMessage(MessageType.NOT_IN_CLAN_ERROR));
			return;
		}
		
		ClanMember member = clanService.getClanMember(user.getPlayer());
		
		if (!member.getRole().hasHigherWeightThan(ClanRole.OFFICER)) {
			user.sendFormattedMessage(config.getMessage(MessageType.HIGHER_ROLE_REQUIRED));
			return;
		}
		
		OfflinePlayer player = (OfflinePlayer) values[0];
		
		if (player.getUniqueId() == user.getPlayer().getUniqueId()) {
			user.sendFormattedMessage(config.getMessage(MessageType.CANT_DO_ON_SELF));
			return;
		}
		
		if (clan.isMember(player.getUniqueId())) {
			user.sendFormattedMessage(config.getMessage(MessageType.IN_CLAN_ERROR));
			return;
		}
		
		if (player.isOnline()) {
			Player onlinePlayer = (Player) player;
			
			MessageBuilder messageBuilder = new MessageBuilder();
			MessageBuilder hoverBuilder = new MessageBuilder();
			
			onlinePlayer.spigot().sendMessage(messageBuilder.addText(false, config.getMessage(MessageType.INVITE_RECEIVED, user.getName())).build());
			messageBuilder = new MessageBuilder();
			
			messageBuilder.addText(false, config.getMessage(MessageType.CLICK_TO_ACCEPT, "Join"));
			hoverBuilder.addText(false, config.getMessage(MessageType.INVITE_SENDER, user.getName()));

			onlinePlayer.spigot()
					.sendMessage(new BaseComponent[] { messageBuilder
							.addEvent(new HoverEvent(Action.SHOW_TEXT, new BaseComponent[] { hoverBuilder.build() }))
							.addEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
									"/clans join " + user.getName()))
							.build() });
		}
		
		Bukkit.getPluginManager().callEvent(new ClanInviteEvent(clan, player.getUniqueId()));
		
		new BukkitRunnable() {
			public void run() {
				clan.invite(player.getUniqueId()).update();
			}
		}.runTaskAsynchronously(plugin);
	}
}