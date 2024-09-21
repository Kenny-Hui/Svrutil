package com.lx862.svrutil;

import com.lx862.svrutil.feature.FeatureSet;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

public class Events {

    public static void onServerTick(MinecraftServer server) {
        TickManager.onTickUpdate();
    }

    public static void onTickEnd(MinecraftServer server) {
        for(FeatureSet featureSet : FeatureSet.values()) {
            if(featureSet.feature.enabled) {
                featureSet.feature.onTick(server);
            }
        }

        server.getPlayerManager().getPlayerList().forEach(player -> {
            // Sync fake time to client
            if(server.getTicks() % 20 == 0) {
                if (SvrUtil.fakeTimeList.containsKey(player.getUuid())) {
                    WorldTimeUpdateS2CPacket packet = new WorldTimeUpdateS2CPacket(SvrUtil.fakeTimeList.get(player.getUuid()), SvrUtil.fakeTimeList.get(player.getUuid()), true);
                    player.networkHandler.sendPacket(packet);
                }
            }
        });
    }

    public static void onJoin(ServerPlayNetworkHandler dispatcher, PacketSender sender, MinecraftServer server) {
        for(FeatureSet featureSet : FeatureSet.values()) {
            if(featureSet.feature.enabled) {
                featureSet.feature.onPlayerJoin(dispatcher, sender, server);
            }
        }
    }
}