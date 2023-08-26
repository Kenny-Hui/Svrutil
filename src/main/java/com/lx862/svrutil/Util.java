package com.lx862.svrutil;

import com.google.gson.JsonArray;
import com.lx862.svrutil.data.JoinMessage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Util {
    private static final int OP_LEVEL = 4;

    public static void showWelcomeMessage(ServerPlayerEntity player, JoinMessage joinMsg) {
        TickManager.schedule(joinMsg.delayTick, () -> {
            if(joinMsg.joinMessage != null) {
                player.sendMessage(joinMsg.joinMessage, false);
            }

            if(joinMsg.title != null) {
                player.networkHandler.sendPacket(new TitleS2CPacket(joinMsg.title));
            }
            if(joinMsg.subtitle != null) {
                player.networkHandler.sendPacket(new SubtitleS2CPacket(joinMsg.subtitle));
            }
        });
    }

    public static int getPermLevel(PlayerEntity player) {
        for(int i = 0; i < OP_LEVEL; i++) {
            if(player.hasPermissionLevel(OP_LEVEL - i)) {
                return OP_LEVEL - i;
            }
        }
        return 0;
    }

    public static Vec3d getVec3dFromJsonArray(JsonArray array) {
        return new Vec3d(array.get(0).getAsDouble(), array.get(1).getAsDouble(), array.get(2).getAsDouble());
    }

    public static Vec3d getXZFromJsonArray(JsonArray array) {
        return new Vec3d(array.get(0).getAsDouble(), 0, array.get(1).getAsDouble());
    }

    public static Vec3d lerpVec3d(double delta, Vec3d pos1, Vec3d pos2) {
        if(pos2 == null) return pos1;
        return new Vec3d(MathHelper.lerp(delta, pos1.x, pos2.x), MathHelper.lerp(delta, pos1.y, pos2.y), MathHelper.lerp(delta, pos1.z, pos2.z));
    }
}
