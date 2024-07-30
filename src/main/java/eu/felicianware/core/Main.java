package eu.felicianware.core;

import eu.felicianware.core.features.commands.*;
import eu.felicianware.core.features.events.TickOnServerEvent;
import eu.felicianware.core.features.listeners.CrystalListener;
import eu.felicianware.core.features.listeners.JoinLeaveListener;
import eu.felicianware.core.features.tablist.PlayerList;
import eu.felicianware.core.managers.ConfigManager;
import eu.felicianware.core.util.log;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.print.Paper;
import java.lang.reflect.Field;

public final class Main extends JavaPlugin {
    private ConfigManager configManager;
    private PlayerList playerList;
    Paper paper;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        playerList = new PlayerList();

        loadAnnouncementFromConfig();

        TickOnServerEvent tickOnServerEvent = new TickOnServerEvent(this, playerList);

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

        tickOnServerEvent.onServerTick();
    }

    @Override
    public void onDisable() {
        log.info("Shutting down 2b2tcore...");
        playerList.clear();
        saveAnnouncementToConfig();
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
            this.getCommand("2bcore-reload").setExecutor(new ReloadCommand(configManager, playerList, this));
            this.getCommand("annoc").setExecutor(new AnnocCommand(playerList));
            this.getCommand("restart").setExecutor(new RestartCommand(this));

            return true;
        } catch (Exception e) {
            log.severe("Error loading commands: " + e.getMessage());
            return false;
        }
    }

    private boolean loadListeners() {
        try {
            this.getServer().getPluginManager().registerEvents(new CrystalListener(this, configManager), this);
            this.getServer().getPluginManager().registerEvents(new JoinLeaveListener(this), this);
            return true;
        } catch (Exception e) {
            log.severe("Error loading listeners: " + e.getMessage());
            return false;
        }
    }

    private void loadAnnouncementFromConfig() {
        FileConfiguration config = this.getConfig();
        boolean annocbool = config.getBoolean("announcement.enabled", false);
        String annoc = config.getString("announcement.message", "");
        playerList.setAnnouncement(annocbool, annoc);
    }

    private void saveAnnouncementToConfig() {
        FileConfiguration config = this.getConfig();
        config.set("announcement.enabled", playerList.isAnnouncementEnabled());
        config.set("announcement.message", playerList.getAnnouncementMessage());
        this.saveConfig();
    }

}
