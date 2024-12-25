package com.lx862.svrutil.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import eu.pb4.placeholders.api.TextParserUtils;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class JoinMessage {
    public Text title;
    public Text subtitle;
    public Text joinMessage;
    public int delayTick;
    public List<Integer> permLevel;

    public JoinMessage(Text title, Text subtitle, Text joinMessage, int delayTick, List<Integer> permLevel) {
        this.title = title;
        this.subtitle = subtitle;
        this.joinMessage = joinMessage;
        this.delayTick = delayTick;
        this.permLevel = permLevel;
    }

    public static JoinMessage fromJson(JsonObject jsonObject) {
        List<Integer> permLevels = new ArrayList<>();
        int delayTick = jsonObject.has("delayTick") ? jsonObject.get("delayTick").getAsInt() : 0;
        Text title = jsonObject.has("title") ? TextParserUtils.formatText(jsonObject.get("title").getAsString()) : null;
        Text subtitle = jsonObject.has("subtitle") ? TextParserUtils.formatText(jsonObject.get("subtitle").getAsString()) : null;
        Text joinMessage = jsonObject.has("message") ? TextParserUtils.formatText(jsonObject.get("message").getAsString()) : null;

        try {
            jsonObject.get("permLevels").getAsJsonArray().forEach(e -> {
                permLevels.add(e.getAsInt());
            });
        } catch (Exception ignored) {
        }

        return new JoinMessage(title, subtitle, joinMessage, delayTick, permLevels);
    }

    public static JsonObject toJson(JoinMessage joinMessage) {
        JsonObject jsonObject = new JsonObject();
        JsonArray permLevels = new JsonArray();
        for(Integer i : joinMessage.permLevel) {
            permLevels.add(i);
        }
        jsonObject.addProperty("title", "");
        jsonObject.addProperty("subtitle", "");
        jsonObject.addProperty("message", "<yellow>Please edit 'config/svrutil/feature.json' to change the welcome message. Thanks for installing Svrutil.</yellow>");
        jsonObject.addProperty("delayTick", joinMessage.delayTick);
        jsonObject.add("permLevels", permLevels);
        return jsonObject;
    }
}
