package com.creeperevents.oggehej.obsidianbreaker.obsidianbreaker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import static org.bukkit.Bukkit.broadcast;

public class SellItems implements CommandExecutor, Listener {

    private final ObsidianBreaker obsidianBreaker;
    private double price;
    private double result;
    public SellItems(ObsidianBreaker obsidianBreaker) {
        this.obsidianBreaker = obsidianBreaker;
        Bukkit.getServer().getPluginManager().registerEvents(this, obsidianBreaker);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cette commande ne peut être utilisée que par un joueur.");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            openMarketInventory(player);
        }
        return true;
    }

    private void openMarketInventory(Player player) {
        Inventory marketInventory = Bukkit.createInventory(player, 54, "Vente d'items");
        for (int i = 45; i <= 53; i++) {
            if (i != 49) {
                ItemStack glassPaneN = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
                ItemMeta glassMeta = glassPaneN.getItemMeta();
                glassMeta.setDisplayName(ChatColor.GRAY + "Put Your Items Here");
                glassPaneN.setItemMeta(glassMeta);
                marketInventory.setItem(i, glassPaneN);
            }
        }

        ItemStack glassPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
        ItemMeta glassMeta = glassPane.getItemMeta();
        glassMeta.setDisplayName(ChatColor.GREEN + "Sell Your Items");
        glassPane.setItemMeta(glassMeta);
        marketInventory.setItem(49, glassPane);

        player.openInventory(marketInventory);
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Vente d'items")) {
            if (event.getRawSlot() == 49) {
                sellItems((Player) event.getWhoClicked(), event.getInventory());
                event.setCancelled(true);
            }
            if (event.getRawSlot() >= 45 && event.getRawSlot() < 53) {
                event.setCancelled(true);
            }
        }
    }

    private void sellItems(Player player, Inventory marketInventory) {
        for (ItemStack itemStack : marketInventory.getContents()) {
            if (itemStack != null && itemStack.getType() != Material.AIR && !isRenamedGlassPane(itemStack)) {
               price = getItemPrice(itemStack.getType()) * itemStack.getAmount();
               result += price;
            }
        }
        player.sendMessage("§8«§bShop§8» §fYou sold all your items for §6" + result + "§6$.");
        result = 0;
        marketInventory.clear();
        player.closeInventory();
    }

    private boolean isRenamedGlassPane(ItemStack itemStack) {
        if (itemStack.hasItemMeta()) {
            ItemMeta meta = itemStack.getItemMeta();
            if (meta.hasDisplayName() && meta.getDisplayName().equals(ChatColor.GREEN + "Sell Your Items")) {
                return true;
            }
            if (meta.hasDisplayName() && meta.getDisplayName().equals(ChatColor.GRAY + "Put Your Items Here")) {
                return true;
            }
        }
        return false;
    }
    private double getItemPrice(Material material) {
        return obsidianBreaker.getItemPrices().getOrDefault(material, 0.0);
    }
}