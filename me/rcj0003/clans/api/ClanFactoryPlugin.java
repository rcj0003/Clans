package me.rcj0003.clans.api;

import org.bukkit.plugin.Plugin;

import me.rcj0003.clans.group.ClanFactory;

public interface ClanFactoryPlugin extends Plugin {
	int getClanFactoryPriority();
	ClanFactory getNewClanFactory(int initialClanCapacity);
}