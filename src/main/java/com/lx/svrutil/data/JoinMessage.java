package com.lx.svrutil.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lx.svrutil.Util;
import net.minecraft.server.network.ServerPlayerEntity;
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

    public void show(ServerPlayerEntity player) {
        if(!permLevel.isEmpty() && !permLevel.contains(Util.getPermLevel(player))) return;

        Util.showWelcomeMessage(player, this);
    }

    public static JoinMessage fromJson(JsonObject jsonObject) {
        List<Integer> permLevels = new ArrayList<>();
        Text title = jsonObject.has("title") ? Text.Serializer.fromJson(jsonObject.get("title")) : null;
        Text subtitle = jsonObject.has("subtitle") ? Text.Serializer.fromJson(jsonObject.get("subtitle")) : null;
        Text joinMessage = jsonObject.has("message") ? Text.Serializer.fromJson(jsonObject.get("message")) : null;
        int delayTick = jsonObject.has("delayTick") ? jsonObject.get("delayTick").getAsInt() : 0;

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
        jsonObject.addProperty("title", Text.Serializer.toJson(joinMessage.title));
        jsonObject.addProperty("subtitle", Text.Serializer.toJson(joinMessage.subtitle));
        jsonObject.addProperty("message", Text.Serializer.toJson(joinMessage.joinMessage));
        jsonObject.addProperty("delayTick", joinMessage.delayTick);
        jsonObject.add("permLevels", permLevels);
        return jsonObject;
    }
}
