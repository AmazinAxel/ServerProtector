package com.amazinaxel.serverprotector.listeners;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import java.util.List;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.Listener;
import static com.amazinaxel.serverprotector.ServerProtector.plugin;

// This class hides commands the player doesn't have access to
public class CommandSuggestionListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTab(final PlayerCommandSendEvent event) {
        final Player p = event.getPlayer();
        if (p.isOp()) { return; } // If player is op, don't hide anything from them
        event.getCommands().clear(); // We want to clear all the suggestions first
        for (final String cmd2: plugin.getConfig().getStringList("commands")) { // Then, we can add suggestions
            final List<String> cmdArgs2 = new ArrayList<>(Arrays.asList(cmd2.split(" ")));
            String command2 = cmdArgs2.get(0);
            command2 = command2.replace("%space%", " ");
            event.getCommands().add(command2);
        }
    }
}