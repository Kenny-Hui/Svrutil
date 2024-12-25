package com.lx862.svrutil.feature;

import com.google.gson.JsonObject;

public class VanillaMechanicsFeature extends Feature {
    private int fallingBlockDelay = 2;
    private boolean immutableItemFrame = false;
    private int minItemFrameInteractOpLevel = 0;

    public VanillaMechanicsFeature() {
        super("Vanilla Mechanics Modification", "vanilla_mechanics");
    }

    @Override
    public void readConfig(JsonObject jsonObject) {
        super.readConfig(jsonObject);
        this.fallingBlockDelay = jsonObject.get("fallingBlockDelay").getAsInt();
        this.immutableItemFrame = jsonObject.get("immutableItemFrame").getAsBoolean();
        this.minItemFrameInteractOpLevel = jsonObject.get("minItemFrameInteractOpLevel").getAsInt();
    }

    @Override
    public JsonObject writeConfig() {
        JsonObject jsonObject = super.writeConfig();
        jsonObject.addProperty("fallingBlockDelay", fallingBlockDelay);
        jsonObject.addProperty("immutableItemFrame", immutableItemFrame);
        jsonObject.addProperty("minItemFrameInteractOpLevel", minItemFrameInteractOpLevel);
        return jsonObject;
    }

    public int getFallingBlockDelay() {
        return fallingBlockDelay;
    }

    public boolean getImmutableItemFrame() {
        return immutableItemFrame;
    }
    public int getMinItemFrameInteractOpLevel() {
        return minItemFrameInteractOpLevel;
    }
}
