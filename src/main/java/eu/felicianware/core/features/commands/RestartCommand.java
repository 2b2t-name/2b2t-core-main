package eu.felicianware.core.features.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class RestartCommand implements CommandExecutor {

    private final Plugin plugin;

    public RestartCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("restart")) {
            if (sender instanceof Player && !sender.hasPermission("server.restart")) {
                sender.sendMessage(Component.text("You do not have permission to execute this command.", NamedTextColor.RED));
                return true;
            }

            // Schedule the restart messages
            scheduleRestartMessages();

            return true;
        }
        return false;
    }

    private void scheduleRestartMessages() {
        sendRestartMessage(300, "Server restarting in 5 minutes...");
        sendRestartMessage(240, "Server restarting in 1 minute...");
        sendRestartMessage(270, "Server restarting in 30 seconds...");
        sendRestartMessage(285, "Server restarting in 15 seconds...");
        sendRestartMessage(290, "Server restarting in 10 seconds...");
        sendRestartMessage(295, "Server restarting in 5 seconds...");
        sendRestartMessage(297, "Server restarting in 3 seconds...");
        sendRestartMessage(298, "Server restarting in 2 seconds...");
        sendRestartMessage(299, "Server restarting in 1 second...");

        // Schedule the actual restart
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.shutdown();
            }
        }.runTaskLater(plugin, 300 * 20L); // 300 seconds = 5 minutes
    }

    private void sendRestartMessage(int secondsBeforeRestart, String message) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getServer().sendMessage(Component.text("[SERVER] " + message, NamedTextColor.GOLD));
            }
        }.runTaskLater(plugin, secondsBeforeRestart * 20L);
    }
}
