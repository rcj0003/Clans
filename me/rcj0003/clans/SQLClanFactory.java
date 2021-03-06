package me.rcj0003.clans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.rcj0003.clans.group.Clan;
import me.rcj0003.clans.group.ClanFactory;
import me.rcj0003.clans.group.ClanMember;
import me.rcj0003.clans.group.ClanPerk;
import me.rcj0003.clans.group.ClanResults;
import me.rcj0003.clans.group.ClanRole;
import me.rcj0003.clans.group.exceptions.ClanDataException;
import me.rcj0003.clans.group.exceptions.ClanDoesNotExistException;
import me.rcj0003.clans.group.exceptions.ClanFactorySetupException;
import me.rcj0003.clans.utils.SQLHelper;

public class SQLClanFactory implements ClanFactory {
	private SQLHelper sqlHelper;
	private int initialCapacity;

	public SQLClanFactory(SQLHelper sqlHelper, int initialCapacity) {
		this.sqlHelper = sqlHelper;
		this.initialCapacity = initialCapacity;
	}

	public void initalize() throws ClanFactorySetupException {
		try (Connection connection = sqlHelper.openConnection()) {
			Bukkit.getLogger().log(Level.INFO, "Initializing clans databases...");

			connection.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `clans_clans` (clan_id CHAR(36) PRIMARY KEY NOT NULL, name VARCHAR(64) NOT NULL DEFAULT 'My Clan', motd VARCHAR(64) NOT NULL DEFAULT 'Default MOTD', capacity INTEGER NOT NULL, stars INTEGER DEFAULT 0 NOT NULL);")
					.executeUpdate();

			connection.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `clans_players` (player_id CHAR(36) PRIMARY KEY NOT NULL,"
							+ "role_name VARCHAR(64) NOT NULL DEFAULT 'NONE', tag VARCHAR(64) NOT NULL DEFAULT '', currency INTEGER DEFAULT 0);")
					.executeUpdate();

			connection.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `clans_relations` (clan_id CHAR(36), player_id CHAR(36) PRIMARY KEY,"
							+ "CONSTRAINT `link_clan_id` FOREIGN KEY (clan_id) REFERENCES clans_clans(clan_id) ON DELETE CASCADE,"
							+ "CONSTRAINT `link_player_id` FOREIGN KEY (player_id) REFERENCES clans_players(player_id) ON DELETE CASCADE);")
					.executeUpdate();

			connection.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `clans_invites` (clan_id CHAR(36) PRIMARY KEY, player_id CHAR(36),"
							+ "	CONSTRAINT `link_clan_id_invites` FOREIGN KEY (clan_id) REFERENCES clans_clans(clan_id) ON DELETE CASCADE);")
					.executeUpdate();

			connection.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `clans_perks` (clan_id CHAR(36) PRIMARY KEY, perk_name VARCHAR(36),"
							+ "	CONSTRAINT `link_clan_id_perks` FOREIGN KEY (clan_id) REFERENCES clans_clans(clan_id) ON DELETE CASCADE);")
					.executeUpdate();

			Bukkit.getLogger().log(Level.INFO, "Purging clans with no members...");

			PreparedStatement statement = connection.prepareStatement(
					"DELETE FROM `clans_clans` WHERE clan_id NOT IN (SELECT DISTINCT clan_id FROM `clans_relations`);");
			Bukkit.getLogger().log(Level.INFO, String.format("Purged %s clans.", statement.executeUpdate()));

			statement.close();
		} catch (SQLException e) {
			throw new ClanFactorySetupException("Failed to setup SQL clan factory.", e);
		}
	}

	public void shutdown() {
		Bukkit.getLogger().log(Level.WARNING, "Shutting down clan factory...");
	}

	private UUID uuidFromString(String id) {
		if (id == null)
			return null;
		return UUID.fromString(id);
	}

