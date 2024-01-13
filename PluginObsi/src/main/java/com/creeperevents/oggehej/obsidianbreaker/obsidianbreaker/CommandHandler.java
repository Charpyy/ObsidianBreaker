package com.creeperevents.oggehej.obsidianbreaker.obsidianbreaker;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Handles the commands
 * 
 * @author oggehej
 */
public class CommandHandler implements CommandExecutor {
	private ObsidianBreaker plugin;
	CommandHandler(ObsidianBreaker instance) {
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(ChatColor.AQUA + " -- [" + plugin.getName() + " v" + plugin.getDescription().getVersion() + "] --");
			if(sender.hasPermission("obsidianbreaker.reload"))
				sender.sendMessage(ChatColor.GOLD + "/" + label + " reload" + ChatColor.WHITE + " - " + Locale.RELOAD_CONFIG);
		} else if(args[0].equalsIgnoreCase("reload"))
			if(sender.hasPermission("obsidianbreaker.reload")) {
				plugin.reloadConfig();
				Locale.setupLocale(plugin);
				plugin.scheduleCrackCheck();
				plugin.scheduleRegenRunner();
				sender.sendMessage(Locale.CONFIG_RELOADED.toString());
			} else
				sender.sendMessage(Locale.NO_PERMISSION.toString());
		else
			sender.sendMessage(Locale.INVALID_ARGUMENTS.toString());
		return true;
	}
}
