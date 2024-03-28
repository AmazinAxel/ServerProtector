package com.amazinaxel.serverprotector;

import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import com.amazinaxel.serverprotector.listeners.OpListener;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;
import org.jetbrains.annotations.NotNull;

import static com.amazinaxel.serverprotector.ServerProtector.plugin;

// This class handles the /serverprotector command
public class SPCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final String[] args) {
        if (args.length == 0 || !args[0].equals("reload")) { // Show help menu if the user isn't reloading
            sender.sendMessage("§lServer§6§lProtector §r§l> §rCreated by §6§lAmazinAxel.");
            sender.sendMessage("§lServer§6§lProtector §r§l> §7/serverprotector help §rShows this help message.");
            sender.sendMessage("§lServer§6§lProtector §r§l> §7/serverprotector reload §rReloads the config file.");
        } else { // User wants to reload the plugin
            rl(sender);
        }
        return true;
    }

    // This function adds tab completion to the serverprotector command
    public List<String> onTabComplete(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String alias, final String[] args) {
        if (args.length == 1) {
            final List<String> commands = new ArrayList<>();
            if (sender.isOp()) {
                commands.add("reload");
                commands.add("help");
            }
            return commands;
        }
        return null;
    }

    // This function reloads the config and plugin
    private static void rl(final CommandSender sender) {
        if (sender.isOp()) {
            plugin.createConfig();
            plugin.reloadConfig();
            OpListener.allowedOperators = plugin.getConfig().getStringList("operators");
            sender.sendMessage("§lServer§6§lProtector §r§l> §aSuccessfully reloaded the config!");
            Bukkit.getOnlinePlayers().forEach(Player::updateCommands);
        }
    }
}