	public ClanMember getOrCreateClanMemberById(UUID id) throws ClanDataException {
		try (Connection connection = sqlHelper.openConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					"SELECT clans_players.role_name, clans_players.currency, clans_players.tag, clans_relations.clan_id FROM `clans_players` "
							+ "LEFT JOIN `clans_relations` ON clans_relations.player_id = clans_players.player_id "
							+ "WHERE clans_players.player_id=?");
			statement.setString(1, id.toString());
			ResultSet results = statement.executeQuery();

			if (results.next()) {
				ClanMember member = new ClanMember(this, id, uuidFromString(results.getString("clan_id")),
						ClanRole.valueOf(results.getString("role_name")), results.getInt("currency"),
						results.getString("tag"));

				results.close();
				statement.close();

				return member;
			} else {
				results.close();
				statement.close();

				statement = connection.prepareStatement("INSERT INTO `clans_players` (player_id) VALUES(?);");
				statement.setString(1, id.toString());
				statement.executeUpdate();
				statement.close();

				return new ClanMember(this, id, null, ClanRole.NONE, 0, "");
			}
		} catch (SQLException e) {
			throw new ClanDataException(e);
		}
	}

	public Clan getClanById(UUID id) throws ClanDoesNotExistException {
		try (Connection connection = sqlHelper.openConnection()) {
			PreparedStatement statement = connection
					.prepareStatement("SELECT name, motd, capacity, stars FROM `clans_clans` WHERE clan_id=?;");
			statement.setString(1, id.toString());
			ResultSet results = statement.executeQuery();

			if (results.next()) {
				Set<UUID> invites = new HashSet<>();
				Set<ClanPerk> clanPerks = new HashSet<>();

				Clan clan = new Clan(this, id, results.getString("name"), results.getString("motd"),
						results.getInt("capacity"), results.getInt("stars"), getClanMembers(id), invites, clanPerks);

				results.close();
				statement.close();

				statement = connection.prepareStatement("SELECT perk_name FROM `clans_perks` WHERE clan_id=?;");
				statement.setString(1, id.toString());
				results = statement.executeQuery();
				
				while (results.next())
					clanPerks.add(ClanPerk.valueOf(results.getString("perk_name")));

				results.close();
				statement.close();
				
				statement = connection.prepareStatement("SELECT player_id FROM `clans_invites` WHERE clan_id=?;");
				statement.setString(1, id.toString());
				results = statement.executeQuery();
				
				while (results.next())
					invites.add(UUID.fromString(results.getString("player_id")));

				return clan;
			} else {
				results.close();
				statement.close();

				throw new ClanDoesNotExistException();
			}
		} catch (SQLException e) {
			throw new ClanDoesNotExistException(e);
		}
	}

	private Set<UUID> getClanMembers(UUID clanId) throws ClanDataException {
		Set<UUID> members = new HashSet<>();

		try (Connection connection = sqlHelper.openConnection()) {
			PreparedStatement statement = connection
					.prepareStatement("SELECT player_id FROM `clans_relations` WHERE clan_id=?;");
			statement.setString(1, clanId.toString());
			ResultSet results = statement.executeQuery();

			while (results.next())
				members.add(UUID.fromString(results.getString("player_id")));

			results.close();
			statement.close();
		} catch (SQLException e) {
			throw new ClanDataException(e);
		}

		return members;
	}

	public Clan createClan(Player leader, String name) throws ClanDataException {
		ClanMember member = getOrCreateClanMemberById(leader.getUniqueId());
		UUID id = UUID.randomUUID();

		try (Connection connection = sqlHelper.openConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					"INSERT INTO `clans_clans` (clan_id, name, capacity, stars) VALUES(?, ?, ?, 0); INSERT INTO `clans_relations` VALUES(?, ?); UPDATE `clans_players` SET role_name=? WHERE player_id=?;");
			statement.setString(1, id.toString());
			statement.setString(2, name);
			statement.setInt(3, initialCapacity);
			statement.setString(4, id.toString());
			statement.setString(5, member.getUniqueId().toString());
			statement.setString(6, ClanRole.LEADER.name());
			statement.setString(7, member.getUniqueId().toString());
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			throw new ClanDataException(e);
		}

		Set<UUID> members = new HashSet<>();
		members.add(leader.getUniqueId());

		return new Clan(this, id, name, "Default MOTD", initialCapacity, 0, members, new HashSet<>(), new HashSet<>());
	}

	public void updateClanMember(ClanMember member) throws ClanDataException {
		try (Connection connection = sqlHelper.openConnection()) {
			PreparedStatement statement = connection
					.prepareStatement("UPDATE `clans_players` SET role_name=?, currency=?, tag=? WHERE player_id=?;"
							+ "DELETE FROM `clans_relations` WHERE player_id=?;");
			statement.setString(1, member.getRole().name());
			statement.setInt(2, member.getCurrency());
			statement.setString(3, member.getTag());
			statement.setString(4, member.getUniqueId().toString());
			statement.setString(5, member.getUniqueId().toString());

			statement.executeUpdate();
			statement.close();

			if (member.getClanId() != null) {
				statement = connection.prepareStatement("INSERT INTO `clans_relations` VALUES (?, ?);");
				statement.setString(1, member.getClanId().toString());
				statement.setString(2, member.getUniqueId().toString());

				statement.executeUpdate();
				statement.close();
			}
		} catch (SQLException e) {
			throw new ClanDataException(e);
		}
	}

	public void updateClan(Clan clan) throws ClanDataException {
		boolean isUpdated = false;

		try (Connection connection = sqlHelper.openConnection()) {
			// Update main clan data
			PreparedStatement statement = connection.prepareStatement(
					"UPDATE `clans_clans` SET name=?, motd=?, capacity=?, stars=stars+? WHERE clan_id=?;");
			statement.setString(1, clan.getName());
			statement.setString(2, clan.getMOTD());
			statement.setInt(3, clan.getCapacity());
			statement.setInt(4, clan.getAwardedStars());
			statement.setString(5, clan.getUniqueId().toString());
			statement.executeUpdate();
			statement.close();

			clan.resetAwardedStars();

			// Update clan perks
			statement = connection.prepareStatement("DELETE FROM `clans_perks` WHERE clan_id=?;");
			statement.setString(1, clan.getUniqueId().toString());
			statement.executeUpdate();
			statement.close();

			statement = connection.prepareStatement("INSERT INTO `clans_perks` (clan_id, perk_name) VALUES (?, ?);");

			for (ClanPerk perk : clan.getPerks()) {
				isUpdated = true;
				statement.setString(1, clan.getUniqueId().toString());
				statement.setString(2, perk.name());
				statement.addBatch();
			}

			if (isUpdated)
				statement.executeUpdate();

			isUpdated = false;
			statement.close();

			// Update clan invites
			statement = connection.prepareStatement("DELETE FROM `clans_invites` WHERE clan_id=?;");
			statement.setString(1, clan.getUniqueId().toString());
			statement.executeUpdate();
			statement.close();

			statement = connection.prepareStatement("INSERT INTO `clans_perks` (clan_id, player_id) VALUES (?, ?);");

			for (UUID invitee : clan.getInvited()) {
				statement.setString(1, clan.getUniqueId().toString());
				statement.setString(2, invitee.toString());
				statement.addBatch();
			}

			if (isUpdated)
				statement.executeUpdate();

			statement.close();
		} catch (SQLException e) {
			throw new ClanDataException(e);
		}
	}

	public void deleteClan(UUID id) throws ClanDataException {
		try (Connection connection = sqlHelper.openConnection()) {
			PreparedStatement statement = connection.prepareStatement("DELETE FROM `clans_clans` WHERE clan_id=?;");
			statement.setString(1, id.toString());
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			throw new ClanDataException(e);
		}
	}

	public List<ClanResults> getTopClans() {
		List<ClanResults> topClans = new ArrayList<>();

		try (Connection connection = sqlHelper.openConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					"SELECT clans_relations.clan_id, clans_players.player_id AS leader_id, clans_clans.stars, clans_clans.name from `clans_relations` "
							+ "LEFT JOIN `clans_players` ON clans_relations.player_id = clans_players.player_id "
							+ "LEFT JOIN `clans_clans` ON clans_relations.clan_id = clans_clans.clan_id "
							+ "WHERE clans_players.role_name=\"LEADER\" " + "GROUP BY clans_relations.clan_id "
							+ "ORDER BY clans_clans.stars DESC " + "LIMIT 10");
			ResultSet results = statement.executeQuery();

			while (results.next()) {
				UUID clanId = UUID.fromString(results.getString("clan_id"));
				topClans.add(new ClanResults(results.getString("name"), clanId,
						UUID.fromString(results.getString("leader_id")), results.getInt("stars")));
			}

			results.close();
			statement.close();
		} catch (SQLException e) {
			throw new ClanDataException(e);
		}

		return topClans;
	}
}