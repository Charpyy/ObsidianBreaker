package com.creeperevents.oggehej.obsidianbreaker.obsidianbreaker;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PacketListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        try {
            if (channel.equals("typeww2")) {
                String info = new String(bytes);
                String[] data = info.split(";");
                Bukkit.broadcastMessage(info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}