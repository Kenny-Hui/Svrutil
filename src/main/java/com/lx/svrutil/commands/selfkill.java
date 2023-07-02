package com.lx.svrutil.commands;

import com.lx.svrutil.Commands;
import com.lx.svrutil.Mappings;
import com.lx.svrutil.config.CommandConfig;
import com.lx.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;

public class selfkill {
    private static final CommandEntry defaultEntry = new CommandEntry("suicide", 0, true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if(!entry.enabled) return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(ctx -> ctx.hasPermissionLevel(entry.permLevel))
                .executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                    player.kill();
                    Mappings.broadcast(context.getSource().getServer().getPlayerManager(), player, Mappings.literalText(player.getDisplayName().getString() + " took their own life.").formatted(Formatting.RED));
                    Commands.finishedExecution(context, defaultEntry);
                    return 1;
                })
        );
    }
}
