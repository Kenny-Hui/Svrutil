package com.lx862.svrutil.feature;

import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class FancyMessageFeature extends Feature {
    private Identifier messageSound = Identifier.of("minecraft", "entity.experience_orb.pickup");

    public FancyMessageFeature() {
        super("Fancy Message", "fancy_msg");
    }

    @Override
    public void readConfig(JsonObject jsonObject) {
        super.readConfig(jsonObject);
        messageSound = Identifier.tryParse(JsonHelper.getString(jsonObject, "soundEffect", "!"));
    }

    @Override
    public JsonObject writeConfig() {
        JsonObject jsonObject = super.writeConfig();
        jsonObject.addProperty("soundEffect", messageSound == null ? null : messageSound.toString());
        return jsonObject;
    }

    public Identifier getMessageSound() {
        return messageSound;
    }
}
