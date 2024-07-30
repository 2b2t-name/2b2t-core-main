package eu.felicianware.core.features.listeners;

import eu.felicianware.core.managers.ConfigManager;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CrystalListener implements Listener {
    private final Plugin plugin;
    private final Set<UUID> placeCooldowns = new HashSet<>();

    private final long PLACE_DELAY_TICKS;
    private final long EXPLOSION_DELAY_TICKS;
    private final float EXPLOSION_POWER;

    public CrystalListener(Plugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        long PLACE_DELAY_MILLIS = configManager.getPlaceDelayMillis();
        this.PLACE_DELAY_TICKS = PLACE_DELAY_MILLIS / 50;
        this.EXPLOSION_DELAY_TICKS = configManager.getExplosionDelayTicks();
        this.EXPLOSION_POWER = configManager.getExplosionPower();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCrystalPlace(@NotNull PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        final ItemStack interactItem = event.getItem();
        if (interactItem == null || interactItem.getType() != Material.END_CRYSTAL) return;

        final Player player = event.getPlayer();
        final UUID playerUUID = player.getUniqueId();

        if (placeCooldowns.contains(playerUUID)) {
            event.setCancelled(true);
            player.updateInventory();
        } else {
            placeCooldowns.add(playerUUID);
            RegionScheduler regionScheduler = plugin.getServer().getRegionScheduler();
            regionScheduler.runDelayed(plugin, player.getLocation(), handle -> placeCooldowns.remove(playerUUID), PLACE_DELAY_TICKS);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(@NotNull EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof EnderCrystal crystal)) return;
        if (!(event.getDamager() instanceof Player)) return;

        event.setCancelled(true);

        final Location loc = crystal.getLocation();
        final World world = loc.getWorld();
        if (world == null) return;

        RegionScheduler regionScheduler = plugin.getServer().getRegionScheduler();
        regionScheduler.runDelayed(plugin, loc, handle -> {
            if (!crystal.isDead()) {
                crystal.remove();
                world.createExplosion(loc, EXPLOSION_POWER, false, true);
            }
        }, EXPLOSION_DELAY_TICKS);
    }
}