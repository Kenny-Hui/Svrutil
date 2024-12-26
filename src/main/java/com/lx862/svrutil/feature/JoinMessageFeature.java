package com.lx862.svrutil.feature;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lx862.svrutil.TickManager;
import com.lx862.svrutil.data.JoinMessage;
import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.Placeholders;
import eu.pb4.placeholders.api.TextParserUtils;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class JoinMessageFeature extends Feature {
    public final List<JoinMessage> joinMessages = new ArrayList<>();

    public JoinMessageFeature() {
        super("Join Message", "join_message");
    }

    @Override
    public void readConfig(JsonObject jsonObject) {
        super.readConfig(jsonObject);
        joinMessages.clear();
        jsonObject.getAsJsonArray("entries").forEach(e -> {
            joinMessages.add(JoinMessage.fromJson(e.getAsJsonObject()));
        });
    }

    @Override
    public JsonObject writeConfig() {
        JsonObject jsonObject = super.writeConfig();
        JsonArray entries = new JsonArray();
        for(JoinMessage joinMessage : joinMessages) {
            entries.add(JoinMessage.toJson(joinMessage));
        }
        jsonObject.add("entries", entries);
        return jsonObject;
    }

    @Override
    public void onPlayerJoin(ServerPlayNetworkHandler dispatcher, PacketSender sender, MinecraftServer server) {
        ServerPlayerEntity player = dispatcher.getPlayer();
        
        for(JoinMessage joinMessage : joinMessages) {
            if(!joinMessage.permLevel.isEmpty() && !joinMessage.permLevel.contains(server.getPermissionLevel(player.getGameProfile()))) continue;
            PlaceholderContext placeholderContext = new PlaceholderContext(server, server.getCommandSource(), player.getWorld(), player, player, player.getGameProfile());

            TickManager.schedule(joinMessage.delayTick, () -> {
                if(joinMessage.title != null) player.networkHandler.sendPacket(new TitleS2CPacket(Placeholders.parseText(TextParserUtils.formatText(joinMessage.title), placeholderContext)));
                if(joinMessage.subtitle != null) player.networkHandler.sendPacket(new SubtitleS2CPacket(Placeholders.parseText(TextParserUtils.formatText(joinMessage.subtitle), placeholderContext)));
                if(joinMessage.message != null) player.sendMessage(Placeholders.parseText(TextParserUtils.formatText(joinMessage.message), placeholderContext), false);
            });
        }
    }
}
