package net.bedrocknetworkmc.DisableWither.Commands;

import net.bedrocknetworkmc.DisableWither.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class DisableWither implements CommandExecutor {

    private Main plugin;
    private String prefixColor;
    private String prefix;
    public DisableWither(Main pl) {
        plugin = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        prefixColor = plugin.config.getString("prefix");
        prefix = ChatColor.translateAlternateColorCodes('&',prefixColor);

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage("");
            sender.sendMessage(ChatColor.GRAY + " --- " + ChatColor.GREEN + "DisableWither v" + plugin.getDescription().getVersion()
                    + " by BryanGamer" + ChatColor.GRAY + " --- ");
            sender.sendMessage("");
            sender.sendMessage(ChatColor.YELLOW + " /disablewither help" + ChatColor.WHITE  + " - "
                    + ChatColor.GRAY + "Displays this message.");
            sender.sendMessage(ChatColor.YELLOW + " /disablewither reload" + ChatColor.WHITE  + " - "
                    + ChatColor.GRAY + "Reloads configuration file.");
            sender.sendMessage(ChatColor.YELLOW + " /disablewither toggle" + ChatColor.WHITE  + " - "
                    + ChatColor.GRAY + "Toggles if wither spawning will be disabled or not.");
            sender.sendMessage(ChatColor.YELLOW + " /disablewither setmessage <message>" + ChatColor.WHITE  + " - "
                    + ChatColor.GRAY + "Set the message to send when a player tries to spawn a wither on a disabled world");
            sender.sendMessage(ChatColor.YELLOW + " /disablewither setradius <radius>" + ChatColor.WHITE  + " - "
                    + ChatColor.GRAY + "Set the radius in blocks to send the message when a player tries to spawn a wither on a disabled world");
            sender.sendMessage(ChatColor.YELLOW + " /disablewither listworlds" + ChatColor.WHITE  + " - "
                    + ChatColor.GRAY + "Displays a list of worlds where the wither spawning is disabled");
            sender.sendMessage(ChatColor.YELLOW + " /disablewither addworld <world>" + ChatColor.WHITE  + " - "
                    + ChatColor.GRAY + "Add a world to the list of worlds where the wither spawning is disabled");
            sender.sendMessage(ChatColor.YELLOW + " /disablewither removeworld <world>" + ChatColor.WHITE  + " - "
                    + ChatColor.GRAY + "Remove a world from the list of worlds where the wither spawning is disabled");
            return false;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("disablewither.reload")) {
                plugin.config = YamlConfiguration.loadConfiguration(plugin.fileConfig);
                sender.sendMessage(prefix + ChatColor.GREEN + "Configuration file reloaded.");
            }
        } else if (args[0].equalsIgnoreCase("toggle")) {
            if (sender.hasPermission("disablewither.toggle")) {
                Boolean status = plugin.config.getBoolean("enable");
                if (status) {
                    sender.sendMessage(prefix + ChatColor.GREEN + "DisableWither plugin successfully disabled.");
                    plugin.config.set("enable", false);
                    plugin.saveConfig();
                } else if (status == false) {
                    sender.sendMessage(prefix + ChatColor.GREEN + "DisableWither plugin successfully enabled.");
                    plugin.config.set("enable", true);
                    plugin.saveConfig();
                } else {
                    sender.sendMessage(prefix + ChatColor.GREEN + "DisableWither plugin successfully disabled.");
                    plugin.config.set("enable", false);
                    plugin.saveConfig();
                }
            }
        } else if (args[0].equalsIgnoreCase("setmessage")) {
            if (sender.hasPermission("disablewither.setmessage")) {
                if (args.length == 1) {
                    sender.sendMessage(prefix + ChatColor.RED + "Please, specify a message.");
                    return false;
                } else if (args.length >= 2) {
                    StringBuilder str = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        str.append(args[i] + " ");
                    }
                    String message = str.toString();
                    plugin.config.set("message", message);
                    plugin.saveConfig();
                    sender.sendMessage(prefix + ChatColor.GREEN + "Message is now: " + ChatColor.translateAlternateColorCodes('&',message));
                }
            } else {
                sender.sendMessage(prefix + ChatColor.RED + "You don't have permission to do that.");
            }
        } else if (args[0].equalsIgnoreCase("setradius")) {
           if (sender.hasPermission("disablewither.setradius")) {
               if (args.length == 1) {
                   sender.sendMessage(prefix + ChatColor.RED + "Please, specify a radius.");
                   return false;
               } else if (args.length == 2) {
                   String radius = args[1];
                   plugin.config.set("radius", radius);
                   plugin.saveConfig();
                   sender.sendMessage(prefix + ChatColor.GREEN + "Radius of message is now " + radius + ".");
                   return true;
               } else if (args.length >= 3) {
                   sender.sendMessage(prefix + ChatColor.RED + "Too many arguments.");
                   return false;
               }
           } else {
               sender.sendMessage(prefix + ChatColor.RED + "You don't have permission to do that.");
           }
        } else if (args[0].equalsIgnoreCase("listworlds")) {
            if (sender.hasPermission("disablewither.listworlds")) {
                if (args.length >= 2) {
                    sender.sendMessage(prefix + ChatColor.RED + "Too many arguments.");
                    return false;
                }
                StringBuilder str = new StringBuilder();
                List<String> configworlds = plugin.config.getStringList("worlds");
                for (String worlds : configworlds) {
                    if (configworlds.size() < 2) {
                        str.append(worlds);
                    } else {
                        str.append(worlds + ", ");

                    }
                }
                sender.sendMessage(prefix + ChatColor.GREEN + "List of worlds where wither spawning is disabled: " + ChatColor.YELLOW + str);
                return true;
            } else {
                sender.sendMessage(prefix + ChatColor.RED + "You don't have permission to do that.");
            }
        } else if (args[0].equalsIgnoreCase("addworld")) {
            if (sender.hasPermission("disablewither.addworld")) {
                if (args.length == 1) {
                    sender.sendMessage(prefix + ChatColor.RED + "Please, specify a world name.");
                    return false;
                } else if (args.length == 2) {
                    List<String> worlds = plugin.config.getStringList("worlds");
                    if (worlds.contains(args[1])) {
                        sender.sendMessage(prefix + ChatColor.RED + "That world is already on the list of worlds where wither spawning is disabled!");
                        return false;
                    } else {
                        worlds.add(args[1]);
                        plugin.config.set("worlds", worlds);
                        plugin.saveConfig();
                        sender.sendMessage(prefix + ChatColor.GREEN + "Added world " + ChatColor.YELLOW + args[1] + ChatColor.GREEN
                                + " to list of worlds where wither spawning is disabled.");
                        return true;
                    }
                } else if (args.length >= 3) {
                    sender.sendMessage(prefix + ChatColor.RED + "Too many arguments.");
                    return false;
                }
            } else {
                sender.sendMessage(prefix + ChatColor.RED + "You don't have permission to do that.");
            }
        } else if (args[0].equalsIgnoreCase("removeworld")) {
            if (sender.hasPermission("disablewither.removeworld")) {
                if (args.length == 1) {
                    sender.sendMessage(prefix + ChatColor.RED + "Please, specify a world name.");
                    return false;
                } else if (args.length == 2) {
                    List<String> worlds = plugin.config.getStringList("worlds");
                    if (worlds.contains(args[1])) {
                        worlds.remove(args[1]);
                        plugin.config.set("worlds", worlds);
                        plugin.saveConfig();
                        sender.sendMessage(prefix + ChatColor.GREEN + "Removed world " + ChatColor.YELLOW + args[1] + ChatColor.GREEN
                                + " from list of worlds where wither spawning is disabled.");
                        return true;
                    } else {
                        sender.sendMessage(prefix + ChatColor.RED + "That world is not on the list of worlds where wither spawning is disabled!");
                        return false;
                    }
                } else if (args.length >= 3) {
                    sender.sendMessage(prefix + ChatColor.RED + "Too many arguments.");
                    return false;
                }
            } else {
                sender.sendMessage(prefix + ChatColor.RED + "You don't have permission to do that.");
            }
        } else {
            sender.sendMessage("");
            sender.sendMessage(ChatColor.GRAY + " --- " + ChatColor.GREEN + "DisableWither v" + plugin.getDescription().getVersion()
                    + " by BryanGamer" + ChatColor.GRAY + " --- ");
            sender.sendMessage("");
            sender.sendMessage(ChatColor.YELLOW + " /disablewither help" + ChatColor.WHITE  + " - "
                    + ChatColor.GRAY + "Displays this message.");
            sender.sendMessage(ChatColor.YELLOW + " /disablewither reload" + ChatColor.WHITE  + " - "
                    + ChatColor.GRAY + "Reloads configuration file.");
            sender.sendMessage(ChatColor.YELLOW + " /disablewither toggle" + ChatColor.WHITE  + " - "
                    + ChatColor.GRAY + "Toggles if wither spawning will be disabled or not.");
            sender.sendMessage(ChatColor.YELLOW + " /disablewither setmessage <message>" + ChatColor.WHITE  + " - "
                    + ChatColor.GRAY + "Set the message to send when a player tries to spawn a wither on a disabled world");
            sender.sendMessage(ChatColor.YELLOW + " /disablewither setradius <radius>" + ChatColor.WHITE  + " - "
                    + ChatColor.GRAY + "Set the radius in blocks to send the message when a player tries to spawn a wither on a disabled world");
            sender.sendMessage(ChatColor.YELLOW + " /disablewither listworlds" + ChatColor.WHITE  + " - "
                    + ChatColor.GRAY + "Displays a list of worlds where the wither spawning is disabled");
            sender.sendMessage(ChatColor.YELLOW + " /disablewither addworld <world>" + ChatColor.WHITE  + " - "
                    + ChatColor.GRAY + "Add a world to the list of worlds where the wither spawning is disabled");
            sender.sendMessage(ChatColor.YELLOW + " /disablewither removeworld <world>" + ChatColor.WHITE  + " - "
                    + ChatColor.GRAY + "Remove a world from the list of worlds where the wither spawning is disabled");
            return false;
        }
        return true;
    }
}
