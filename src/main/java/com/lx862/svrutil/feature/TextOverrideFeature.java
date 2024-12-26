package com.lx862.svrutil.feature;

import com.google.gson.JsonObject;
import eu.pb4.placeholders.api.TextParserUtils;
import net.minecraft.text.Text;
import net.minecraft.util.JsonHelper;

public class TextOverrideFeature extends Feature {
    private static String whitelistedMessage = null;

    public TextOverrideFeature() {
        super("Text Override", "text_override");
    }

    @Override
    public void readConfig(JsonObject jsonObject) {
        super.readConfig(jsonObject);
        whitelistedMessage = JsonHelper.getString(jsonObject, "whitelistedMessage", null);
    }

    @Override
    public JsonObject writeConfig() {
        JsonObject jsonObject = super.writeConfig();
        jsonObject.addProperty("whitelistedMessage", whitelistedMessage);
        return jsonObject;
    }

    public Text getWhitelistedMessage() {
        return TextParserUtils.formatText(whitelistedMessage);
    }
}
