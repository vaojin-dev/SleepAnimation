package io.github.derec4.sleepAnimation;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private static int skipSpeed;
    private static boolean instantWakeup;

    public static void loadConfig(SleepAnimation plugin) {
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();

        skipSpeed = config.getInt("skip-speed", 50);
        instantWakeup = config.getBoolean("instant-wakeup", false);
    }

    public static int getSkipSpeed() {
        return skipSpeed;
    }

    public static boolean isInstantWakeup() {
        return instantWakeup; 
    }
}