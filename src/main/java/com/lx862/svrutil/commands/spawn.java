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
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

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
        BlockPos spawnPoint = null;
        if(personal) {
            spawnPoint = player.getSpawnPointPosition();
        }
        if(spawnPoint == null) {
            spawnPoint = context.getSource().getWorld().getSpawnPos();
        }

        player.requestTeleportAndDismount(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ());
        context.getSource().sendFeedback(() -> Text.literal("Teleported back to spawn.").formatted(Formatting.GREEN), false);
        Commands.finishedExecution(context, defaultEntry);
        return 1;
    }
}
