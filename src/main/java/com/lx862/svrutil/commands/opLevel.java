package com.lx862.svrutil.commands;

import com.lx862.svrutil.Commands;
import com.lx862.svrutil.Mappings;
import com.lx862.svrutil.Util;
import com.lx862.svrutil.config.CommandConfig;
import com.lx862.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;

public class opLevel {
    private static final CommandEntry defaultEntry = new CommandEntry("opLevel", 2, true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if(!entry.enabled) return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(ctx -> ctx.hasPermissionLevel(entry.permLevel))
                .then(CommandManager.argument("target", EntityArgumentType.player())
                .executes(context -> {
                    ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
                    int level = Util.getPermLevel(target);

                    String levelString;
                    if(level == 0) {
                        levelString = "0 (None)";
                    } else {
                        levelString = String.valueOf(level);
                    }

                    Mappings.sendFeedback(context, Mappings.literalText("Target have OP level " + levelString).formatted(Formatting.GOLD), false);
                    Commands.finishedExecution(context, defaultEntry);
                    return 1;
                }))
        );
    }
}
