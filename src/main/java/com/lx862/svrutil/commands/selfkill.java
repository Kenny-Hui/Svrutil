package com.lx862.svrutil.commands;

import com.lx862.svrutil.Commands;
import com.lx862.svrutil.config.CommandConfig;
import com.lx862.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
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

                    context.getSource().getServer().getPlayerManager().broadcast(Text.literal(player.getDisplayName().getString() + " took their own life.").formatted(Formatting.RED), false);
                    Commands.finishedExecution(context, defaultEntry);
                    return 1;
                })
        );
    }
}
