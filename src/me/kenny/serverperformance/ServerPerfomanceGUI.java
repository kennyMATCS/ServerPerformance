package me.kenny.serverperformance;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sun.misc.Perf;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ServerPerfomanceGUI {
    public ServerPerfomanceGUI(Player player, List<PerformanceValue> values, long totalTickTime) {
        Inventory gui = Bukkit.createInventory(null, 54, "Server Performance");

        values.sort(new PeformanceValueComparator());

        for (PerformanceValue performanceValue : values) {
            ItemStack item = new ItemStack(performanceValue.getMaterial());
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + performanceValue.getName());
            meta.setLore(Arrays.asList(ChatColor.YELLOW + "Usage: " + ChatColor.GRAY + round(((double) performanceValue.getTicks() / (double) totalTickTime) * 100, 3)+ "%", ChatColor.YELLOW + "Ticks: " + ChatColor.GRAY + performanceValue.getTicks()));
            item.setItemMeta(meta);
            gui.addItem(item);
        }

        player.openInventory(gui);
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
