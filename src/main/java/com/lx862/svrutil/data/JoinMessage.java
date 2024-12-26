package com.lx862.svrutil.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;

public class JoinMessage {
    public String title;
    public String subtitle;
    public String message;
    public int delayTick;
    public List<Integer> permLevel;

    public JoinMessage(String title, String subtitle, String message, int delayTick, List<Integer> permLevel) {
        this.title = title;
        this.subtitle = subtitle;
        this.message = message;
        this.delayTick = delayTick;
        this.permLevel = permLevel;
    }

    public static JoinMessage fromJson(JsonObject jsonObject) {
        List<Integer> permLevels = new ArrayList<>();
        String title = JsonHelper.getString(jsonObject, "title", null);
        String subtitle = JsonHelper.getString(jsonObject, "subtitle", null);
        String joinMessage = JsonHelper.getString(jsonObject, "message", null);
        int delayTick = JsonHelper.getInt(jsonObject, "delayTick", 0);

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
        jsonObject.addProperty("title", joinMessage.title);
        jsonObject.addProperty("subtitle", joinMessage.subtitle);
        jsonObject.addProperty("message", joinMessage.message);
        jsonObject.addProperty("delayTick", joinMessage.delayTick);
        jsonObject.add("permLevels", permLevels);
        return jsonObject;
    }
}
