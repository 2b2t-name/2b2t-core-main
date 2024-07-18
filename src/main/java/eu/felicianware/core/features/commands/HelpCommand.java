package eu.felicianware.core.features.commands;

import eu.felicianware.core.managers.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HelpCommand implements CommandExecutor {
    private final ConfigManager configManager;

    public HelpCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            List<String> helpMessages = configManager.getHelpMessages();
            if (helpMessages.isEmpty()) {
                player.sendMessage(Component.text("No help message configured.", NamedTextColor.RED));
            } else {
                for (String line : helpMessages) {
                    Component messageComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(line);
                    player.sendMessage(messageComponent);
                }
            }
        } else {
            sender.sendMessage("This command can only be used by players.");
        }
        return true;
    }
}
