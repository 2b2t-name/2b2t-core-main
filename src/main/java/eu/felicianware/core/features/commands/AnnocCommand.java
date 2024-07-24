package eu.felicianware.core.features.commands;

import eu.felicianware.core.features.tablist.PlayerList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AnnocCommand implements CommandExecutor {

    private final PlayerList playerList;

    public AnnocCommand(PlayerList playerList) {
        this.playerList = playerList;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage("Usage: /annoc <true|false> <message>");
            return false;
        }

        boolean annocBool;
        try {
            annocBool = Boolean.parseBoolean(args[0]);
        } catch (Exception e) {
            sender.sendMessage("Invalid boolean value. Use true or false.");
            return false;
        }

        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            messageBuilder.append(args[i]).append(" ");
        }

        String message = messageBuilder.toString().trim();
        playerList.setAnnouncement(annocBool, message);

        sender.sendMessage("Announcement updated: " + message + " (Enabled: " + annocBool + ")");
        return true;
    }
}
