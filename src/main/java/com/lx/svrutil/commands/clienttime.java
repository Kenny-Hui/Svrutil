package com.lx.svrutil.commands;

import com.lx.svrutil.Commands;
import com.lx.svrutil.SvrUtil;
import com.lx.svrutil.Mappings;
import com.lx.svrutil.config.CommandConfig;
import com.lx.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.TimeArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;

import java.util.UUID;

public class clienttime {
    private static final CommandEntry defaultEntry = new CommandEntry("clienttime", 0, true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if(!entry.enabled) return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(ctx -> ctx.hasPermissionLevel(entry.permLevel))
                .then(CommandManager.argument("time", TimeArgumentType.time())
                        .executes(context -> execute(context, IntegerArgumentType.getInteger(context, "time"), false))
                )
                .then(CommandManager.literal("day")
                        .executes(context -> execute(context, 1000, false))
                )
                .then(CommandManager.literal("noon")
                        .executes(context -> execute(context, 6000, false))
                )
                .then(CommandManager.literal("night")
                        .executes(context -> execute(context, 13000, false))
                )
                .then(CommandManager.literal("midnight")
                        .executes(context -> execute(context, 18000, false))
                )
                .then(CommandManager.literal("reset")
                        .executes(context -> execute(context, 0, true))
                )
        );
    }

    private static int execute(CommandContext<ServerCommandSource> context, int time, boolean reset) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        UUID playerUuid = player.getUuid();
        if(reset) {
            SvrUtil.fakeTimeList.remove(playerUuid);
            Mappings.sendFeedback(context, Mappings.literalText("Client time has been reset.").formatted(Formatting.GREEN), false);
        } else {
            SvrUtil.fakeTimeList.put(playerUuid, (long)time);
            Mappings.sendFeedback(context, Mappings.literalText("Client time set to " + time).formatted(Formatting.GREEN), false);
        }
        Commands.finishedExecution(context, defaultEntry);
        return 1;
    }
}