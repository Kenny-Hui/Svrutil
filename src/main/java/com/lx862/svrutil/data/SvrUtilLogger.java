package com.lx862.svrutil.data;

import com.lx862.svrutil.ModInfo;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;

public class SvrUtilLogger {
    public static boolean isDev = FabricLoader.getInstance().isDevelopmentEnvironment();
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(ModInfo.MOD_NAME);

    public static void info(String str, Object... o) {
        LOGGER.info("[{}] " + str, ModInfo.MOD_NAME, o);
    }

    public static void warn(String str, Object... o) {
        LOGGER.warn("[{}] " + str, ModInfo.MOD_NAME, o);
    }

    public static void error(String str, Object... o) {
        LOGGER.error("[{}] " + str, ModInfo.MOD_NAME, o);
    }

    public static void fatal(String str, Object... o) {
        LOGGER.fatal("[{}] " + str, ModInfo.MOD_NAME, o);
    }

    public static void debug(String str, Object... o) {
        if(isDev) info("[DEBUG] " + str, o);
    }
}
