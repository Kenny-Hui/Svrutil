package com.lx862.svrutil.commands;

import com.lx862.svrutil.Commands;
import com.lx862.svrutil.config.CommandConfig;
import com.lx862.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.SetCameraEntityS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SpectateCommand {
    private static final CommandEntry defaultEntry = new CommandEntry("spectate", 2, true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if(!entry.enabled) return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(ctx -> ctx.hasPermissionLevel(entry.permLevel))
                        .then(CommandManager.argument("target", EntityArgumentType.entity())
                                .executes(context -> {
                                    Entity target = EntityArgumentType.getEntity(context, "target");
                                    int result = execute(context, context.getSource().getPlayerOrThrow(), target);
                                    if(result > 0) Commands.finishedExecution(context, defaultEntry);
                                    return result;
                                })
                        )
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .then(CommandManager.argument("target", EntityArgumentType.entity())
                        .executes(context -> {
                            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
                            Entity target = EntityArgumentType.getEntity(context, "target");
                            int result = execute(context, player, target);
                            Commands.finishedExecution(context, defaultEntry);
                            return result;
                        }))
                ));
    }

    private static int execute(CommandContext<ServerCommandSource> context, ServerPlayerEntity player, Entity target) {
        player.networkHandler.sendPacket(new SetCameraEntityS2CPacket(target));
        context.getSource().sendFeedback(() -> Text.literal("Now spectating ").append(target.getDisplayName()).formatted(Formatting.GREEN), false);
        return 1;
    }
}
