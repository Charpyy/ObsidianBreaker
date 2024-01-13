package com.creeperevents.oggehej.obsidianbreaker.obsidianbreaker;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Listener for player actions
 * 
 * @author oggehej
 */
public class PlayerListener implements Listener {
	private ObsidianBreaker plugin;
	PlayerListener(ObsidianBreaker instance) {
		this.plugin = instance;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player player = event.getPlayer();
			String[] s = plugin.getConfig().getString("DurabilityChecker").split(":");
			if(player.getItemInHand().getTypeId() == Integer.parseInt(s[0])
					&& (s.length == 1 || player.getItemInHand().getData().getData() == Byte.parseByte(s[1]))
					&& player.hasPermission("obsidianbreaker.test")) {
				try {
					Block block = event.getClickedBlock();
					BlockStatus status = plugin.getStorage().getBlockStatus(block, false);
					float totalDurability;
					float remainingDurability;
					if(status == null) {
						totalDurability = plugin.getStorage().getTotalDurabilityFromConfig(block);
						remainingDurability = totalDurability;
					} else {
						totalDurability = status.getTotalDurability();
						remainingDurability = totalDurability - status.getDamage();
					}

					if(block.getLocation().getBlockY() == 0 && plugin.getConfig().getBoolean("VoidProtector")) {
						player.sendMessage(Locale.DURABILITY + " " + Locale.UNLIMITED_VOID);
					} else if(totalDurability > 0) {
						// Round numbers for fancy output
						DecimalFormat format = new DecimalFormat("##.##");
						DecimalFormatSymbols symbol = new DecimalFormatSymbols();
						symbol.setDecimalSeparator('.');
						format.setDecimalFormatSymbols(symbol);

						String durability = format.format(totalDurability);
						String durabilityLeft = format.format(remainingDurability);
						player.sendMessage(Locale.DURABILITY + " " + Locale.DURABILITY_LEFT.toString().replace("{0}", durabilityLeft).replace("{1}", durability));
					} else
						player.sendMessage(Locale.DURABILITY + " " + Locale.UNLIMITED);
				} catch (UnknownBlockTypeException e) {}
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if(plugin.getConfig().getBoolean("BlockCracks.Enabled")) {
			Location from = event.getFrom();
			Location to = event.getTo();

			if(from.getWorld() == to.getWorld() && from.distance(to) < 15)
				return;

			plugin.new CrackRunnable().runTaskLaterAsynchronously(plugin, 1);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerLogin(PlayerLoginEvent event) {
		if(plugin.getConfig().getBoolean("BlockCracks.Enabled"))
			plugin.new CrackRunnable().runTaskLaterAsynchronously(plugin, 1);
	}
}
