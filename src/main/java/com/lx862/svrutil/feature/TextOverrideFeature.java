package com.lx862.svrutil.feature;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eu.pb4.placeholders.api.TextParserUtils;
import net.minecraft.text.Text;

public class TextOverrideFeature extends Feature {
    private static Text whitelistedMessage = null;

    public TextOverrideFeature() {
        super("Text Override", "text_override");
    }

    @Override
    public void readConfig(JsonObject jsonObject) {
        super.readConfig(jsonObject);
        if(jsonObject.has("whitelistedMessage")) {
            JsonElement element = jsonObject.get("whitelistedMessage");
            try {
                whitelistedMessage = Text.Serializer.fromJson(element);
            } catch (Exception ignored) {
            }

            try {
                whitelistedMessage = TextParserUtils.formatText(element.getAsString());
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public JsonObject generateConfig() {
        JsonObject jsonObject = super.generateConfig();
        jsonObject.addProperty("whitelistedMessage", "You are not whitelisted on the server.");
        return jsonObject;
    }

    public Text getWhitelistedMessage() {
        return whitelistedMessage;
    }
}
