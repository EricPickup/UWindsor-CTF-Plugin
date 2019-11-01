package ca.uwindsor.css.ctf;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandGetFlag {

	public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		//========================== ERROR CHECKING ===============================
		//Command format: /teams getFlag
		if (args.length == 1) {
			//Check if sender is console, console cannot belong to a team therefore they should enter a team name
			if (!(sender instanceof Player)) {
				sender.sendMessage("You must enter a team name from console!");
				return true;
			}
		} else {
			if (!Teams.containsTeam(args[1])) {
				sender.sendMessage(ChatColor.RED + "Invalid team name! Choose from: " + String.join(", ", Teams.getTeamNames()));
				return true;
			}
		}
		//=========================================================================
		
		if (sender instanceof Player) {
			if (args.length <= 1) {
				sender.sendMessage(ChatColor.RED + "Invalid arguments: /teams getflag <team>");
			} else if (!Teams.getTeam(args[1].toUpperCase()).hasBanner()) {
				sender.sendMessage(ChatColor.RED + "Team does not have a banner placed.");
			} else {
				String team = args[1].toUpperCase();
				args[1] = team;
				Player player = (Player) sender;
				player.sendMessage(Teams.getTeam(team).printTeamName() + ChatColor.GREEN + 
						" team's flag is located at: " + Teams.getTeam(team).getBannerCoordinates());
			}
		}
		return true;
	}

}
