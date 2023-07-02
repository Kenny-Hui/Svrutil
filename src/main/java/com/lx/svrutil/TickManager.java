package com.lx.svrutil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TickManager {
    private static int ticks = 0;
    private static final ConcurrentHashMap<Integer, Runnable> scheduleList = new ConcurrentHashMap<>();
    private static final List<Runnable> tickSubscriber = new ArrayList<>();

    /**
     * Schedule a runnable to be called after X Minecrft tick has elapsed since this call
     * @param ticksAfter The tick to wait
     * @param callback The callback to your method that does the thing after ticksAfter tick.
     */
    public static void schedule(int ticksAfter, Runnable callback) {
        scheduleList.put(ticks + ticksAfter, callback);
    }

    public static void onTickCallback(Runnable consumer) {
        tickSubscriber.add(consumer);
    }

    /**
     * Only call this whenever a Minecraft server tick, use {@link #onTickCallback(Runnable)} to register a tick callback.
     */
    public static void onTickUpdate() {
        ticks++;
        for(Runnable runnable : tickSubscriber) {
            try {
                runnable.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(Map.Entry<Integer, Runnable> entry : scheduleList.entrySet()) {
            if(ticks >= entry.getKey()) {
                scheduleList.remove(entry.getKey());
                entry.getValue().run();
            }
        }
    }

    public static int getTicks() {
        return ticks;
    }
}