package eu.felicianware.core.features.events;

import eu.felicianware.core.features.tablist.PlayerList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class TickOnServerEvent {
    private final Plugin plugin;
    private final PlayerList playerList;

    public TickOnServerEvent(Plugin plugin, PlayerList playerList) {
        this.plugin = plugin;
        this.playerList = playerList;
    }

    public void onServerTick() {
        new BukkitRunnable() {
                @Override
                public void run() {
                    Map<Integer, String> placeholders = new HashMap<>();
                    placeholders.put(0, String.format("%.2f", plugin.getServer().getTPS()[0]));
                    placeholders.put(2, plugin.getServer().getIp());

                    int onlinePlayers = Bukkit.getOnlinePlayers().size();
                    placeholders.put(6, String.valueOf(onlinePlayers));

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        Location playerLocation = player.getLocation();

                        placeholders.put(3, String.valueOf((int) playerLocation.getX()));
                        placeholders.put(4, String.valueOf((int) playerLocation.getY()));
                        placeholders.put(5, String.valueOf((int) playerLocation.getZ()));
                        placeholders.put(7, String.valueOf(player.getPing()));

                        playerList.updateAllPlayerListHeaders(placeholders);}}

        }.runTaskTimer(plugin, 0L, 20L);
    }
}
