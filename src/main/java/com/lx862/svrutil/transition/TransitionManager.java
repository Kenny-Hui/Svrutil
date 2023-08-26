package com.lx862.svrutil.transition;

import com.lx862.svrutil.*;
import com.lx862.svrutil.transition.data.*;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.SetCameraEntityS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransitionManager {
    public static final HashMap<ServerPlayerEntity, TransitioningData> playerInTransition = new HashMap<>();

    public static void initialize() {
        TickManager.onTickCallback(() -> {
            for(Map.Entry<ServerPlayerEntity, TransitioningData> entry : playerInTransition.entrySet()) {
                executeTransition(entry.getKey(), entry.getValue());
            }
        });
    }

    private static void executeTransition(ServerPlayerEntity player, TransitioningData transitionData) {
        Transition transition = transitionData.transition;

        if(player.isRemoved() || !player.isAlive()) {
            SvrUtil.LOGGER.info("[{}] Player left the game or died, removing player from transition.", ModInfo.MOD_NAME);
            playerFinishedTransition(player, transition.transitionViewType, transitionData.originalPlayerPos, transitionData.cameraEntity);
        }

        if(!transitionData.cameraEntity.isAlive()) {
            SvrUtil.LOGGER.info("[{}] Camera Entity died, removing player from transition.", ModInfo.MOD_NAME);
            playerFinishedTransition(player, transition.transitionViewType, transitionData.originalPlayerPos, transitionData.cameraEntity);
        }

        Map.Entry<Integer, TransitionSlide> currentSlideData = transition.slides.entrySet().stream().filter(e -> {
            int startTime = e.getKey();
            System.out.println("s: " + startTime);
            int endTime = startTime + e.getValue().durationTick;
            System.out.println("st: " + endTime);
            System.out.println("el: " + transitionData.elapsedTicks);
            return transitionData.elapsedTicks >= startTime && transitionData.elapsedTicks <= endTime;
        }).findFirst().orElse(null);

        if(currentSlideData == null) {
            SvrUtil.LOGGER.info("[{}] Cannot find the current slide, assume finished.", ModInfo.MOD_NAME);
            playerFinishedTransition(player, transition.transitionViewType, transitionData.originalPlayerPos, transitionData.cameraEntity);
            return;
        }

        TransitionSlide currentSlide = currentSlideData.getValue();
        int currentSlideElapsedTick = transitionData.elapsedTicks - currentSlideData.getKey();

        List<Map.Entry<Integer, Keyframe>> nowAndFutureKeyframes = currentSlide.keyframes.entrySet().stream().filter(e -> {
            int keyframeStartTime = e.getKey();
            return keyframeStartTime >= (currentSlideElapsedTick - e.getValue().duration);
        }).toList();

        if(nowAndFutureKeyframes.size() <= 1) {
            SvrUtil.LOGGER.info("[{}] Cannot find the current or next keyframe, assume finished.", ModInfo.MOD_NAME);
//            playerFinishedTransition(player, transition.transitionViewType, transitionData.originalPlayerPos, transitionData.cameraEntity);
            return;
        }

        Map.Entry<Integer, Keyframe> currentKeyframe = nowAndFutureKeyframes.get(0);
        Map.Entry<Integer, Keyframe> nextKeyframe = nowAndFutureKeyframes.get(1);
        Vec3d cameraPos;
        Vec3d cameraAngle = null;
        double a1 = currentSlideElapsedTick > currentKeyframe.getKey() ? transitionData.elapsedTicks - currentKeyframe.getKey() : transitionData.elapsedTicks;
        double a2 = currentSlideElapsedTick > currentKeyframe.getKey() ? nextKeyframe.getKey() - currentKeyframe.getKey() : nextKeyframe.getKey();
        double delta = (a1 / a2);
        System.out.println("Delta: " + a1 / a2);

        cameraPos = Util.lerpVec3d(delta, currentKeyframe.getValue().position, nextKeyframe.getValue().position);

        if(currentKeyframe.getValue().camera != null && nextKeyframe.getValue().camera != null) {
            cameraAngle = Util.lerpVec3d(delta, currentKeyframe.getValue().camera, nextKeyframe.getValue().camera);
        }

        if(transition.transitionViewType == TransitionViewType.SPECTATE || transition.transitionViewType == TransitionViewType.TELEPORT_SPECTATE) {
            player.networkHandler.sendPacket(new SetCameraEntityS2CPacket(transitionData.cameraEntity));
            transitionData.cameraEntity.teleport(cameraPos.x, cameraPos.y, cameraPos.z);
            if(cameraAngle != null) {
                transitionData.cameraEntity.setHeadYaw((float)(cameraAngle.x));
                transitionData.cameraEntity.setPitch((float) (cameraAngle.z));
            }
        }

        if(transition.transitionViewType == TransitionViewType.TELEPORT || transition.transitionViewType == TransitionViewType.TELEPORT_SPECTATE) {
            player.teleport(cameraPos.x, cameraPos.y, cameraPos.z);
            if(cameraAngle != null) {
                player.refreshPositionAndAngles(cameraPos.x, cameraPos.y, cameraPos.z, (float)cameraAngle.x, (float)cameraAngle.z);
                player.setHeadYaw((float)(cameraAngle.x));
                player.setPitch((float) (cameraAngle.z));
            }
        }

        if(currentSlideElapsedTick == 0) {
            System.out.println("Player first entered slide, playing sound to player.");
            Mappings.sendPlaySoundIdS2CPacket(player.getWorld(), player, currentSlide.startSound, SoundCategory.MASTER, player.getPos(), 10000, 1);
        }

        transitionData.elapsedTicks++;
        playerInTransition.put(player, transitionData);
    }

    private static void playerFinishedTransition(ServerPlayerEntity player, TransitionViewType transitionViewType, Vec3d originalPlayerPos, Entity cameraEntity) {
        // Reset the player's camera to no longer follow the armor stand
        player.networkHandler.sendPacket(new SetCameraEntityS2CPacket(player));

        if(transitionViewType != TransitionViewType.SPECTATE) {
            player.teleport(originalPlayerPos.x, originalPlayerPos.y, originalPlayerPos.z);
        }
        cameraEntity.kill();
        playerInTransition.remove(player);
    }
}
