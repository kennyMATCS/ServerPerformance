package me.kenny.serverperformance;

import org.bukkit.Material;

public class PerformanceValue {
    private String name;
    private long ticks;
    private Material material;
    private boolean spigot;

    public PerformanceValue(String name, long ticks, Material material, boolean spigot) {
        this.name = name;
        this.ticks = ticks;
        this.material = material;
        this.spigot = spigot;
    }

    public long getTicks() {
        return ticks;
    }

    public void setTicks(long ticks) {
        this.ticks = ticks;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public boolean getSpigot() {
        return spigot;
    }
}
