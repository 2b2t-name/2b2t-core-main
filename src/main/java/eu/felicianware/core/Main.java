package eu.felicianware.core;

import eu.felicianware.core.managers.ConfigManager;
import eu.felicianware.core.util.log;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);

        log.info("Initialising 2b2tcore...");

        if (loadCommands()) {
            log.info("Commands loaded successfully.");
        } else {
            log.severe("Failed to load commands.");
        }

        if (loadListeners()) {
            log.info("Listeners loaded successfully.");
        } else {
            log.severe("Failed to load listeners.");
        }
    }

    @Override
    public void onDisable() {
        log.info("Shutting down 2b2tcore...");
    }

    private boolean loadCommands() {
        boolean isEnabled = configManager.getConfig().getBoolean("commands.enabled");
        if (!isEnabled) {
            log.info("Commands are disabled in config.");
            return false;
        }

        try {
            return true;
        } catch (Exception e) {
            log.severe("Error loading commands: " + e.getMessage());
            return false;
        }
    }

    private boolean loadListeners() {
        try {
            return true;
        } catch (Exception e) {
            log.severe("Error loading listeners: " + e.getMessage());
            return false;
        }
    }
}
