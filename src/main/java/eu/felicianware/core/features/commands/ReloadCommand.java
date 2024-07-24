package eu.felicianware.core.features.commands;

import eu.felicianware.core.managers.ConfigManager;
import eu.felicianware.core.features.tablist.PlayerList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ReloadCommand implements CommandExecutor {
    private final ConfigManager configManager;
    private final Plugin plugin;

    public ReloadCommand(ConfigManager configManager, PlayerList playerList, Plugin plugin) {
        this.configManager = configManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission("2bcore.reload")) {
            configManager.reloadConfig();
            updateTabLists();
            sender.sendMessage("§a2bcore configuration reloaded.");
        } else {
            sender.sendMessage("§cYou do not have permission to use this command.");
        }
        return true;
    }

    private void updateTabLists() {
        Map<Integer, String> placeholders = new HashMap<>();
        placeholders.put(0, String.valueOf((int) plugin.getServer().getTPS()[0])); // TPS
        placeholders.put(1, plugin.getServer().getName());
        placeholders.put(2, plugin.getServer().getIp());

        int onlinePlayers = plugin.getServer().getOnlinePlayers().size();
        placeholders.put(6, String.valueOf(onlinePlayers));

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            int ping = player.getPing();
            placeholders.put(7, String.valueOf(ping));

            placeholders.put(3, "0");
            placeholders.put(4, "0");
            placeholders.put(5, "0");
        }
    }
}
