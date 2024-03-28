package com.amazinaxel.serverprotector;
import java.io.File;
import java.util.Objects;
import org.bukkit.plugin.PluginManager;
import com.amazinaxel.serverprotector.listeners.OpListener;
import com.amazinaxel.serverprotector.listeners.CommandListener;
import com.amazinaxel.serverprotector.listeners.CommandSuggestionListener;
import com.amazinaxel.serverprotector.listeners.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerProtector extends JavaPlugin {
    public static ServerProtector plugin;

    // Initialize plugin
    public void onEnable() {
        (com.amazinaxel.serverprotector.ServerProtector.plugin = this).createConfig();
        this.reloadConfig();
        this.initListeners();
        Objects.requireNonNull(this.getCommand("serverprotector")).setExecutor(new SPCommand());
        Objects.requireNonNull(this.getCommand("serverprotector")).setTabCompleter(new SPCommand());
    }

    // Initialize listeners
    private void initListeners() {
        final PluginManager pw = Bukkit.getPluginManager();
        pw.registerEvents(new JoinListener(), this);
        pw.registerEvents(new CommandSuggestionListener(), this);
        pw.registerEvents(new CommandListener(), this);
        pw.registerEvents(new OpListener(), this);
    }

    // Create config file if it isn't already there
    public void createConfig() {
        final File customConfigFile = new File(this.getDataFolder(), "config.yml");
        if (!customConfigFile.exists()) {
            this.saveDefaultConfig();
        }
    }
}