package ca.uwindsor.css.ctf;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.md_5.bungee.api.ChatColor;

public class PlayerJoinListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		player.setScoreboard(Main.board);
		
		if (Teams.getPlayerTeam(player) != null) {
			//Re-assigning display/list name since bukkit does not save these
			player.setDisplayName(Teams.getPlayerTeam(player).getColor() + player.getName() + ChatColor.RESET);
			player.setPlayerListName(Teams.getPlayerTeam(player).getColor() + player.getName() + ChatColor.RESET);
		}
		
	}

}
