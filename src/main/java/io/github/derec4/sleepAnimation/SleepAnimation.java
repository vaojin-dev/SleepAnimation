package io.github.derec4.sleepAnimation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class SleepAnimation extends JavaPlugin {

    private TimeSkipper timeSkipper;

    @Override
    public void onEnable() {
        ConfigManager.loadConfig(this);
        timeSkipper = new TimeSkipper(this, ConfigManager.getSkipSpeed(), 0);
        timeSkipper.start();
        if (shouldUseModernTimeSkipListener()) {
            getServer().getPluginManager().registerEvents(new SleepListenerModern(this), this);
        } else {
            getServer().getPluginManager().registerEvents(new SleepListener(this), this);
        }

        // register base command 'sleepanimation' and handle subcommands (reload)
        PluginCommand sleepCmd = getCommand("sleepanimation");

        if (sleepCmd != null) {
            sleepCmd.setExecutor(new SleepAnimationCommand(this));
        }

        Bukkit.getLogger().info("");
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "  |_______|                             " +
                "  ");
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "  | Derex |     Sleep Animation v" + getDescription().getVersion());
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "  |_______|     Running on " + Bukkit.getName() + " - " + Bukkit.getVersion());
        Bukkit.getLogger().info("");
    }

    @Override
    public void onDisable() {
        if (timeSkipper != null) {
            timeSkipper.clear();
        }
    }

    public TimeSkipper getTimeSkipper() {
        return timeSkipper;
    }

    public void setTimeSkipper(TimeSkipper timeSkipper) {
        this.timeSkipper = timeSkipper;
    }

    private boolean shouldUseModernTimeSkipListener() {
        return isPaperVersionAtLeast("26.1.2") || hasClockTimeSkipEventClass();
    }

    private boolean isPaperVersionAtLeast(String minVersion) {
        String bukkitVersion = Bukkit.getServer().getBukkitVersion();
        if (bukkitVersion == null) {
            return false;
        }

        String baseVersion = bukkitVersion.split("-")[0];
        if (!baseVersion.matches("\\d+(\\.\\d+)*")) {
            return false;
        }

        return compareVersions(baseVersion, minVersion) >= 0;
    }

    private int compareVersions(String left, String right) {
        String[] leftParts = left.split("\\.");
        String[] rightParts = right.split("\\.");
        int max = Math.max(leftParts.length, rightParts.length);

        for (int i = 0; i < max; i++) {
            int leftNum = i < leftParts.length ? parseVersionPart(leftParts[i]) : 0;
            int rightNum = i < rightParts.length ? parseVersionPart(rightParts[i]) : 0;

            if (leftNum != rightNum) {
                return Integer.compare(leftNum, rightNum);
            }
        }

        return 0;
    }

    private int parseVersionPart(String part) {
        try {
            return Integer.parseInt(part);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private boolean hasClockTimeSkipEventClass() {
        try {
            Class.forName("org.bukkit.event.world.ClockTimeSkipEvent", false, getClass().getClassLoader());
            return true;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }
}
