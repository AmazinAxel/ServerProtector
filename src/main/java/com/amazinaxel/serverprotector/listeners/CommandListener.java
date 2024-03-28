package com.amazinaxel.serverprotector.listeners;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.Listener;
import static com.amazinaxel.serverprotector.ServerProtector.plugin;

// This class listens for if a player runs an unauthorized command.
public class CommandListener implements Listener {
    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent e) {
        final Player p = e.getPlayer();
        final String command = e.getMessage().substring(1).split(" ", 2)[0];
        if (!p.isOp() && !plugin.getConfig().getStringList("commands").contains(command)) {
            p.sendMessage("Unknown command. Type \"/help\" for help.");
            e.setCancelled(true);
        }
    }
}