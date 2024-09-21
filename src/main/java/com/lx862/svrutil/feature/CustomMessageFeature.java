package com.lx862.svrutil.feature;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.text.Text;

public class CustomMessageFeature extends Feature {
    private static Text whitelistedMessage = null;

    public CustomMessageFeature() {
        super("Custom Message", "custom_message");
    }

    @Override
    public void readConfig(JsonObject jsonObject) {
        super.readConfig(jsonObject);
        if(jsonObject.has("whitelistedMessage")) {
            JsonElement element = jsonObject.get("whitelistedMessage");
            try {
                whitelistedMessage = Text.Serializer.fromJson(element.getAsString());
            } catch (Exception ignored) {
            }

            try {
                whitelistedMessage = Text.Serializer.fromJson(element);
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public JsonObject writeConfig() {
        JsonObject jsonObject = super.writeConfig();
        jsonObject.addProperty("whitelistedMessage", Text.Serializer.toJson(whitelistedMessage));
        return jsonObject;
    }

    public Text getWhitelistedMessage() {
        return whitelistedMessage;
    }
}
