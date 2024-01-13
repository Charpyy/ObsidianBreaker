package com.creeperevents.oggehej.obsidianbreaker.obsidianbreaker.nms;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Reflection implements NMS {
	private boolean failed = false;

	@Override
	public void sendCrackEffect(Location location, int damage) {
		if(!failed) {
			try {
				int x = location.getBlockX(), y = location.getBlockY(), z = location.getBlockZ();
				Object worldHandle = location.getWorld().getClass().getMethod("getHandle").invoke(location.getWorld());
				Object blockPosition = ReflectionUtilsLight.getNMSClass("BlockPosition").getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE).newInstance(x, y, z);
				int dimension = worldHandle.getClass().getField("dimension").getInt(worldHandle);
				Object packet = ReflectionUtilsLight.getNMSClass("PacketPlayOutBlockBreakAnimation")
						.getConstructor(Integer.TYPE, ReflectionUtilsLight.getNMSClass("BlockPosition"), Integer.TYPE)
						.newInstance(location.hashCode(), blockPosition, damage);
				Object serverHandle = Bukkit.getServer().getClass().getMethod("getHandle").invoke(Bukkit.getServer());
				serverHandle.getClass()
						.getMethod("sendPacketNearby", ReflectionUtilsLight.getNMSClass("EntityHuman"), Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Integer.TYPE, ReflectionUtilsLight.getNMSClass("Packet"))
						.invoke(serverHandle, null, x, y, z, 30, dimension, packet);
			} catch(Exception e) {
				failed = true;
				System.err.println("[ObsidianBreaker] Generic reflection failed. Visible block cracks are disabled. Please contact the plugin author.");
			}
		}
	}
}
