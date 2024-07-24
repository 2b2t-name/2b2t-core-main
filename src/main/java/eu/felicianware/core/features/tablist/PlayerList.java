package eu.felicianware.core.features.tablist;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("ALL")
public class PlayerList {

    private String annoc = "";
    private boolean annocbool = false;
    private final Map<Player, Component> headers = new ConcurrentHashMap<>();
    private final Map<Player, Component> footers = new ConcurrentHashMap<>();

    public void setAnnouncement(boolean annocbool, String annoc) {
        this.annocbool = annocbool;
        this.annoc = annoc;
        updateAllPlayerListHeaders();
    }

    public boolean isAnnouncementEnabled() {
        return annocbool;
    }

    public String getAnnouncementMessage() {
        return annoc;
    }

    public void setHeader(Player player, Map<Integer, String> placeholders) {
        if (placeholders == null) {
            placeholders = Map.of();
        }

        StringBuilder headerBuilder = new StringBuilder("\n§7§l2BUILDERS2TOOLS\n");

        if (annocbool) {
            headerBuilder.append("\n§6").append(annoc).append("\n");
        }

        String header = headerBuilder.toString()
                .replaceAll("\\bget.tps\\b", placeholders.getOrDefault(0, ""))
                .replaceAll("\\bget.motd\\b", placeholders.getOrDefault(1, ""))
                .replaceAll("\\bget.ip\\b", placeholders.getOrDefault(2, ""))
                .replaceAll("\\bget.playerxaxes\\b", placeholders.getOrDefault(3, ""))
                .replaceAll("\\bget.playeryaxes\\b", placeholders.getOrDefault(4, ""))
                .replaceAll("\\bget.playerzaxes\\b", placeholders.getOrDefault(5, ""));

        Component headerComponent = Component.text(header);
        headers.put(player, headerComponent);
        player.sendPlayerListHeaderAndFooter(
                headerComponent,
                footers.getOrDefault(player, Component.text(""))
        );
    }

    public void setFooter(Player player, Map<Integer, String> placeholders) {
        if (placeholders == null) {
            placeholders = Map.of();
        }

        String footer = "\n§8" + placeholders.getOrDefault(0, "") + " tps  - " + placeholders.getOrDefault(6, "") + " players online - " + placeholders.getOrDefault(7, "") + " ping\n";

        Component footerComponent = Component.text(footer
                .replaceAll("\\bget.tps\\b", placeholders.getOrDefault(0, ""))
                .replaceAll("\\bget.motd\\b", placeholders.getOrDefault(1, ""))
                .replaceAll("\\bget.ip\\b", placeholders.getOrDefault(2, ""))
                .replaceAll("\\bget.playerxaxes\\b", placeholders.getOrDefault(3, ""))
                .replaceAll("\\bget.playeryaxes\\b", placeholders.getOrDefault(4, ""))
                .replaceAll("\\bget.playerzaxes\\b", placeholders.getOrDefault(5, ""))
        );

        footers.put(player, footerComponent);
        player.sendPlayerListHeaderAndFooter(
                headers.getOrDefault(player, Component.text("")),
                footerComponent
        );
    }

    public void clear() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendPlayerListHeaderAndFooter(Component.text(""), Component.text(""));
        }
        headers.clear();
        footers.clear();
    }

    public void updateAllPlayerListHeaders() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            setHeader(player, null);
            setFooter(player, null);
        }
    }

    public void updateAllPlayerListHeaders(Map<Integer, String> placeholders) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            setHeader(player, placeholders);
            setFooter(player, placeholders);
        }
    }
}
