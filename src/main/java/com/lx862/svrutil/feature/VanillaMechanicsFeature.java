package com.lx862.svrutil.feature;

import com.google.gson.JsonObject;

public class VanillaMechanicsFeature extends Feature {
    private int fallingBlockDelay = 2;
    private boolean fixedItemFrame = false;
    private int minItemFrameInteractOpLevel = 0;

    public VanillaMechanicsFeature() {
        super("Vanilla Mechanics Modification", "vanilla_mechanics");
    }

    @Override
    public void readConfig(JsonObject jsonObject) {
        super.readConfig(jsonObject);
        this.fallingBlockDelay = jsonObject.get("fallingBlockDelay").getAsInt();
        this.fixedItemFrame = jsonObject.get("fixedItemFrame").getAsBoolean();
        this.minItemFrameInteractOpLevel = jsonObject.get("minItemFrameInteractOpLevel").getAsInt();
    }

    @Override
    public JsonObject writeConfig() {
        JsonObject jsonObject = super.writeConfig();
        jsonObject.addProperty("fallingBlockDelay", fallingBlockDelay);
        jsonObject.addProperty("fixedItemFrame", fixedItemFrame);
        jsonObject.addProperty("minItemFrameInteractOpLevel", minItemFrameInteractOpLevel);
        return jsonObject;
    }

    public int getFallingBlockDelay() {
        return fallingBlockDelay;
    }

    public boolean getFixedItemFrame() {
        return fixedItemFrame;
    }
    public int getMinItemFrameInteractOpLevel() {
        return minItemFrameInteractOpLevel;
    }
}
