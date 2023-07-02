package com.lx.svrutil.commands;

import com.lx.svrutil.Commands;
import com.lx.svrutil.Mappings;
import com.lx.svrutil.config.CommandConfig;
import com.lx.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
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
                .executes(context -> execute(context, context.getSource().getPlayer()))
        );
    }

    public static int execute(CommandContext<ServerCommandSource> context, ServerPlayerEntity player) {
        try {
            BlockPos spawnPoint = context.getSource().getWorld().getSpawnPos();

            /* TP player back to spawn */
            player.requestTeleportAndDismount(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ());
            player.sendMessage(Mappings.literalText("Teleported back to spawn.").formatted(Formatting.GREEN), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Commands.finishedExecution(context, defaultEntry);
        return 1;
    }
}
