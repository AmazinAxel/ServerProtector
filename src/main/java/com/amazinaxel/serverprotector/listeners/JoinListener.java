package com.amazinaxel.serverprotector.listeners;
import org.bukkit.event.EventHandler;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.Listener;
import net.kyori.adventure.text.Component;
import static com.amazinaxel.serverprotector.ServerProtector.plugin;

// This class checks if the player joins while they have unauthorized operator privileges
public class JoinListener implements Listener  {
    @EventHandler
    public void onLoginOpCheck(final PlayerLoginEvent e) {
        final Player p = e.getPlayer();
        if (p.isOp()) {
            final List<String> allowedOperatorList = plugin.getConfig().getStringList("operators");
            if (!allowedOperatorList.contains(p.getName())) {
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Component.text("§lServer§6§lProtector §r§l> §c§lThere was a bug and you were kicked to protect the server. The bug should have resolved itself, so please try rejoining."));
                p.setOp(false);
            }
        }
    }
}
