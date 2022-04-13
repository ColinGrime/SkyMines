package com.github.colingrime.commands.skymines.subcommands;

import com.github.colingrime.SkyMines;
import com.github.colingrime.commands.skymines.SkyMinesSubCommand;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.skymines.SkyMine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SkyMinesSetHomeSubCommand extends SkyMinesSubCommand {

	private final SkyMines plugin;

	public SkyMinesSetHomeSubCommand(SkyMines plugin) {
		super(plugin);
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, String[] args, SkyMine skyMine) {
		Location location = ((Player) sender).getLocation();
		Vector playerVector = location.toVector();

		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			for (Vector vector : skyMine.getStructure().getParameter()) {
				if (vector.distance(playerVector) <= skyMine.getStructure().getSize().getMaxSide() / 2.0) {
					skyMine.setHome(location);
					Messages.SUCCESS_SETHOME.sendTo(sender);
					return;
				}
			}

			Messages.FAILURE_TOO_FAR_AWAY.sendTo(sender);
		});
	}

	@Override
	public boolean requireSkyMine() {
		return true;
	}

	@Override
	public String getName() {
		return "sethome";
	}

	@Override
	public Messages getUsage() {
		return Messages.USAGE_SKYMINES_SETHOME;
	}

	@Override
	public String getPermission() {
		return "skymines.sethome";
	}
}
