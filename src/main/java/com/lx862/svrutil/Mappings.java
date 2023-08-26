package com.lx862.svrutil;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.MutableText;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class Mappings {
    public static MutableText literalText(String content) {
        return Text.literal(content);
    }

    public static void broadcast(PlayerManager playerManager, PlayerEntity player, MutableText content) {
        playerManager.broadcast(content, false);
    }

    public static void registerCommand(Consumer<CommandDispatcher<ServerCommandSource>> callback) {
        net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, third) -> callback.accept(dispatcher));
    }

    public static void sendPlaySoundIdS2CPacket(World world, ServerPlayerEntity player, Identifier sound, SoundCategory soundCategory, Vec3d pos, float volume, float pitch) {
        #if MC_VERSION > "11902"
            net.minecraft.registry.entry.RegistryEntry<net.minecraft.sound.SoundEvent> soundEvent = net.minecraft.registry.entry.RegistryEntry.of(net.minecraft.sound.SoundEvent.of(sound));
            player.networkHandler.sendPacket(new net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket(soundEvent, soundCategory, pos.x, pos.y, pos.z, volume, 1, world.getRandom().nextLong()));
        #else
            player.networkHandler.sendPacket(new net.minecraft.network.packet.s2c.play.PlaySoundIdS2CPacket(sound, soundCategory, pos, volume, 1, world.getRandom().nextLong()));
        #endif
    }

    public static void sendFeedback(CommandContext<ServerCommandSource> context, MutableText text, boolean broadcastToOps) {
        #if MC_VERSION >= "12000"
            context.getSource().sendFeedback(() -> text, broadcastToOps);
        #else
            context.getSource().sendFeedback(text, broadcastToOps);
        #endif
    }
}
