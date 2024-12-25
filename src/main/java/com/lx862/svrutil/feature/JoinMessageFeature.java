package com.lx862.svrutil.feature;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lx862.svrutil.TickManager;
import com.lx862.svrutil.data.JoinMessage;
import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.Placeholders;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class JoinMessageFeature extends Feature {
    public final List<JoinMessage> joinMessages = new ArrayList<>();

    public JoinMessageFeature() {
        super("Join Message", "join_message");
        this.joinMessages.add(new JoinMessage(null, null, Text.literal("Please edit 'config/svrutil/feature.json' to change the welcome message. Thanks for installing Svrutil.").formatted(Formatting.AQUA), 60, List.of(1,2,3,4)));
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
    public JsonObject generateConfig() {
        JsonObject jsonObject = super.generateConfig();
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
            if(!joinMessage.permLevel.isEmpty() && !joinMessage.permLevel.contains(server.getPermissionLevel(player.getGameProfile()))) return;
            PlaceholderContext placeholderContext = new PlaceholderContext(server, server.getCommandSource(), player.getWorld(), player, player, player.getGameProfile());

            TickManager.schedule(joinMessage.delayTick, () -> {
                if(joinMessage.title != null) player.networkHandler.sendPacket(new TitleS2CPacket(Placeholders.parseText(joinMessage.title, placeholderContext)));
                if(joinMessage.subtitle != null) player.networkHandler.sendPacket(new SubtitleS2CPacket(Placeholders.parseText(joinMessage.subtitle, placeholderContext)));
                if(joinMessage.joinMessage != null) player.sendMessage(Placeholders.parseText(joinMessage.joinMessage, placeholderContext), false);
            });
        }
    }
}
