package eu.felicianware.core.managers;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConfigManager {
    private final JavaPlugin plugin;
    private File configFile;
    @Getter
    private FileConfiguration config;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        createConfig();
    }

    private void createConfig() {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        addDefaults();
    }

    private void addDefaults() {
        config.addDefault("messages.help", List.of("&6------ &3Commands &6------", "&3/help - Shows the commands.", "&3/kill - Kill yourself.", "&3/msg - Message a player.", "&3/ignore - Ignores a player in chat.", "&3/stats - Shows the server stats.", "&6----------------------"));
        config.addDefault("crystal.place_delay_millis", 100);
        config.addDefault("crystal.explosion_delay_ticks", 3);
        config.addDefault("crystal.explosion_power", 6.0);
        config.options().copyDefaults(true);
        saveConfig();
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config to " + configFile + ": " + e.getMessage());
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        plugin.getLogger().info("Configuration reloaded.");
    }

    public long getPlaceDelayMillis() {
        return config.getLong("crystal.place_delay_millis");
    }

    public long getExplosionDelayTicks() {
        return config.getLong("crystal.explosion_delay_ticks");
    }

    public float getExplosionPower() {
        return (float) config.getDouble("crystal.explosion_power");
    }
}
