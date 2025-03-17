package com.lx862.svrutil.commands;

import com.lx862.svrutil.Commands;
import com.lx862.svrutil.config.CommandConfig;
import com.lx862.svrutil.data.CommandEntry;
import com.lx862.svrutil.SvrUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Collection;

public class RCommand {
    private static final CommandEntry defaultEntry = new CommandEntry("r", 0, true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if(!entry.enabled) return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(ctx -> ctx.hasPermissionLevel(entry.permLevel))
                .then(CommandManager.argument("message", StringArgumentType.greedyString())
                        .executes(context -> {
                            String player = context.getSource().getName();
                            Collection<String> targets = SvrUtil.lastReply.get(player);
                            if(targets == null || targets.isEmpty()) {
                                context.getSource().sendFeedback(() -> Text.literal("Either no one have replied to you, or the player is offline.").formatted(Formatting.GRAY), false);
                                return 1;
                            }

                            for(String targetName : targets) {
                                ServerPlayerEntity target = context.getSource().getServer().getPlayerManager().getPlayer(targetName);
                                if (context.getSource().getWorld().getPlayerByUuid(target.getUuid()) == null) {
                                    context.getSource().sendFeedback(() -> Text.literal(String.format("Cannot reply as %s is now offline")).formatted(Formatting.RED), false);
                                    continue;
                                }

                                String message = StringArgumentType.getString(context, "message");
                                context.getSource().getServer().getCommandManager().executeWithPrefix(context.getSource().getPlayer().getCommandSource(), String.format("msg %s %s", target.getGameProfile().getName(), message));
                                Commands.finishedExecution(context, defaultEntry);
                            }

                            return 1;
                        })));
    }
}
