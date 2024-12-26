package com.lx862.svrutil.feature;

import com.google.gson.JsonObject;
import net.minecraft.util.JsonHelper;

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
        this.fallingBlockDelay = JsonHelper.getInt(jsonObject, "fallingBlockDelay", fallingBlockDelay);
        this.immutableItemFrame = JsonHelper.getBoolean(jsonObject, "immutableItemFrame", immutableItemFrame);
        this.minItemFrameInteractOpLevel = JsonHelper.getInt(jsonObject, "minItemFrameInteractOpLevel", minItemFrameInteractOpLevel);
    }

    @Override
    public JsonObject generateConfig() {
        JsonObject jsonObject = super.generateConfig();
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
