package com.amazinaxel.serverprotector.listeners;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerCommandEvent;
import java.util.List;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.event.Listener;
import net.kyori.adventure.text.Component;
import static com.amazinaxel.serverprotector.ServerProtector.plugin;

// This class listens for when players receive op, and runs many checks to make sure they don't have unauthorized operator privileges.
public class OpListener implements Listener {
    private BukkitTask opChecker;
    public static List<String> allowedOperators;

    // Check if the console tries giving OP to another player that isn't allowed to have it
    @EventHandler
    public void onOPServer(final ServerCommandEvent e) {
        if ((e.getCommand().toLowerCase().startsWith("op ") || e.getCommand().toLowerCase().startsWith("minecraft:op ")) && this.isForbiddenOperator(e.getCommand())) {
            e.getSender().sendMessage("§lServer§6§lProtector §r§l> §cYou can't op this player because they aren't in the operators list.");
            e.setCancelled(true);
        }
    }

    // Check if a player tries giving OP to someone that isn't allowed to have it
    @EventHandler
    public void onOpPlayer(final PlayerCommandPreprocessEvent e) {
        if ((e.getPlayer().isOp() || e.getPlayer().hasPermission("*")) && (e.getMessage().toLowerCase().startsWith("/op ") || e.getMessage().toLowerCase().startsWith("/minecraft:op ")) && this.isForbiddenOperator(e.getMessage())) {
            e.getPlayer().sendMessage("§lServer§6§lProtector §r§l> §cYou can't op this player because they aren't in the operators list.");
            e.setCancelled(true);
        }
    }

    // This repeats every 15 seconds to make sure that players that somehow got OP will have it removed.
    @EventHandler
    public void onServerStart(final ServerLoadEvent e) {
        if (this.opChecker != null) {
            return;
        }
        this.opChecker = new BukkitRunnable() {
            public void run() {
                Bukkit.getOperators().forEach(offlinePlayer -> {
                    if (!OpListener.allowedOperators.contains(offlinePlayer.getName())) {
                        offlinePlayer.setOp(false);
                        if (offlinePlayer.isOnline() && offlinePlayer.getPlayer() != null) {
                            offlinePlayer.getPlayer().kick(Component.text("§lServer§6§lProtector §r§l> §c§lYou aren't allowed to be an operator! Please try rejoining."));
                        }
                    }
                });
            }
        }.runTaskTimer(plugin, 300L, 40L);
    }

    // This function simply checks if the player is allowed to have op.
    private boolean isForbiddenOperator(String command) {
        if (command.charAt(0) == '/') {
            command = command.replaceFirst("/", "");
        }
        for (final String allowedOperator: OpListener.allowedOperators) {
            if (command.equalsIgnoreCase("op " + allowedOperator) || command.equalsIgnoreCase("minecraft:op " + allowedOperator)) {
                return false;
            }
        }
        return true;
    }
    static {
        OpListener.allowedOperators = plugin.getConfig().getStringList("operators");
    }
}
