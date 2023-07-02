package com.lx.svrutil.commands;

import com.lx.svrutil.Commands;
import com.lx.svrutil.config.CommandConfig;
import com.lx.svrutil.config.MainConfig;
import com.lx.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class silentKick {
    private static final CommandEntry defaultEntry = new CommandEntry("silentkick", 4, true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if(!entry.enabled) return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(ctx -> ctx.hasPermissionLevel(entry.permLevel))
                .then(CommandManager.argument("target", EntityArgumentType.player())
                .executes(context -> {
                    ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
                    target.networkHandler.disconnect(MainConfig.getSilentKickMessage());
                    Commands.finishedExecution(context, defaultEntry);
                    return 1;
                }))
        );
    }
}
