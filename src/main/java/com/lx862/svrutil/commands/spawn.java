package com.lx862.svrutil.commands;

import com.lx862.svrutil.Commands;
import com.lx862.svrutil.config.CommandConfig;
import com.lx862.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class spawn {
    private static final CommandEntry defaultEntry = new CommandEntry("spawn", 0, true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if(!entry.enabled) return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(ctx -> ctx.hasPermissionLevel(entry.permLevel))
                        .then(CommandManager.literal("personal")
                                .executes(context -> execute(context, context.getSource().getPlayerOrThrow(), true))
                        )
                .executes(context -> execute(context, context.getSource().getPlayerOrThrow(), false))
        );
    }

    public static int execute(CommandContext<ServerCommandSource> context, ServerPlayerEntity player, boolean personal) throws CommandSyntaxException {
        final ServerWorld dimension;
        final BlockPos spawnPoint;
        if(personal) {
            spawnPoint = player.getSpawnPointPosition();
            dimension = context.getSource().getServer().getWorld(player.getSpawnPointDimension());
        } else {
            spawnPoint = context.getSource().getWorld().getSpawnPos();
            dimension = context.getSource().getWorld();
        }

        player.dismountVehicle();
        player.requestTeleport(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ());
        player.requestTeleportAndDismount(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ());
        context.getSource().sendFeedback(Text.literal("Teleported back to spawn.").formatted(Formatting.GREEN), false);
        Commands.finishedExecution(context, defaultEntry);
        return 1;
    }
}
