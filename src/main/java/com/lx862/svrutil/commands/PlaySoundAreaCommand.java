package com.lx862.svrutil.commands;

import com.lx862.svrutil.config.CommandConfig;
import com.lx862.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;

import java.util.List;

public class PlaySoundAreaCommand {
    private static final CommandEntry defaultEntry = new CommandEntry("playsoundarea", 2, true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if(!entry.enabled) return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(ctx -> ctx.hasPermissionLevel(entry.permLevel))
                .then(CommandManager.argument("pos1", Vec3ArgumentType.vec3())
                .then(CommandManager.argument("pos2", Vec3ArgumentType.vec3())
                .then(CommandManager.argument("sound", IdentifierArgumentType.identifier()).suggests(SuggestionProviders.AVAILABLE_SOUNDS)
                .then(CommandManager.argument("volume", FloatArgumentType.floatArg(0.0F))
                    .executes((ctx) -> execute(ctx, FloatArgumentType.getFloat(ctx, "volume"), 1))
                .then(CommandManager.argument("pitch", FloatArgumentType.floatArg(0.0F, 2.0F))
                    .executes((ctx) -> execute(ctx, FloatArgumentType.getFloat(ctx, "volume"), FloatArgumentType.getFloat(ctx, "pitch")))
                )))))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> context, float volume, float pitch) {
        Box AABB = new Box(Vec3ArgumentType.getVec3(context, "pos1"), Vec3ArgumentType.getVec3(context, "pos2"));
        List<ServerPlayerEntity> playersInRange = context.getSource().getWorld().getEntitiesByClass(ServerPlayerEntity.class, AABB, e -> true);
        Identifier sound = IdentifierArgumentType.getIdentifier(context, "sound");

        for (ServerPlayerEntity player : playersInRange) {
            player.networkHandler.sendPacket(new PlaySoundS2CPacket(RegistryEntry.of(SoundEvent.of(sound)), SoundCategory.MASTER, player.getPos().x, player.getPos().y, player.getPos().z, volume, pitch, player.getWorld().getRandom().nextLong()));
        }
        return 1;
    }
}
