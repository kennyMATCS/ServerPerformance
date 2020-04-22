package me.kenny.serverperformance;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sun.misc.Perf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ServerPerfomanceGUI {
    public ServerPerfomanceGUI(Player player, List<PerformanceValue> values, long totalTickTime, boolean spigot) {
        Inventory gui = Bukkit.createInventory(null, 54, "Server Performance");

        values.sort(new PeformanceValueComparator());

        if (spigot)
            gui.setContents(showSpigotInfo(values, totalTickTime));
        else
            gui.setContents(showPluginInfo(values, totalTickTime));

        player.openInventory(gui);
    }

    public ItemStack[] showSpigotInfo(List<PerformanceValue> values, long totalTickTime) {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            PerformanceValue performanceValue = values.get(i);
            if (performanceValue.getSpigot()) {
                ItemStack item = new ItemStack(performanceValue.getMaterial());
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.AQUA + performanceValue.getName());
                meta.setLore(Arrays.asList(ChatColor.YELLOW + "Usage: " + ChatColor.GRAY + round(((double) performanceValue.getTicks() / (double) totalTickTime) * 100, 3) + "%", ChatColor.YELLOW + "Ticks: " + ChatColor.GRAY + performanceValue.getTicks()));
                item.setItemMeta(meta);
                items.add(item);
            }
        }

        ItemStack[] array = new ItemStack[54];
        array = items.toArray(array);
        ItemStack switchPage = new ItemStack(Material.FEATHER);
        ItemMeta meta = switchPage.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Switch to plugin performance.");
        switchPage.setItemMeta(meta);
        array[53] = switchPage;
        return array;
    }

    public ItemStack[] showPluginInfo(List<PerformanceValue> values, long totalTickTime) {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            PerformanceValue performanceValue = values.get(i);
            if (!performanceValue.getSpigot()) {
                ItemStack item = new ItemStack(performanceValue.getMaterial());
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.AQUA + performanceValue.getName());
                meta.setLore(Arrays.asList(ChatColor.YELLOW + "Usage: " + ChatColor.GRAY + round(((double) performanceValue.getTicks() / (double) totalTickTime) * 100, 3) + "%", ChatColor.YELLOW + "Ticks: " + ChatColor.GRAY + performanceValue.getTicks()));
                item.setItemMeta(meta);
                items.add(item);
            }
        }

        ItemStack[] array = new ItemStack[54];
        array = items.toArray(array);
        ItemStack switchPage = new ItemStack(Material.FEATHER);
        ItemMeta meta = switchPage.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Switch to backend performance.");
        switchPage.setItemMeta(meta);
        array[53] = switchPage;
        return array;
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
