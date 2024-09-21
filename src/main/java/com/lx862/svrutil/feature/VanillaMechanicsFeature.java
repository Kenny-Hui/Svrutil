package com.lx862.svrutil.feature;

import com.google.gson.JsonObject;

public class VanillaMechanicsFeature extends Feature {
    private int fallingBlockDelay = 2;

    public VanillaMechanicsFeature() {
        super("Vanilla Mechanics Modification", "vanilla_mechanics");
    }

    @Override
    public void readConfig(JsonObject jsonObject) {
        super.readConfig(jsonObject);
        this.fallingBlockDelay = jsonObject.get("fallingBlockDelay").getAsInt();
    }

    @Override
    public JsonObject writeConfig() {
        JsonObject jsonObject = super.writeConfig();
        jsonObject.addProperty("fallingBlockDelay", fallingBlockDelay);
        return jsonObject;
    }

    public int getFallingBlockDelay() {
        return fallingBlockDelay;
    }
}
