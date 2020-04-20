package me.kenny.serverperformance;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerPerformance extends JavaPlugin implements CommandExecutor {
    @Override
    public void onEnable() {
        getCommand("performance").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return true;
    }
}
