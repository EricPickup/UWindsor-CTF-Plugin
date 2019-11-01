package ca.uwindsor.css.ctf;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandGetFlag {

	public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		//========================== ERROR CHECKING ===============================
		//Command format: /teams getFlag
		if (args.length == 1 && !(sender instanceof Player)) {
			//Check if sender is console, console cannot belong to a team therefore they should enter a team name
			sender.sendMessage("You must enter a team name from console!");
			return true;
		} else if (args.length == 2 && !TeamManager.containsTeam(args[1])) {
			sender.sendMessage(ChatColor.RED + "Invalid team name! Choose from: " + String.join(", ", TeamManager.getTeamNames()));
			return true;
		}
		//=========================================================================
		String teamName = args[1].toUpperCase();
		if (sender instanceof Player) {
			if (args.length <= 1) {
				sender.sendMessage(ChatColor.RED + "Invalid arguments: /teams getflag <team>");
			} else if (!TeamManager.getTeam(teamName).hasBanner()) {
				sender.sendMessage(ChatColor.RED + "Team does not have a banner placed.");
			} else {
				Player player = (Player) sender;
				Team team = TeamManager.getTeam(teamName);
				player.sendMessage(team.printTeamName() + ChatColor.GREEN + 
						" team's flag is located at: " + team.getBannerCoordinates());
			}
		}
		return true;
	}

}
