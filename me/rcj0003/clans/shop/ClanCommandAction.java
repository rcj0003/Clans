package me.rcj0003.clans.shop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.rcj0003.clans.group.Clan;
import me.rcj0003.clans.group.ClanMember;
import me.rcj0003.clans.group.ClanService;
import me.rcj0003.clans.utils.SQLHelper;

public class ClanCommandAction implements PurchaseAction {
	private Map<UUID, Long> expirations = new HashMap<>();
	private Plugin plugin;
	private ClanService clanService;
	private SQLHelper sqlHelper;
	private String internalName, command;
	private int price;
	private long cooldownTime;
	
	public ClanCommandAction(Plugin plugin, ClanService clanService, SQLHelper sqlHelper, String internalName, String command, int price, long cooldownTime) {
		this.plugin = plugin;
		this.clanService = clanService;
		this.sqlHelper = sqlHelper;
		this.internalName = internalName;
		this.command = command;
		this.price = price;
		this.cooldownTime = cooldownTime;
		
		try (Connection connection = sqlHelper.openConnection()) {
			PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `command_purchases` (id CHAR(36), internal_name VARCHAR(64), expiration BIGINT);");
			statement.executeUpdate();
			statement.close();
			
			statement = connection.prepareStatement("SELECT id, expiration FROM `command_purchases` WHERE internal_name=?;");
			statement.setString(1, internalName);
			ResultSet results = statement.executeQuery();
			
			while (results.next())
				expirations.put(UUID.fromString(results.getString("id")), results.getLong("expiration"));
			
			results.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean canPurchase(Player player) {
		ClanMember member = clanService.getClanMember(player);
		
		if (member.getClanId() == null)
			return false;
		
		if (expirations.containsKey(member.getClanId())) {
			if (expirations.get(member.getClanId()) > System.currentTimeMillis())
				return false;
			
			expirations.remove(member.getClanId());
			
			new BukkitRunnable() {
				public void run() {
					try (Connection connection = sqlHelper.openConnection()) {
						PreparedStatement statement = connection.prepareStatement("DELETE FROM `command_purchases` WHERE id=? AND internal_name=?;");
						statement.setString(1, member.getClanId().toString());
						statement.setString(2, internalName);
						statement.executeUpdate();
						statement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}.runTaskAsynchronously(plugin);
		}
		
		return member.getCurrency() > getPrice(player);
	}

	public void purchase(Player player) {
		ClanMember member = clanService.getClanMember(player);
		expirations.put(member.getClanId(), System.currentTimeMillis() + cooldownTime);
		
		new BukkitRunnable() {
			public void run() {
				try (Connection connection = sqlHelper.openConnection()) {
					PreparedStatement statement = connection.prepareStatement("INSERT INTO `command_purchases` VALUES (?, ?, ?);");
					statement.setString(1, member.getClanId().toString());
					statement.setString(2, internalName);
					statement.setLong(3, System.currentTimeMillis() + cooldownTime);
					statement.executeUpdate();
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				member.modifyCurrency(-getPrice(player)).update();
			}
		}.runTaskAsynchronously(plugin);
		
		Clan clan = clanService.getClanForPlayer(player);
		
		for (UUID memberId : clan.getMembers()) {
			OfflinePlayer clanMember = Bukkit.getOfflinePlayer(memberId);
			if (clanMember.isOnline())
				Bukkit.getConsoleSender().sendMessage(command.replace("%player_name%", clanMember.getName()));
		}
	}

	public int getPrice(Player player) {
		return price;
	}
}