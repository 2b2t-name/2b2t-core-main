package eu.felicianware.core;

import eu.felicianware.core.features.commands.DiscordCommand;
import eu.felicianware.core.features.commands.HelpCommand;
import eu.felicianware.core.features.commands.KillCommand;
import eu.felicianware.core.features.listeners.CrystalListener;
import eu.felicianware.core.features.listeners.JoinLeaveListener;
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
            // register commands here
            this.getCommand("help").setExecutor(new HelpCommand(configManager));
            this.getCommand("kill").setExecutor(new KillCommand());
            this.getCommand("discord").setExecutor(new DiscordCommand());

            return true;
        } catch (Exception e) {
            log.severe("Error loading commands: " + e.getMessage());
            return false;
        }
    }

    private boolean loadListeners() {
        try {
            // register listeners/events here
            this.getServer().getPluginManager().registerEvents(new CrystalListener(this, configManager), this);
            this.getServer().getPluginManager().registerEvents(new JoinLeaveListener(this), this);
            return true;
        } catch (Exception e) {
            log.severe("Error loading listeners: " + e.getMessage());
            return false;
        }
    }
}
