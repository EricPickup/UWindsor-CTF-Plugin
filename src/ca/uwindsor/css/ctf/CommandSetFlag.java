package ca.uwindsor.css.ctf;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.block.Banner;
import net.md_5.bungee.api.ChatColor;

public class CommandSetFlag {
	
	public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You cannot set the flag from console!");
			return true;
		}
		
		Player player = (Player) sender;
		
		Team team;
		
		if (args.length == 1) {	//If command entered is in format: /teams setflag
			if (TeamManager.getPlayerTeam(player) == null) {
				sender.sendMessage(ChatColor.RED + "You are not part of a team!");
				return true;
			}
			team = TeamManager.getPlayerTeam(player);
			
		} else if (player.isOp()) {	//If command entered is in format: /teams setflag <teamName> - check if player is op
			
			String teamName = args[1].toUpperCase();
			if (!TeamManager.containsTeam(teamName)) {
				sender.sendMessage(ChatColor.RED + "Invalid team name! Choose from: " + String.join(", ", TeamManager.getTeamNames()));
				return true;
			} else if (TeamManager.getTeam(teamName).getStolenStatus() == true) {
				sender.sendMessage(ChatColor.RED + "You cannot place your flag while it is stolen!");
				return true;
			} else {
				team = TeamManager.getTeam(teamName);
			}
			
		} else {	//Otherwise, player is not op, reject permissions
			player.sendMessage(ChatColor.RED + "You do not have permissions to set the flag of another team!");
			return true;
		}
		
		for (Player teamMember : team.getPlayers()) {
			if (TeamManager.carriers.containsKey(teamMember)) {
				player.sendMessage(ChatColor.RED + "You cannot change your flag location while one of your team members is carrying an enemy flag!");
				return true;
			}
		}
		
		if (team.hasBanner()) {
			if (!enemiesNearBanner(team) || sender.isOp()) {	//Check if there are enemies near flag, overwrite if user is op
				player.sendMessage(ChatColor.GREEN + "Removed previously saved flag at location " + team.getBannerCoordinates());
				team.removeBanner();
			} else {
				player.sendMessage(ChatColor.RED + "You cannot move the flag when enemies are near it!");
				return true;
			}
		}
		
		World w = Bukkit.getServer().getWorlds().get(0);
		Block block = w.getBlockAt(player.getLocation());
		block.setType(team.getBannerMaterial());
		Banner banner = (Banner) block.getState();
		
		banner.setBaseColor(team.getBannerColor());
		banner.update();
		
		sender.sendMessage(ChatColor.GREEN + "You've successfully set the flag location for the " + team.printTeamName() + ChatColor.GREEN + " team at X: " + block.getX() + " Y: " + block.getY() +
			" Z: " + block.getZ());
		team.addBanner(block);
		return true;
	}
	
	public static boolean enemiesNearBanner(Team team) {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			double distance = player.getLocation().distance(team.getBannerSpawn());
			System.out.println("Player's team: " +  TeamManager.getPlayerTeam(player));
			System.out.print("Current team's name: " + team.getName());
			
			if (distance < 150 && (TeamManager.getPlayerTeam(player) == null || !TeamManager.getPlayerTeam(player).equals(team))) {
				return true;
			}
		}
		return false;
	}
}
