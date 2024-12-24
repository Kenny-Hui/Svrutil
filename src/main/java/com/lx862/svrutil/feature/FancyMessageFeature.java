package com.lx862.svrutil.feature;

import com.google.gson.JsonObject;
import net.minecraft.server.MinecraftServer;

public class FancyMessageFeature extends Feature {

    public FancyMessageFeature() {
        super("Fancy Message", "fancy_msg");
    }

    @Override
    public void readConfig(JsonObject jsonObject) {
        super.readConfig(jsonObject);
    }

    @Override
    public JsonObject writeConfig() {
        JsonObject jsonObject = super.writeConfig();
        return jsonObject;
    }
}
