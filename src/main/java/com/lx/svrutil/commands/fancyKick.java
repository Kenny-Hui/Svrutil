package com.lx.svrutil.commands;

import com.lx.svrutil.Commands;
import com.lx.svrutil.Mappings;
import com.lx.svrutil.config.CommandConfig;
import com.lx.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;

public class fancyKick {
    private static final CommandEntry defaultEntry = new CommandEntry("fancykick", 4, true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if(!entry.enabled) return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(ctx -> ctx.hasPermissionLevel(entry.permLevel))
                .then(CommandManager.argument("target", EntityArgumentType.players())
                        .then(CommandManager.argument("reason", TextArgumentType.text())
                            .executes(context -> {
                                Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context, "target");
                                Text text = TextArgumentType.getTextArgument(context, "reason");

                                for(ServerPlayerEntity player : targets) {
                                    player.networkHandler.disconnect(text);
                                    Mappings.sendFeedback(context, Text.translatable("commands.kick.success", player.getDisplayName(), text), true);
                                }

                                Commands.finishedExecution(context, defaultEntry);
                                return 1;
                            })
                        )
                )
        );
    }
}
