package com.lx.svrutil;

import com.lx.svrutil.config.MainConfig;
import com.lx.svrutil.data.JoinMessage;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

public class Events {

    public static void onServerTick(MinecraftServer server) {
        TickManager.onTickUpdate();
    }

    public static void onTickEnd(MinecraftServer server) {
        if(server.getTicks() % 20 == 0) {
            server.getPlayerManager().getPlayerList().forEach(player -> {
                if (SvrUtil.fakeTimeList.containsKey(player.getUuid())) {
                    WorldTimeUpdateS2CPacket packet = new WorldTimeUpdateS2CPacket(SvrUtil.fakeTimeList.get(player.getUuid()), SvrUtil.fakeTimeList.get(player.getUuid()), true);
                    player.networkHandler.sendPacket(packet);
                }
            });
        }
    }

    public static void onJoin(ServerPlayNetworkHandler dispatcher, PacketSender sender, MinecraftServer server) {
        for(JoinMessage joinMessage : MainConfig.joinMessages) {
            joinMessage.show(dispatcher.getPlayer());
        }
    }
}