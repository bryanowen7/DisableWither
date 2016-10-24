package net.bedrocknetworkmc.DisableWither.Listeners;

import net.bedrocknetworkmc.DisableWither.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.List;

public class WitherSpawnListener implements Listener {

    private Main plugin;
    private String prefix;
    public WitherSpawnListener(Main pl) {
        plugin = pl;
    }

    @EventHandler
    public void onWitherSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() == EntityType.WITHER) {

            if (!plugin.config.getBoolean("enable")) {
                return;
            }

            List<String> configworlds = plugin.config.getStringList("worlds");
            prefix = ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("message"));

            for (String worlds : configworlds) {
                if (event.getLocation().getWorld() == Bukkit.getWorld(worlds)) {
                    event.setCancelled(true);
                    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                        int radius = Integer.parseInt(plugin.config.getString("radius"));
                        if (radius < 0) {
                            player.sendMessage(prefix);
                        } else {
                            if (event.getLocation().distance(player.getLocation()) <= radius) {
                                player.sendMessage(prefix);
                            }
                        }
                    }
                }
            }
        }
    }
}
