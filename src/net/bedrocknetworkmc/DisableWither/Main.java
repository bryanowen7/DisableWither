package net.bedrocknetworkmc.DisableWither;

import net.bedrocknetworkmc.DisableWither.Commands.DisableWither;
import net.bedrocknetworkmc.DisableWither.Listeners.WitherSpawnListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    public static Main instance;

    public FileConfiguration config = getConfig();
    public File fileConfig;

    @Override
    public void onEnable() {
        registerCommands();
        registerConfig();
        registerEvents();
        instance = this;
    }

    @Override
    public void onDisable() {
    }

    public static Main getInstance() {
        return instance;
    }

    public void registerCommands() {
        getCommand("disablewither").setExecutor(new DisableWither(this));
    }

    public void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new WitherSpawnListener(this), this);
    }

    public void registerConfig() {
        config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();
        fileConfig = new File(getDataFolder(), "config.yml");

    }
}
