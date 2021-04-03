package me.rcj0003.clans.group;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.rcj0003.clans.group.exceptions.ClanDataException;
import me.rcj0003.clans.group.exceptions.ClanDoesNotExistException;
import me.rcj0003.clans.group.exceptions.ClanFactorySetupException;

public interface ClanFactory {
	void initalize() throws ClanFactorySetupException;
	void shutdown() throws ClanDataException;
	
	ClanMember getOrCreateClanMemberById(UUID id) throws ClanDataException;
	Clan getClanById(UUID id) throws ClanDoesNotExistException;
	Clan createClan(Player leader, String name) throws ClanDataException;
	
	void updateClanMember(ClanMember member) throws ClanDataException;
	void updateClan(Clan clan) throws ClanDataException;
	
	void deleteClan(UUID id) throws ClanDataException;
	
	List<ClanResults> getTopClans();
}