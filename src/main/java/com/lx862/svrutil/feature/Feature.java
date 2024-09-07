package com.lx862.svrutil.feature;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

public abstract class Feature {
    public final String displayName;
    public final String configName;
    public boolean enabled = true;

    public Feature(String displayName, String configName) {
        this.configName = configName;
        this.displayName = displayName;
    }

    public void readConfig(JsonObject jsonObject) {
        this.enabled = jsonObject.has("enabled") && jsonObject.get("enabled").getAsBoolean();
    }

    public JsonObject writeConfig() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("enabled", enabled);
        return jsonObject;
    }

    public void onTick(MinecraftServer server) {
    }

    public void onPlayerJoin(ServerPlayNetworkHandler dispatcher, PacketSender sender, MinecraftServer server) {
    }
}
