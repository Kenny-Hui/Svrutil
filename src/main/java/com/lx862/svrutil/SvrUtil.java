package com.lx862.svrutil;

import com.lx862.svrutil.config.Config;
import com.lx862.svrutil.data.SvrUtilLogger;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import java.util.*;

public class SvrUtil implements ModInitializer {
    public static final String[] motds = {"Have a great day!", "OwO What's this?", "made ya look", "Drink water!", "i use arch btw", "The mystery is of Wet and Dry. And where does the solution lie?", "Backup Regularly >.<", "I'll be back in a jiffy!", "Treasure everything before it's no longer there.", "3 Billion Devices runs Java!"};
    public static final HashMap<String, String> lastReply = new HashMap<>();
    public static final HashMap<UUID, Long> fakeTimeList = new HashMap<>();

    @Override
    public void onInitialize() {
        String motd = motds[(int)(System.currentTimeMillis() % motds.length)];
        SvrUtilLogger.info(motd);
        Config.loadAll();

        /* Register Fabric API Events */
        ServerPlayConnectionEvents.JOIN.register(Events::onJoin);
        ServerTickEvents.START_SERVER_TICK.register(Events::onServerTick);
        ServerTickEvents.END_SERVER_TICK.register(Events::onTickEnd);
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, third) -> Commands.register(dispatcher));
    }
}
