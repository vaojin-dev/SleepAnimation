package io.github.derec4.sleepAnimation;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;

import java.lang.reflect.Method;

public class SleepListenerModern implements Listener {

    private static final String NIGHT_SKIP = "NIGHT_SKIP";

    private final SleepAnimation plugin;

    public SleepListenerModern(SleepAnimation plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTimeSkip(TimeSkipEvent event) {
        if (!isNightSkip(event)) {
            return;
        }

        event.setCancelled(true);
        plugin.getTimeSkipper().startAnimation(event.getWorld());

        if (ConfigManager.isInstantWakeup()) {
            for (Player player : event.getWorld().getPlayers()) {
                if (player.isSleeping()) {
                    player.wakeup(false);
                }
            }
        }
    }

    private boolean isNightSkip(TimeSkipEvent event) {
        try {
            Method getSkipReason = event.getClass().getMethod("getSkipReason");
            Object reason = getSkipReason.invoke(event);
            if (reason instanceof Enum<?>) {
                return NIGHT_SKIP.equals(((Enum<?>) reason).name());
            }
            return NIGHT_SKIP.equals(String.valueOf(reason));
        } catch (ReflectiveOperationException ex) {
            return false;
        }
    }
}
