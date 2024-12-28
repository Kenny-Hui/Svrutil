package com.lx862.svrutil.mixin;

import com.lx862.svrutil.SvrUtil;
import com.lx862.svrutil.commands.afk;
import com.lx862.svrutil.feature.FancyMessageFeature;
import com.lx862.svrutil.feature.FeatureSet;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.MessageCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.List;

@Mixin(MessageCommand.class)
public class MessageCommandMixin {
    @Inject(method = "execute", at = @At("HEAD"), cancellable = true)
    private static void execute(ServerCommandSource source, Collection<ServerPlayerEntity> targets, SignedMessage message, CallbackInfo ci) {
        String playerName = source.getName();

        SvrUtil.lastReply.put(source.getName(), targets.stream().map(e -> e.getGameProfile().getName()).toList());

        for(ServerPlayerEntity player : targets) {
            SvrUtil.lastReply.put(player.getGameProfile().getName(), List.of(source.getName()));
        }

        if(FeatureSet.FANCY_MESSAGE.feature.enabled) {
            for(ServerPlayerEntity target : targets) {
                Text msgContent = message.getContent();

                source.sendFeedback(Text.literal(String.format("§6[me §r-> §6%s]: ", target.getGameProfile().getName())).append(msgContent), false);
                target.sendMessage(Text.literal(String.format("§6[%s §r-> §6me]: ", playerName)).append(msgContent), false);

                Identifier soundEffect = ((FancyMessageFeature)FeatureSet.FANCY_MESSAGE.feature).getMessageSound();
                if(soundEffect != null) {
                    target.networkHandler.sendPacket(new PlaySoundS2CPacket(RegistryEntry.of(SoundEvent.of(soundEffect)), SoundCategory.MASTER, target.getX(), target.getY(), target.getZ(), 1, 1, target.getWorld().getRandom().nextLong()));
                }

                if(afk.afkList.containsKey(target.getUuid()) && source.isExecutedByPlayer()) {
                    source.getPlayer().sendMessageToClient(Text.literal("").append(target.getDisplayName()).append(" are AFK and may not be available at the moment.").formatted(Formatting.YELLOW), true);
                }
            }
            ci.cancel();
        }
    }
}
