package com.lx862.svrutil.commands;

import com.lx862.svrutil.Commands;
import com.lx862.svrutil.config.CommandConfig;
import com.lx862.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NetherCommand {
    private static final CommandEntry defaultEntry = new CommandEntry("nether", 2, true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if(!entry.enabled) return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(ctx -> ctx.hasPermissionLevel(entry.permLevel))
                .executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                    ServerWorld theNether = context.getSource().getServer().getWorld(World.NETHER);
                    BlockPos spawnPoint = theNether.getSpawnPos();
                    player.teleport(theNether, spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ(), player.getYaw(), player.getPitch());
                    context.getSource().sendFeedback(() -> Text.literal("Teleported to the nether.").formatted(Formatting.GREEN), false);
                    Commands.finishedExecution(context, defaultEntry);
                    return 1;
                })
        );
    }
}
