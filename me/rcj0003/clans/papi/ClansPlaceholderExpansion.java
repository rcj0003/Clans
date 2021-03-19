package me.rcj0003.clans.papi;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.rcj0003.clans.group.ClanService;

public class ClansPlaceholderExpansion extends PlaceholderExpansion {
	private Plugin plugin;
	private ClanService clanService;
	private Placeholder[] placeholders;
	
	public ClansPlaceholderExpansion(Plugin plugin, ClanService clanService, Placeholder... placeholders) {
		this.plugin = plugin;
		this.clanService = clanService;
		this.placeholders = placeholders;
	}
	
	public String getAuthor() {
		return String.join(", ", plugin.getDescription().getAuthors());
	}

	public String getIdentifier() {
		return plugin.getName().toLowerCase().replace(" ", "");
	}

	public String getVersion() {
		return plugin.getDescription().getVersion();
	}
	
    public boolean canRegister(){
        return plugin.isEnabled();
    }
    
    public String onRequest(OfflinePlayer player, String identifier){
    	for (Placeholder placeholder : placeholders)
    		if (placeholder.doesIdentifierMatch(identifier))
    			return placeholder.getResult(clanService, player, identifier);
    	
        return null;
    }
}