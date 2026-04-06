package io.github.derec4.sleepAnimation;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Command executor
 */
public class SleepAnimationCommand implements CommandExecutor {
    private final SleepAnimation plugin;

    public SleepAnimationCommand(SleepAnimation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "Usage: /" + label + " reload");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("SleepAnimation.reload")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to reload SleepAnimation.");
                return true;
            }

            // reload config and restart the TimeSkipper with new settings
            ConfigManager.loadConfig(plugin);
            TimeSkipper old = plugin.getTimeSkipper();
            if (old != null) {
                old.clear();
            }
            TimeSkipper ts = new TimeSkipper(plugin, ConfigManager.getSkipSpeed(), 0);
            plugin.setTimeSkipper(ts);
            ts.start();

            sender.sendMessage(ChatColor.GREEN + "SleepAnimation configuration reloaded.");
            return true;
        }

        sender.sendMessage(ChatColor.RED + "Unknown subcommand. Usage: /" + label + " reload");
        return true;
    }
}

