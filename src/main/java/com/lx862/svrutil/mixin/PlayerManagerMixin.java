package com.lx862.svrutil.mixin;

import com.lx862.svrutil.SvrUtil;
import com.lx862.svrutil.feature.TextOverrideFeature;
import com.lx862.svrutil.feature.Feature;
import com.lx862.svrutil.feature.FeatureSet;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.SocketAddress;
import java.util.List;

/*
 * This Mixin is used for sending fake time packets to client (/clienttime), and overriding the whitelist message.
 */
@Mixin(value = PlayerManager.class)
public class PlayerManagerMixin {

    @Shadow @Final private List<ServerPlayerEntity> players;

    @Inject(method = "sendToDimension", at = @At("HEAD"), cancellable = true)
    public void sendToDimension(Packet<?> packet, RegistryKey<World> dimension, CallbackInfo ci) {
        if(packet instanceof WorldTimeUpdateS2CPacket) {
            for (ServerPlayerEntity serverPlayerEntity : players) {
                if (serverPlayerEntity.getWorld().getRegistryKey() == dimension && !SvrUtil.fakeTimeList.containsKey(serverPlayerEntity.getUuid())) {
                    serverPlayerEntity.networkHandler.sendPacket(packet);
                }
            }
            ci.cancel();
        }
    }

    @Inject(method = "checkCanJoin", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    public void checkCanJoinWhitelist(SocketAddress address, GameProfile profile, CallbackInfoReturnable<Text> cir) {
        Feature featureSet = FeatureSet.TEXT_OVERRIDE.feature;
        Text whitelistedMessage = ((TextOverrideFeature)featureSet).getWhitelistedMessage();
        if(featureSet.enabled && whitelistedMessage != null) {
            cir.setReturnValue(whitelistedMessage);
            cir.cancel();
        }
    }
}