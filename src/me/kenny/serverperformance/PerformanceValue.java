package me.kenny.serverperformance;

import org.bukkit.Material;

public class PerformanceValue {
    private String name;
    private long ticks;
    private Material material;

    public PerformanceValue(String name, long ticks, Material material) {
        this.name = name;
        this.ticks = ticks;
        this.material = material;
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
}
