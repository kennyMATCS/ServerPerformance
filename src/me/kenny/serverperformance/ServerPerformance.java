package me.kenny.serverperformance;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.TimingsCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.CustomTimingsHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;


public class ServerPerformance extends JavaPlugin implements CommandExecutor, Listener {
    @Override
    public void onEnable() {
        ((SimplePluginManager) Bukkit.getPluginManager()).useTimings(true);
        CustomTimingsHandler.reload();

        getCommand("performance").setExecutor(this);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(byteArrayOutputStream);
            PrintStream old = System.out;
            System.setOut(ps);
            CustomTimingsHandler.printTimings(System.out);
            System.out.flush();
            System.setOut(old);

            List<PerformanceValue> performanceValues = new ArrayList<>();
            Set<PerformanceValue> remove = new HashSet<>();
            String[] split = byteArrayOutputStream.toString().split("\\r?\\n");
            long totalTickTime = 0;
            for (int i = 0; i < split.length; i++) {
                String string = split[i];
                if (i == 1) {
                    String lineSplit[] = string.split(" ");
                    for (int j = 0; j < lineSplit.length; j++) {
                        String s = lineSplit[j];
                        if (s.equalsIgnoreCase("Time:"))
                            totalTickTime = Long.valueOf(lineSplit[j + 1]);
                    }
                }

                if (string.startsWith("    ")) {
                    String lineSplit[] = string.split(" ");
                    String stringBefore = "";
                    for (int j = 0; j < lineSplit.length; j++) {
                        String s = lineSplit[j];
                        if (s.equalsIgnoreCase("**"))
                            break;
                        if (!s.equalsIgnoreCase("Time:")) {
                            if (i == 0)
                                stringBefore = stringBefore + s;
                            else
                                stringBefore = stringBefore + " " + s;
                        } else {
                            PerformanceValue value = null;
                            stringBefore = stringBefore.trim();
                            if (stringBefore.startsWith("Task:") || stringBefore.startsWith("Plugin:")) {
                                stringBefore = stringBefore.replace("Task:", "Plugin -");
                                stringBefore = stringBefore.replace("Plugin:", "Plugin -");
                                String[] stringBeforeSplit = stringBefore.split(" ");
                                stringBefore = stringBeforeSplit[0] + " " + stringBeforeSplit[1] + " " + stringBeforeSplit[2] + " " + stringBeforeSplit[3];
                                value = new PerformanceValue(stringBefore, Long.valueOf(lineSplit[j + 1]), Material.REDSTONE);
                                performanceValues = add(value, performanceValues);
                            } else {
                                stringBefore.replace("    ", "");
                                value = new PerformanceValue(stringBefore, Long.valueOf(lineSplit[j + 1]), Material.NAME_TAG);
                                performanceValues = add(value, performanceValues);
                            }
                        }
                    }
                }
            }

            for (PerformanceValue value : remove) {
                performanceValues.remove(value);
            }

            new ServerPerfomanceGUI(player, performanceValues, totalTickTime);
        }
        return true;
    }

    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().toLowerCase().startsWith("/timings")) {
            event.getPlayer().sendMessage(ChatColor.RED + "Timings are disabled when using Server Performance.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryMoveEvent(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase("Server Performance"))
            event.setCancelled(true);
    }

    public List<PerformanceValue> add(PerformanceValue value, List<PerformanceValue> values) {
        List<PerformanceValue> newValues = new ArrayList<>();
        PerformanceValue remove = null;
        newValues.addAll(values);
        long ticks = 0;
        for (PerformanceValue v : values) {
            if (value.getName().equalsIgnoreCase(v.getName())) {
                remove = v;
                ticks = v.getTicks();
            }
        }

        if (remove != null)
            newValues.remove(remove);

        value.setTicks(value.getTicks() + ticks);
        newValues.add(value);
        return newValues;
    }
}

