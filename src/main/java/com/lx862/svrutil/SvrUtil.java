package com.lx862.svrutil;

import com.lx862.svrutil.config.Config;
import com.lx862.svrutil.transition.TransitionManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class SvrUtil implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger(ModInfo.MOD_NAME);
    public static final String[] motds = {"Have a great day!", "OwO What's this?", "made ya look", "Drink water!", "i use arch btw", "The mystery is of Wet and Dry. And where does the solution lie?", "Backup Regularly >.<", "I'll be back in a jiffy!", "Treasure everything before it's no longer there.", "3 Billion Devices runs Java!"};
    public static final HashMap<String, String> lastReply = new HashMap<>();
    public static final HashMap<UUID, Long> fakeTimeList = new HashMap<>();

    @Override
    public void onInitialize() {
        String motd = motds[(int)(System.currentTimeMillis() % motds.length)];
        LOGGER.info("[{}] {}", ModInfo.MOD_NAME, motd);

        Config.loadAll();

        // WIP Feature
        if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
            TransitionManager.initialize();
        }

        Mappings.registerCommand(Commands::register);

        /* Register Fabric API Events */
        ServerPlayConnectionEvents.JOIN.register(Events::onJoin);
        ServerTickEvents.START_SERVER_TICK.register(Events::onServerTick);
        ServerTickEvents.END_SERVER_TICK.register(Events::onTickEnd);
    }
}
