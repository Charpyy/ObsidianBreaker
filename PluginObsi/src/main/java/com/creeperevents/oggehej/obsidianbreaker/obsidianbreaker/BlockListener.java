package com.creeperevents.oggehej.obsidianbreaker.obsidianbreaker;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Block listener class
 * 
 * @author oggehej
 */
public class BlockListener implements Listener {
	private ObsidianBreaker plugin;
	BlockListener(ObsidianBreaker instance) {
		this.plugin = instance;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityExplode(EntityExplodeEvent event) {
		Location explosionLocation = event.getLocation();
		if (isInSpawnRegion(explosionLocation)) {
			event.setCancelled(true);
			String message = "Event cancelled.";
			Bukkit.broadcastMessage(ChatColor.GREEN + message);
		}
			if(event.getEntity() == null)
				return;

			List<String> worlds = plugin.getConfig().getStringList("DisabledWorlds");
			for(String world : worlds)
				if(world.equalsIgnoreCase(event.getLocation().getWorld().getName()))
					return;

			Iterator<Block> it = event.blockList().iterator();
			while(it.hasNext()) {
				Block block = it.next();
				if(plugin.getStorage().isValidBlock(block))
					it.remove();
			}

			float unalteredRadius = (float) plugin.getConfig().getDouble("BlastRadius");
			int radius = (int) Math.ceil(unalteredRadius);
			Location detonatorLoc = event.getLocation();

			for (int x = -radius; x <= radius; x++)
				for (int y = -radius; y <= radius; y++)
					for (int z = -radius; z <= radius; z++) {
						Location targetLoc = new Location(detonatorLoc.getWorld(), detonatorLoc.getX() + x, detonatorLoc.getY() + y, detonatorLoc.getZ() + z);
						if (detonatorLoc.distance(targetLoc) <= unalteredRadius)
							explodeBlock(targetLoc, detonatorLoc, event.getEntityType());
					}
	}
	private boolean isInSpawnRegion(Location location) {
		WorldGuardPlugin worldGuard = WorldGuardPlugin.inst();
		ApplicableRegionSet regions = worldGuard.getRegionManager(location.getWorld()).getApplicableRegions(location);
		return regions.getRegions().stream().anyMatch(region -> region.getId().equalsIgnoreCase("spawn"));
	}
	@EventHandler( priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent event) {
		StorageHandler storage = plugin.getStorage();
		Block block = event.getBlock();
		BlockStatus status = storage.getBlockStatus(block, false);
		if(status != null) {
			storage.removeBlockStatus(status);
			plugin.getNMS().sendCrackEffect(block.getLocation(), -1);
		}
	}

	/**
	 * Explode a block
	 * 
	 * @param loc {@code Location} of the block
	 * @param source {@code Location} of the explosion source
	 * @param explosive The {@code EntityType} of the explosion cause
	 */
	void explodeBlock(Location loc, Location source, EntityType explosive) {
		if(!loc.getChunk().isLoaded() || (loc.getBlockY() == 0 && plugin.getConfig().getBoolean("VoidProtector")))
			return;
		if (isInSpawnRegion(loc)) {
			return;
		}

		Block block = loc.getWorld().getBlockAt(loc);
		if(plugin.getStorage().isValidBlock(block)) {
			float liquidDivider = (float) plugin.getConfig().getDouble("LiquidMultiplier");

			// No need to loop through everything if we don't have to
			if(liquidDivider != 1 || plugin.getConfig().getBoolean("BedrockBlocking")) {
				new BlockIteratorRunner(block, source, explosive).runTask(plugin);
			} else
				removeBlock(block, false, explosive);
		}
	}

	/**
	 * @param block Block
	 * @param isLiquid If the path from the detonation and block has liquids
	 * @param explosive Explosive type
	 */
	@SuppressWarnings("deprecation")
	private void removeBlock(Block block, boolean isLiquid, EntityType explosive) {
		try {
			float liquidDivider = (float) plugin.getConfig().getDouble("LiquidMultiplier");

			if(isLiquid && liquidDivider <= 0)
				return;

			float rawDamage = explosive == null ? 1 : (float) plugin.getConfig().getDouble("ExplosionSources." + explosive.toString());
			if(plugin.getStorage().addDamage(block, isLiquid ? rawDamage / liquidDivider : rawDamage)) {
				plugin.getNMS().sendCrackEffect(block.getLocation(), -1);

				@SuppressWarnings("unchecked")
				List<String> list = (List<String>) plugin.getConfig().getList("Drops.DontDrop");
				for(Object section : list) {
					if(section instanceof Integer)
						section = Integer.toString((Integer) section);

					String[] s = ((String) section).split(":");
					if(block.getTypeId() == Integer.parseInt(s[0]) && (s.length == 1 || block.getData() == Byte.parseByte(s[1]))) {
						block.setType(Material.AIR);
						return;
					}
				}

				if(new Random().nextInt(100) + 1 >= plugin.getConfig().getInt("Drops.DropChance"))
					block.setType(Material.AIR);
				else
					block.breakNaturally();
			} else
				plugin.getStorage().renderCracks(block);
		} catch(UnknownBlockTypeException e) {}
	}

	/**
	 * Class that will be called asynchronously
	 * because it contains the BlockIterator stuff
	 */
	private class BlockIteratorRunner extends BukkitRunnable {
		private Block block;
		private Location source;
		private EntityType explosive;
		private boolean isLiquid = false;

		private BlockIteratorRunner(Block block, Location source, EntityType explosive) {
			this.block = block;
			this.source = source;
			this.explosive = explosive;
		}

		@Override
		public void run() {
			Location loc = block.getLocation();

			try {
				// BlockIterator it = new BlockIterator(source.getWorld(), source.toVector(), loc.subtract(source).toVector().normalize(), 0, (int) source.distance(loc));
				Iterator<Block> it = BlockIntersector.getIntersectingBlocks(source, loc).iterator();

				while(it.hasNext()) {
					Block b = it.next();
					if(b.isLiquid()) {
						isLiquid = true;
						break;
					} else if(b.getType() == Material.BEDROCK && plugin.getConfig().getBoolean("BedrockBlocking") && it.hasNext()) {
						return;
					}
				}
			} catch(Exception e) {
				if(source.getBlock().isLiquid())
					isLiquid = true;

				plugin.printError("Liquid detection system failed. Fell back to primitive detection.", e);
			}

			new BukkitRunnable() {
				@Override
				public void run() {
					removeBlock(block, isLiquid, explosive);
				}
			}.runTask(plugin);
		}
	}
}
