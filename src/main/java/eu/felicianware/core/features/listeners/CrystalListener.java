package eu.felicianware.core.features.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CrystalListener implements Listener {

    private final Map<UUID, Integer> crystalSpawnTicks = new HashMap<>();
    private final JavaPlugin plugin;

    public CrystalListener(final JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onCrystalBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (block.getType() == Material.END_CRYSTAL) {
            EnderCrystal crystal = (EnderCrystal) block.getWorld().spawn(block.getLocation(), EnderCrystal.class);
            UUID crystalUUID = crystal.getUniqueId();
            int currentTick = Bukkit.getCurrentTick();

            if (crystalSpawnTicks.containsKey(crystalUUID)) {
                int spawnTick = crystalSpawnTicks.get(crystalUUID);
                int ageInTicks = currentTick - spawnTick;

                if (ageInTicks < 4) {
                    event.setCancelled(true);
                }
            }
        }
    }

    public void trackCrystal(EnderCrystal crystal) {
        UUID crystalUUID = crystal.getUniqueId();
        int spawnTick = Bukkit.getCurrentTick();
        crystalSpawnTicks.put(crystalUUID, spawnTick);

        new BukkitRunnable() {
            @Override
            public void run() {
                crystalSpawnTicks.remove(crystalUUID);
            }
        }.runTaskLater(plugin, 6000L);
    }
}
