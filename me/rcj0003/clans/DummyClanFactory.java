package me.rcj0003.clans;

import java.util.UUID;

import org.bukkit.entity.Player;

import me.rcj0003.clans.group.Clan;
import me.rcj0003.clans.group.ClanFactory;
import me.rcj0003.clans.group.ClanMember;
import me.rcj0003.clans.group.exceptions.ClanDataException;
import me.rcj0003.clans.group.exceptions.ClanDoesNotExistException;
import me.rcj0003.clans.group.exceptions.ClanFactorySetupException;

public class DummyClanFactory implements ClanFactory {
	public void initalize() throws ClanFactorySetupException {
		throw new ClanFactorySetupException("Dummy clan factory cannot be initialized.");
	}

	public void shutdown() throws ClanDataException {
	}

	public ClanMember getOrCreateClanMemberById(UUID id) throws ClanDataException {
		throw new ClanDataException("Method not implemented.");
	}

	public Clan getClanById(UUID id) throws ClanDoesNotExistException {
		throw new ClanDataException("Method not implemented.");
	}

	public Clan createClan(Player leader, String name) throws ClanDataException {
		throw new ClanDataException("Method not implemented.");
	}

	public void updateClanMember(ClanMember member) throws ClanDataException {
		throw new ClanDataException("Method not implemented.");
	}

	public void updateClan(Clan clan) throws ClanDataException {
		throw new ClanDataException("Method not implemented.");
	}

	public void deleteClan(UUID id) throws ClanDataException {
		throw new ClanDataException("Method not implemented.");
	}
}