package eu.felicianware.core.features.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DiscordCommand implements CommandExecutor {
    private static final TextColor GOLD = NamedTextColor.GOLD;
    private static final String DISCORD_LINK = "https://discord.gg/WrkrRYb3mj";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        player.sendMessage(buildDiscordMessage());

        return true;
    }

    private Component buildDiscordMessage() {
        return Component.text()
                .append(Component.text("You can join the discord server here: ", GOLD))
                .append(Component.newline())
                .append(Component.text(DISCORD_LINK, GOLD))
                .build();
    }
}
