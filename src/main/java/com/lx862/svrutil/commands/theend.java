package com.lx862.svrutil.commands;

import com.lx862.svrutil.Commands;
import com.lx862.svrutil.config.CommandConfig;
import com.lx862.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

public class theend {
    private static final CommandEntry defaultEntry = new CommandEntry("theend", 2, true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if(!entry.enabled) return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(ctx -> ctx.hasPermissionLevel(entry.permLevel))
                .executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                    ServerWorld theEnd = context.getSource().getServer().getWorld(World.END);
                    BlockPos spawnPoint = theEnd.getSpawnPos();
                    /* TP player to the nether.
                    I don't know what magic this FabricDimension thing does, but using vanilla teleport could screw up everything horribly */
                    FabricDimensions.teleport(player, theEnd, new TeleportTarget(new Vec3d(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ()), player.getVelocity(), player.getYaw(), player.getPitch()));
                    context.getSource().sendFeedback(Text.literal("Teleported to the end.").formatted(Formatting.GREEN), false);
                    Commands.finishedExecution(context, defaultEntry);
                    return 1;
                })
        );
    }
}
