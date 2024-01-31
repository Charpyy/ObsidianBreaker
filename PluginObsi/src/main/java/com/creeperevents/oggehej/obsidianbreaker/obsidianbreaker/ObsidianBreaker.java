package com.creeperevents.oggehej.obsidianbreaker.obsidianbreaker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.creeperevents.oggehej.obsidianbreaker.obsidianbreaker.nms.NMS;
import com.creeperevents.oggehej.obsidianbreaker.obsidianbreaker.nms.Reflection;
import com.creeperevents.oggehej.obsidianbreaker.obsidianbreaker.nms.ReflectionUtilsLight;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.milkbowl.vault.economy.Economy;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.creeperevents.oggehej.obsidianbreaker.obsidianbreaker.nms.NMS;
import com.creeperevents.oggehej.obsidianbreaker.obsidianbreaker.nms.Reflection;
import com.creeperevents.oggehej.obsidianbreaker.obsidianbreaker.nms.ReflectionUtilsLight;

/**
 * The main class of ObsidianBreaker
 * 
 * @author oggehej
 */
public class ObsidianBreaker extends JavaPlugin {
	BlockListener blockListener;
	public WorldGuardPlugin worldGuard;
	private PlayerListener playerListener;
	private StorageHandler storage;
	private NMS nmsHandler;
	private BukkitTask crackRunner;
	BukkitTask regenRunner;
	private Map<Material, Double> itemPrice = new HashMap<>();
	private Economy economy;
	/**
	 * To be run on enable
	 */
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}

		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}

		economy = rsp.getProvider();
		return economy != null;
	}
	public void onEnable() {
		if (!setupEconomy()) {
			getLogger().severe("Vault ou un plugin d'économie compatible n'est pas installé !");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		getCommand("sell").setExecutor(new SellItems(this, economy));
		itemPrice.put(Material.DIAMOND_BLOCK, 540.0);
		itemPrice.put(Material.AIR, 0.0);
		itemPrice.put(Material.STONE, 0.4);
		itemPrice.put(Material.GRASS, 0.5);
		itemPrice.put(Material.DIRT, 0.3);
		itemPrice.put(Material.COBBLESTONE, 0.3);
		itemPrice.put(Material.WOOL, 0.4);
		itemPrice.put(Material.WOOD, 0.6);
		itemPrice.put(Material.SAPLING, 0.2);
		itemPrice.put(Material.BEDROCK, 100.0);
		itemPrice.put(Material.WATER, 0.0);
		itemPrice.put(Material.STATIONARY_WATER, 0.0);
		itemPrice.put(Material.LAVA, 0.0);
		itemPrice.put(Material.STATIONARY_LAVA, 0.0);
		itemPrice.put(Material.SAND, 0.5);
		itemPrice.put(Material.GRAVEL, 0.6);
		itemPrice.put(Material.GOLD_ORE, 10.0);
		itemPrice.put(Material.IRON_ORE, 5.0);
		itemPrice.put(Material.COAL_ORE, 2.0);
		itemPrice.put(Material.LOG, 1.5);
		itemPrice.put(Material.LEAVES, 0.3);
		itemPrice.put(Material.SPONGE, 20.0);
		itemPrice.put(Material.GLASS, 1.0);
		itemPrice.put(Material.LAPIS_ORE, 8.0);
		itemPrice.put(Material.LAPIS_BLOCK, 15.0);
		itemPrice.put(Material.DISPENSER, 8.0);
		itemPrice.put(Material.SANDSTONE, 1.5);
		itemPrice.put(Material.NOTE_BLOCK, 5.0);
		itemPrice.put(Material.PUMPKIN_STEM, 2.0);
		itemPrice.put(Material.MELON_STEM, 2.0);
		itemPrice.put(Material.VINE, 0.3);
		itemPrice.put(Material.FENCE_GATE, 2.0);
		itemPrice.put(Material.BRICK_STAIRS, 5.0);
		itemPrice.put(Material.SMOOTH_STAIRS, 3.0);
		itemPrice.put(Material.MYCEL, 2.0);
		itemPrice.put(Material.WATER_LILY, 0.5);
		itemPrice.put(Material.NETHER_BRICK, 3.0);
		itemPrice.put(Material.NETHER_FENCE, 3.0);
		itemPrice.put(Material.NETHER_BRICK_STAIRS, 5.0);
		itemPrice.put(Material.NETHER_WARTS, 2.0);
		itemPrice.put(Material.ENCHANTMENT_TABLE, 20.0);
		itemPrice.put(Material.BREWING_STAND, 8.0);
		itemPrice.put(Material.CAULDRON, 6.0);
		itemPrice.put(Material.ENDER_PORTAL, 50.0);
		itemPrice.put(Material.ENDER_PORTAL_FRAME, 50.0);
		itemPrice.put(Material.ENDER_STONE, 1.5);
		itemPrice.put(Material.REDSTONE_LAMP_OFF, 5.0);
		itemPrice.put(Material.REDSTONE_LAMP_ON, 5.0);
		itemPrice.put(Material.WOOD_DOUBLE_STEP, 1.0);
		itemPrice.put(Material.WOOD_STEP, 1.0);
		itemPrice.put(Material.COCOA, 0.5);
		itemPrice.put(Material.SANDSTONE_STAIRS, 2.0);
		itemPrice.put(Material.EMERALD_ORE, 15.0);
		itemPrice.put(Material.ENDER_CHEST, 40.0);
		itemPrice.put(Material.TRIPWIRE_HOOK, 1.0);
		itemPrice.put(Material.TRIPWIRE, 0.5);
		itemPrice.put(Material.EMERALD_BLOCK, 360.0);
		itemPrice.put(Material.SPRUCE_WOOD_STAIRS, 3.0);
		itemPrice.put(Material.BIRCH_WOOD_STAIRS, 3.0);
		itemPrice.put(Material.JUNGLE_WOOD_STAIRS, 3.0);
		itemPrice.put(Material.COMMAND, 50.0);
		itemPrice.put(Material.BEACON, 100.0);
		itemPrice.put(Material.COBBLE_WALL, 2.0);
		itemPrice.put(Material.FLOWER_POT, 2.0);
		itemPrice.put(Material.CARROT, 1.0);
		itemPrice.put(Material.POTATO, 1.0);
		itemPrice.put(Material.WOOD_BUTTON, 0.5);
		itemPrice.put(Material.SKULL, 10.0);
		itemPrice.put(Material.ANVIL, 20.0);
		itemPrice.put(Material.TRAPPED_CHEST, 8.0);
		itemPrice.put(Material.GOLD_PLATE, 2.0);
		itemPrice.put(Material.IRON_PLATE, 2.0);
		itemPrice.put(Material.REDSTONE_COMPARATOR_OFF, 8.0);
		itemPrice.put(Material.REDSTONE_COMPARATOR_ON, 8.0);
		itemPrice.put(Material.DAYLIGHT_DETECTOR, 5.0);
		itemPrice.put(Material.REDSTONE_BLOCK, 10.0);
		itemPrice.put(Material.QUARTZ_ORE, 15.0);
		itemPrice.put(Material.HOPPER, 10.0);
		itemPrice.put(Material.QUARTZ_BLOCK, 3.0);
		itemPrice.put(Material.QUARTZ_STAIRS, 5.0);
		itemPrice.put(Material.ACTIVATOR_RAIL, 2.0);
		itemPrice.put(Material.DROPPER, 5.0);
		itemPrice.put(Material.STAINED_CLAY, 2.0);
		itemPrice.put(Material.STAINED_GLASS_PANE, 1.5);
		itemPrice.put(Material.LEAVES_2, 0.3);
		itemPrice.put(Material.LOG_2, 1.5);
		itemPrice.put(Material.ACACIA_STAIRS, 3.0);
		itemPrice.put(Material.DARK_OAK_STAIRS, 3.0);
		itemPrice.put(Material.SLIME_BLOCK, 8.0);
		itemPrice.put(Material.BARRIER, 0.0);
		itemPrice.put(Material.IRON_TRAPDOOR, 5.0);
		itemPrice.put(Material.PRISMARINE, 2.0);
		itemPrice.put(Material.SEA_LANTERN, 5.0);
		itemPrice.put(Material.HAY_BLOCK, 2.0);
		itemPrice.put(Material.CARPET, 0.5);
		itemPrice.put(Material.HARD_CLAY, 2.0);
		itemPrice.put(Material.COAL_BLOCK, 5.0);
		itemPrice.put(Material.PACKED_ICE, 2.0);
		itemPrice.put(Material.DOUBLE_PLANT, 1.0);
		itemPrice.put(Material.STANDING_BANNER, 5.0);
		itemPrice.put(Material.WALL_BANNER, 5.0);
		itemPrice.put(Material.DAYLIGHT_DETECTOR_INVERTED, 5.0);
		itemPrice.put(Material.RED_SANDSTONE, 2.0);
		itemPrice.put(Material.RED_SANDSTONE_STAIRS, 3.0);
		itemPrice.put(Material.DOUBLE_STONE_SLAB2, 2.0);
		itemPrice.put(Material.STONE_SLAB2, 1.0);
		itemPrice.put(Material.SPRUCE_FENCE_GATE, 2.0);
		itemPrice.put(Material.BIRCH_FENCE_GATE, 2.0);
		itemPrice.put(Material.JUNGLE_FENCE_GATE, 2.0);
		itemPrice.put(Material.DARK_OAK_FENCE_GATE, 2.0);
		itemPrice.put(Material.ACACIA_FENCE_GATE, 2.0);
		itemPrice.put(Material.SPRUCE_FENCE, 1.0);
		itemPrice.put(Material.BIRCH_FENCE, 1.0);
		itemPrice.put(Material.JUNGLE_FENCE, 1.0);
		itemPrice.put(Material.DARK_OAK_FENCE, 1.0);
		itemPrice.put(Material.ACACIA_FENCE, 1.0);
		itemPrice.put(Material.SPRUCE_DOOR, 2.0);
		itemPrice.put(Material.BIRCH_DOOR, 2.0);
		itemPrice.put(Material.JUNGLE_DOOR, 2.0);
		itemPrice.put(Material.ACACIA_DOOR, 2.0);
		itemPrice.put(Material.DARK_OAK_DOOR, 2.0);
		itemPrice.put(Material.END_ROD, 3.0);
		itemPrice.put(Material.CHORUS_PLANT, 2.0);
		itemPrice.put(Material.CHORUS_FLOWER, 2.0);
		itemPrice.put(Material.PURPUR_BLOCK, 2.0);
		itemPrice.put(Material.PURPUR_PILLAR, 2.0);
		itemPrice.put(Material.PURPUR_STAIRS, 3.0);
		itemPrice.put(Material.PURPUR_DOUBLE_SLAB, 1.5);
		itemPrice.put(Material.PURPUR_SLAB, 1.0);
		itemPrice.put(Material.END_BRICKS, 2.0);
		itemPrice.put(Material.BEETROOT_BLOCK, 1.0);
		itemPrice.put(Material.GRASS_PATH, 0.5);
		itemPrice.put(Material.END_GATEWAY, 50.0);
		itemPrice.put(Material.COMMAND_REPEATING, 50.0);
		itemPrice.put(Material.COMMAND_CHAIN, 50.0);
		itemPrice.put(Material.FROSTED_ICE, 2.0);
		itemPrice.put(Material.MAGMA, 3.0);
		itemPrice.put(Material.NETHER_WART_BLOCK, 3.0);
		itemPrice.put(Material.RED_NETHER_BRICK, 3.0);
		itemPrice.put(Material.BONE_BLOCK, 2.0);
		itemPrice.put(Material.STRUCTURE_VOID, 0.0);
		itemPrice.put(Material.OBSERVER, 5.0);
		itemPrice.put(Material.WHITE_SHULKER_BOX, 10.0);
		itemPrice.put(Material.ORANGE_SHULKER_BOX, 10.0);
		itemPrice.put(Material.MAGENTA_SHULKER_BOX, 10.0);
		itemPrice.put(Material.LIGHT_BLUE_SHULKER_BOX, 10.0);
		itemPrice.put(Material.YELLOW_SHULKER_BOX, 10.0);
		itemPrice.put(Material.LIME_SHULKER_BOX, 10.0);
		itemPrice.put(Material.PINK_SHULKER_BOX, 10.0);
		itemPrice.put(Material.GRAY_SHULKER_BOX, 10.0);
		itemPrice.put(Material.SILVER_SHULKER_BOX, 10.0);
		itemPrice.put(Material.CYAN_SHULKER_BOX, 10.0);
		itemPrice.put(Material.PURPLE_SHULKER_BOX, 10.0);
		itemPrice.put(Material.BLUE_SHULKER_BOX, 10.0);
		itemPrice.put(Material.BROWN_SHULKER_BOX, 10.0);
		itemPrice.put(Material.GREEN_SHULKER_BOX, 10.0);
		itemPrice.put(Material.RED_SHULKER_BOX, 10.0);
		itemPrice.put(Material.BLACK_SHULKER_BOX, 10.0);
		itemPrice.put(Material.WHITE_GLAZED_TERRACOTTA, 5.0);
		itemPrice.put(Material.ORANGE_GLAZED_TERRACOTTA, 5.0);
		itemPrice.put(Material.MAGENTA_GLAZED_TERRACOTTA, 5.0);
		itemPrice.put(Material.LIGHT_BLUE_GLAZED_TERRACOTTA, 5.0);
		itemPrice.put(Material.YELLOW_GLAZED_TERRACOTTA, 5.0);
		itemPrice.put(Material.LIME_GLAZED_TERRACOTTA, 5.0);
		itemPrice.put(Material.PINK_GLAZED_TERRACOTTA, 5.0);
		itemPrice.put(Material.GRAY_GLAZED_TERRACOTTA, 5.0);
		itemPrice.put(Material.SILVER_GLAZED_TERRACOTTA, 5.0);
		itemPrice.put(Material.CYAN_GLAZED_TERRACOTTA, 5.0);
		itemPrice.put(Material.PURPLE_GLAZED_TERRACOTTA, 5.0);
		itemPrice.put(Material.BLUE_GLAZED_TERRACOTTA, 5.0);
		itemPrice.put(Material.BROWN_GLAZED_TERRACOTTA, 5.0);
		itemPrice.put(Material.GREEN_GLAZED_TERRACOTTA, 5.0);
		itemPrice.put(Material.RED_GLAZED_TERRACOTTA, 5.0);
		itemPrice.put(Material.BLACK_GLAZED_TERRACOTTA, 5.0);
		itemPrice.put(Material.CONCRETE, 2.0);
		itemPrice.put(Material.CONCRETE_POWDER, 1.0);
		itemPrice.put(Material.STRUCTURE_BLOCK, 0.0);
		itemPrice.put(Material.IRON_SPADE, 2.0);
		itemPrice.put(Material.IRON_PICKAXE, 3.0);
		itemPrice.put(Material.IRON_AXE, 3.0);
		itemPrice.put(Material.FLINT_AND_STEEL, 1.0);
		itemPrice.put(Material.APPLE, 1.0);
		itemPrice.put(Material.BOW, 5.0);
		itemPrice.put(Material.ARROW, 0.2);
		itemPrice.put(Material.COAL, 1.0);
		itemPrice.put(Material.DIAMOND, 60.0);
		itemPrice.put(Material.IRON_INGOT, 5.0);
		itemPrice.put(Material.GOLD_INGOT, 8.0);
		itemPrice.put(Material.IRON_SWORD, 4.0);
		itemPrice.put(Material.WOOD_SWORD, 1.0);
		itemPrice.put(Material.WOOD_SPADE, 0.5);
		itemPrice.put(Material.WOOD_PICKAXE, 1.0);
		itemPrice.put(Material.WOOD_AXE, 1.0);
		itemPrice.put(Material.STONE_SWORD, 2.0);
		itemPrice.put(Material.STONE_SPADE, 1.0);
		itemPrice.put(Material.STONE_PICKAXE, 2.0);
		itemPrice.put(Material.STONE_AXE, 2.0);
		itemPrice.put(Material.DIAMOND_SWORD, 30.0);
		itemPrice.put(Material.DIAMOND_SPADE, 10.0);
		itemPrice.put(Material.DIAMOND_PICKAXE, 30.0);
		itemPrice.put(Material.DIAMOND_AXE, 30.0);
		itemPrice.put(Material.STICK, 0.1);
		itemPrice.put(Material.BOWL, 0.3);
		itemPrice.put(Material.MUSHROOM_SOUP, 2.0);
		itemPrice.put(Material.GOLD_SWORD, 10.0);
		itemPrice.put(Material.GOLD_SPADE, 3.0);
		itemPrice.put(Material.GOLD_PICKAXE, 10.0);
		itemPrice.put(Material.GOLD_AXE, 10.0);
		itemPrice.put(Material.STRING, 0.5);
		itemPrice.put(Material.FEATHER, 0.2);
		itemPrice.put(Material.SULPHUR, 0.5);
		itemPrice.put(Material.WOOD_HOE, 1.0);
		itemPrice.put(Material.STONE_HOE, 2.0);
		itemPrice.put(Material.IRON_HOE, 4.0);
		itemPrice.put(Material.DIAMOND_HOE, 15.0);
		itemPrice.put(Material.GOLD_HOE, 10.0);
		itemPrice.put(Material.SEEDS, 0.2);
		itemPrice.put(Material.WHEAT, 0.5);
		itemPrice.put(Material.BREAD, 1.0);
		itemPrice.put(Material.LEATHER_HELMET, 2.0);
		itemPrice.put(Material.LEATHER_CHESTPLATE, 3.0);
		itemPrice.put(Material.LEATHER_LEGGINGS, 2.5);
		itemPrice.put(Material.LEATHER_BOOTS, 1.5);
		itemPrice.put(Material.CHAINMAIL_HELMET, 4.0);
		itemPrice.put(Material.CHAINMAIL_CHESTPLATE, 5.0);
		itemPrice.put(Material.CHAINMAIL_LEGGINGS, 4.5);
		itemPrice.put(Material.CHAINMAIL_BOOTS, 3.5);
		itemPrice.put(Material.IRON_HELMET, 8.0);
		itemPrice.put(Material.IRON_CHESTPLATE, 10.0);
		itemPrice.put(Material.IRON_LEGGINGS, 9.0);
		itemPrice.put(Material.IRON_BOOTS, 7.0);
		itemPrice.put(Material.DIAMOND_HELMET, 25.0);
		itemPrice.put(Material.DIAMOND_CHESTPLATE, 30.0);
		itemPrice.put(Material.DIAMOND_LEGGINGS, 27.0);
		itemPrice.put(Material.DIAMOND_BOOTS, 22.0);
		itemPrice.put(Material.GOLD_HELMET, 15.0);
		itemPrice.put(Material.GOLD_CHESTPLATE, 18.0);
		itemPrice.put(Material.GOLD_LEGGINGS, 16.0);
		itemPrice.put(Material.GOLD_BOOTS, 12.0);
		itemPrice.put(Material.FLINT, 0.5);
		itemPrice.put(Material.PORK, 1.0);
		itemPrice.put(Material.GRILLED_PORK, 2.0);
		itemPrice.put(Material.PAINTING, 5.0);
		itemPrice.put(Material.GOLDEN_APPLE, 50.0);
		itemPrice.put(Material.SIGN, 1.0);
		itemPrice.put(Material.WOOD_DOOR, 1.0);
		itemPrice.put(Material.BUCKET, 2.0);
		itemPrice.put(Material.WATER_BUCKET, 1.0);
		itemPrice.put(Material.LAVA_BUCKET, 1.0);
		itemPrice.put(Material.MINECART, 5.0);
		itemPrice.put(Material.SADDLE, 5.0);
		itemPrice.put(Material.IRON_DOOR, 2.0);
		itemPrice.put(Material.REDSTONE, 1.0);
		itemPrice.put(Material.SNOW_BALL, 0.1);
		itemPrice.put(Material.BOAT, 2.0);
		itemPrice.put(Material.LEATHER, 1.0);
		itemPrice.put(Material.MILK_BUCKET, 2.0);
		itemPrice.put(Material.CLAY_BRICK, 0.5);
		itemPrice.put(Material.CLAY_BALL, 0.2);
		itemPrice.put(Material.SUGAR_CANE, 0.2);
		itemPrice.put(Material.PAPER, 0.2);
		itemPrice.put(Material.BOOK, 1.0);
		itemPrice.put(Material.SLIME_BALL, 0.5);
		itemPrice.put(Material.STORAGE_MINECART, 5.0);
		itemPrice.put(Material.POWERED_MINECART, 5.0);
		itemPrice.put(Material.EGG, 0.1);
		itemPrice.put(Material.COMPASS, 5.0);
		itemPrice.put(Material.FISHING_ROD, 3.0);
		itemPrice.put(Material.WATCH, 5.0);
		itemPrice.put(Material.GLOWSTONE_DUST, 1.0);
		itemPrice.put(Material.RAW_FISH, 0.5);
		itemPrice.put(Material.COOKED_FISH, 1.0);
		itemPrice.put(Material.INK_SACK, 0.2);
		itemPrice.put(Material.BONE, 0.5);
		itemPrice.put(Material.SUGAR, 0.2);
		itemPrice.put(Material.CAKE, 3.0);
		itemPrice.put(Material.BED, 2.0);
		itemPrice.put(Material.DIODE, 5.0);
		itemPrice.put(Material.COOKIE, 0.5);
		itemPrice.put(Material.MAP, 5.0);
		itemPrice.put(Material.SHEARS, 2.0);
		itemPrice.put(Material.MELON, 0.5);
		itemPrice.put(Material.PUMPKIN_SEEDS, 0.2);
		itemPrice.put(Material.MELON_SEEDS, 0.2);
		itemPrice.put(Material.RAW_BEEF, 1.0);
		itemPrice.put(Material.COOKED_BEEF, 2.0);
		itemPrice.put(Material.RAW_CHICKEN, 1.0);
		itemPrice.put(Material.COOKED_CHICKEN, 2.0);
		itemPrice.put(Material.ROTTEN_FLESH, 0.2);
		itemPrice.put(Material.ENDER_PEARL, 5.0);
		itemPrice.put(Material.BLAZE_ROD, 5.0);
		itemPrice.put(Material.GHAST_TEAR, 3.0);
		itemPrice.put(Material.GOLD_NUGGET, 1.0);
		itemPrice.put(Material.NETHER_STALK, 1.0);
		itemPrice.put(Material.POTION, 5.0);
		itemPrice.put(Material.GLASS_BOTTLE, 0.2);
		itemPrice.put(Material.SPIDER_EYE, 1.0);
		itemPrice.put(Material.FERMENTED_SPIDER_EYE, 2.0);
		itemPrice.put(Material.BLAZE_POWDER, 2.0);
		itemPrice.put(Material.MAGMA_CREAM, 2.0);
		itemPrice.put(Material.BREWING_STAND_ITEM, 3.0);
		itemPrice.put(Material.CAULDRON_ITEM, 3.0);
		itemPrice.put(Material.EYE_OF_ENDER, 5.0);
		itemPrice.put(Material.SPECKLED_MELON, 3.0);
		itemPrice.put(Material.MONSTER_EGG, 0.0);
		itemPrice.put(Material.EXP_BOTTLE, 2.0);
		itemPrice.put(Material.FIREBALL, 3.0);
		itemPrice.put(Material.BOOK_AND_QUILL, 3.0);
		itemPrice.put(Material.WRITTEN_BOOK, 5.0);
		itemPrice.put(Material.EMERALD, 45.0);
		itemPrice.put(Material.ITEM_FRAME, 2.0);
		itemPrice.put(Material.FLOWER_POT_ITEM, 2.0);
		itemPrice.put(Material.CARROT_ITEM, 0.5);
		itemPrice.put(Material.POTATO_ITEM, 0.5);
		itemPrice.put(Material.BAKED_POTATO, 1.0);
		itemPrice.put(Material.POISONOUS_POTATO, 0.2);
		itemPrice.put(Material.EMPTY_MAP, 2.0);
		itemPrice.put(Material.GOLDEN_CARROT, 3.0);
		itemPrice.put(Material.SKULL_ITEM, 5.0);
		itemPrice.put(Material.CARROT_STICK, 2.0);
		itemPrice.put(Material.NETHER_STAR, 50.0);
		itemPrice.put(Material.PUMPKIN_PIE, 3.0);
		itemPrice.put(Material.FIREWORK, 2.0);
		itemPrice.put(Material.FIREWORK_CHARGE, 1.0);
		itemPrice.put(Material.ENCHANTED_BOOK, 10.0);
		itemPrice.put(Material.REDSTONE_COMPARATOR, 3.0);
		itemPrice.put(Material.NETHER_BRICK_ITEM, 1.0);
		itemPrice.put(Material.QUARTZ, 2.0);
		itemPrice.put(Material.EXPLOSIVE_MINECART, 5.0);
		itemPrice.put(Material.HOPPER_MINECART, 5.0);
		itemPrice.put(Material.PRISMARINE_SHARD, 2.0);
		itemPrice.put(Material.PRISMARINE_CRYSTALS, 2.0);
		itemPrice.put(Material.RABBIT, 1.0);
		itemPrice.put(Material.COOKED_RABBIT, 2.0);
		itemPrice.put(Material.RABBIT_STEW, 3.0);
		itemPrice.put(Material.RABBIT_FOOT, 1.0);
		itemPrice.put(Material.RABBIT_HIDE, 0.5);
		itemPrice.put(Material.ARMOR_STAND, 2.0);
		itemPrice.put(Material.IRON_BARDING, 5.0);
		itemPrice.put(Material.GOLD_BARDING, 8.0);
		itemPrice.put(Material.DIAMOND_BARDING, 15.0);
		itemPrice.put(Material.LEASH, 2.0);
		itemPrice.put(Material.NAME_TAG, 5.0);
		itemPrice.put(Material.COMMAND_MINECART, 5.0);
		itemPrice.put(Material.MUTTON, 1.0);
		itemPrice.put(Material.COOKED_MUTTON, 2.0);
		itemPrice.put(Material.BANNER, 1.0);
		itemPrice.put(Material.END_CRYSTAL, 50.0);
		itemPrice.put(Material.SPRUCE_DOOR_ITEM, 2.0);
		itemPrice.put(Material.BIRCH_DOOR_ITEM, 2.0);
		itemPrice.put(Material.JUNGLE_DOOR_ITEM, 2.0);
		itemPrice.put(Material.ACACIA_DOOR_ITEM, 2.0);
		itemPrice.put(Material.DARK_OAK_DOOR_ITEM, 2.0);
		itemPrice.put(Material.CHORUS_FRUIT, 1.0);
		itemPrice.put(Material.CHORUS_FRUIT_POPPED, 1.5);
		itemPrice.put(Material.BEETROOT, 0.5);
		itemPrice.put(Material.BEETROOT_SEEDS, 0.2);
		itemPrice.put(Material.BEETROOT_SOUP, 1.5);
		itemPrice.put(Material.DRAGONS_BREATH, 5.0);
		itemPrice.put(Material.SPLASH_POTION, 5.0);
		itemPrice.put(Material.SPECTRAL_ARROW, 1.0);
		itemPrice.put(Material.TIPPED_ARROW, 2.0);
		itemPrice.put(Material.LINGERING_POTION, 3.0);
		itemPrice.put(Material.SHIELD, 5.0);
		itemPrice.put(Material.ELYTRA, 20.0);
		itemPrice.put(Material.BOAT_SPRUCE, 2.0);
		itemPrice.put(Material.BOAT_BIRCH, 2.0);
		itemPrice.put(Material.BOAT_JUNGLE, 2.0);
		itemPrice.put(Material.BOAT_ACACIA, 2.0);
		itemPrice.put(Material.BOAT_DARK_OAK, 2.0);
		itemPrice.put(Material.TOTEM, 50.0);
		itemPrice.put(Material.SHULKER_SHELL, 10.0);
		itemPrice.put(Material.IRON_NUGGET, 0.5);
		itemPrice.put(Material.KNOWLEDGE_BOOK, 0.0);
		itemPrice.put(Material.GOLD_RECORD, 15.0);
		itemPrice.put(Material.GREEN_RECORD, 15.0);
		itemPrice.put(Material.RECORD_3, 15.0);
		itemPrice.put(Material.RECORD_4, 15.0);
		itemPrice.put(Material.RECORD_5, 15.0);
		itemPrice.put(Material.RECORD_6, 15.0);
		itemPrice.put(Material.RECORD_7, 15.0);
		itemPrice.put(Material.RECORD_8, 15.0);
		itemPrice.put(Material.RECORD_9, 15.0);
		itemPrice.put(Material.RECORD_10, 15.0);
		itemPrice.put(Material.RECORD_11, 15.0);
		itemPrice.put(Material.RECORD_12, 15.0);
		blockListener = new BlockListener(this);
		playerListener = new PlayerListener(this);
		storage = new StorageHandler(this);
		worldGuard = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");

		// Initialise NMS class
		setupNMS();

		// Register listeners
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(blockListener, this);
		pm.registerEvents(playerListener, this);

		// Load configuration file
		getConfig().options().copyDefaults(true);
		saveConfig();
		Locale.setupLocale(this);

		// Initialise command
		getCommand("obsidianbreaker").setExecutor(new CommandHandler(this));

		// Schedule runners
		scheduleRegenRunner();
		scheduleCrackCheck();
	}
	public Map<Material, Double> getItemPrices() {
		return itemPrice;
	}

	/**
	 * To be run on disable
	 */
	public void onDisable() {
		storage = null;
		blockListener = null;
		playerListener = null;
		ReflectionUtilsLight.clearCache();
	}

	/**
	 * Get the storage handler of ObsidianBreaker
	 * 
	 * @return Storage handler
	 */
	public StorageHandler getStorage() {
		return storage;
	}

	/**
	 * Return the {@code NMS} handler for the current version
	 * 
	 * @return {@code NMS} handler
	 */
	public NMS getNMS() {
		return this.nmsHandler;
	}

	/**
	 * Set up the NMS functions
	 */
	private void setupNMS() {
		String packageName = this.getServer().getClass().getPackage().getName();
		String version = packageName.substring(packageName.lastIndexOf('.') + 1);

		try {
			final Class<?> clazz = Class.forName(getClass().getPackage().getName() + ".nms." + version);
			if (NMS.class.isAssignableFrom(clazz)) {
				getLogger().info("Using NMS version " + version);
				this.nmsHandler = (NMS) clazz.getConstructor().newInstance();
			}
		} catch (final Exception e) {
			getLogger().info("Could not find NMS version " + version + ". Falling back to reflections. Are you sure you're using the latest version?\n"
					+ "If you are using the latest verion and an error appears later on or block cracks don't work, contact the plugin author. "
					+ "Otherwise everything should function normally.");
			this.nmsHandler = new Reflection();
		}
	}

	/**
	 * Schedule the crack broadcaster
	 */
	void scheduleCrackCheck() {
		if(crackRunner != null) {
			crackRunner.cancel();
			crackRunner = null;
		}

		if(getConfig().getBoolean("BlockCracks.Enabled"))
			crackRunner = new CrackRunnable().runTaskTimerAsynchronously(this, 0, getConfig().getLong("BlockCracks.Interval") * 20);
	}

	/**
	 * Schedule the regen runner
	 */
	void scheduleRegenRunner() {
		if(regenRunner != null) {
			regenRunner.cancel();
			regenRunner = null;
		}

		// Configuration can be set to a negative frequency in order to disable
		long freq = getConfig().getLong("Regen.Frequency") * 20 * 60;
		if(freq > 0) {
			regenRunner = new RegenRunnable().runTaskTimerAsynchronously(this, freq, freq);
		}
	}

	/**
	 * Print a formatted error message
	 * 
	 * @param message Error message
	 * @param e The {@code Exception} object or null if none
	 */
	public void printError(String message, Exception e) {
		String s = "<-- Start -->\n"
				+ "[" + getName() + " v" + getDescription().getVersion() + "] " + message + "\n"
				+ "If you've decided to post this error message, "
				+ "please include everything between the \"Start\" and \"End\" tag PLUS your config.yml and lang.yml\n"
				+ "<-- Stack trace -->\n";
		if(e != null)
			s += ExceptionUtils.getStackTrace(e) + "\n";
		else
			s += "None provided\n";
		s += "<-- End -->";
		Bukkit.getLogger().severe(s);
	}

	@SuppressWarnings("deprecation")
	public static boolean isMatch(Block block, String string) {
		try {
			String[] s = string.split(":");
			if(block.getTypeId() == Integer.parseInt(s[0]) && (s.length == 1 || block.getData() == Byte.parseByte(s[1])))
				return true;
		} catch(Exception e) {}

		return false;
	}

	/**
	 * Will broadcast cracks to all players
	 * 
	 * @author oggehej
	 */
	class CrackRunnable extends BukkitRunnable {
		@Override
		public void run() {
			try {
				for(ConcurrentHashMap<String, BlockStatus> map : getStorage().damage.values()) {
					for(BlockStatus status : map.values()) {
						try {
							Location loc = getStorage().generateLocation(status.getBlockHash());
							if(loc.getChunk().isLoaded())
								getStorage().renderCracks(loc.getBlock());
						} catch(Exception e) {}
					}
				}
			} catch(Exception e) {
				printError("Error occured when broadcasting block cracks. TaskID: " + this.getTaskId(), e);
			}
		}
	}

	/**
	 * Will regenerate blocks when run
	 * 
	 * @author oggehej
	 */
	class RegenRunnable extends BukkitRunnable {
		@Override
		public void run() {
			try {
				for(ConcurrentHashMap<String, BlockStatus> map : storage.damage.values()) {
					for(BlockStatus status : map.values()) {
						if(status.isModified())
							status.setModified(false);
						else {
							status.setDamage(status.getDamage() - (float) getConfig().getDouble("Regen.Amount"));
							if(status.getDamage() < 0.001f)
								getStorage().removeBlockStatus(status);							
						}
					}
				}
			} catch(Exception e) {
				printError("Error occured while trying to regen block (task "+getTaskId()+")", e);
			}
		}
	}
}
