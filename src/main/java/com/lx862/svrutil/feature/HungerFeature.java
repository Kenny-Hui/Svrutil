package com.lx862.svrutil.feature;

import com.google.gson.JsonObject;
import net.minecraft.server.MinecraftServer;

public class HungerFeature extends Feature {
    public int minHunger = 0;
    public int maxHunger = 20;

    public HungerFeature() {
        super("Hunger", "hunger");
    }

    @Override
    public void readConfig(JsonObject jsonObject) {
        super.readConfig(jsonObject);
        this.minHunger = jsonObject.get("minLevel").getAsInt();
        this.maxHunger = jsonObject.get("maxLevel").getAsInt();
    }

    @Override
    public JsonObject writeConfig() {
        JsonObject jsonObject = super.writeConfig();
        jsonObject.addProperty("minLevel", minHunger);
        jsonObject.addProperty("maxLevel", maxHunger);
        return jsonObject;
    }

    @Override
    public void onTick(MinecraftServer server) {
        server.getPlayerManager().getPlayerList().forEach(player -> {
            int foodLevel = player.getHungerManager().getFoodLevel();
            if(foodLevel < minHunger) {
                player.getHungerManager().setFoodLevel(minHunger);
            } else if(foodLevel > maxHunger) {
                player.getHungerManager().setFoodLevel(maxHunger);
            }
        });
    }
}
