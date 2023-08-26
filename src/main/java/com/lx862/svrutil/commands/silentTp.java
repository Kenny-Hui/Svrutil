package com.lx862.svrutil.commands;

import com.lx862.svrutil.Commands;
import com.lx862.svrutil.config.CommandConfig;
import com.lx862.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class silentTp {
    private static final CommandEntry defaultEntry = new CommandEntry("silentTp", 2, true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if(!entry.enabled) return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(ctx -> ctx.hasPermissionLevel(entry.permLevel))
                .then(CommandManager.argument("target", EntityArgumentType.player())
                .executes(context -> {
                    ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
                    Vec3d targetPos = target.getPos();
                    World targetWorld = target.getWorld();
                    context.getSource().getPlayerOrThrow().teleport((ServerWorld) targetWorld, targetPos.getX(), targetPos.getY(), targetPos.getZ(), 0.0f, 0.0f);
                    Commands.finishedExecution(context, defaultEntry);
                    return 1;
                })));
    }
}
