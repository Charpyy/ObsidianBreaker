package com.creeperevents.oggehej.obsidianbreaker.obsidianbreaker.nms;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * General utils commonly used when playing with reflections
 * @author Oskar
 */
public class ReflectionUtilsLight {
	private static HashMap<String, Class<?>> nmsClasses = new HashMap<String, Class<?>>();

	/**
	 * Get the current server revision.
	 * <p>Example: v1_8_R3
	 */
	public static String getVersion() {
		String name = Bukkit.getServer().getClass().getPackage().getName();
		String version = name.substring(name.lastIndexOf('.') + 1) + ".";
		return version;
	}

	/**
	 * Get the NMS class with the specified name
	 */
	public static Class<?> getNMSClass(String className) {
		Class<?> clazz = null;
		if(nmsClasses.containsKey(className)) {
			clazz = nmsClasses.get(className);
		} else {
			String fullName = "net.minecraft.server." + getVersion() + className;
			try {
				clazz = Class.forName(fullName);
			} catch (Exception e) {
				System.err.println("Could not find NMS class!");
				e.printStackTrace();
			}
			nmsClasses.put(className, clazz);
		}
		return clazz;
	}

	/**
	 * Get the player connection as a generic object
	 */
	public static Object getConnection(Player player) {
		try {
			Object nmsPlayer = getPlayerHandle(player);
			return nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
		} catch(Exception e) {
			System.err.println("Could not fetch player connection!");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get the handle of specified player
	 */
	public static Object getPlayerHandle(Player player) {
		try {
			return player.getClass().getMethod("getHandle").invoke(player);
		} catch (Exception e) {
			System.err.println("Could not get player handle!");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Clear the NMS class cache
	 */
	public static void clearCache() {
		nmsClasses.clear();
	}
}
