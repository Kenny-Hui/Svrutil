package com.lx.svrutil;

import net.fabricmc.loader.api.FabricLoader;

public class ModInfo {
    public static final String MOD_NAME = "SvrUtil";
    public static final String MOD_ID = "svrutil";

    public static String getVersion() {
        try {
            return FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion().getFriendlyString();
        } catch (Exception e) {
            e.printStackTrace();
            return "<Unknown>";
        }
    }
}
