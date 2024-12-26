package com.lx862.svrutil.feature;

import com.google.gson.JsonObject;
import eu.pb4.placeholders.api.TextParserUtils;
import net.minecraft.text.Text;
import net.minecraft.util.JsonHelper;

public class TextOverrideFeature extends Feature {
    private static Text whitelistedMessage = null;

    public TextOverrideFeature() {
        super("Text Override", "text_override");
    }

    @Override
    public void readConfig(JsonObject jsonObject) {
        super.readConfig(jsonObject);
        whitelistedMessage = JsonHelper.hasString(jsonObject, "whitelistedMessage") ? TextParserUtils.formatText(JsonHelper.getString(jsonObject, "whitelistedMessage")) : null;
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
