package com.lx862.svrutil.commands;

import com.lx862.svrutil.Commands;
import com.lx862.svrutil.config.CommandConfig;
import com.lx862.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public class clientweather {
    private static final CommandEntry defaultEntry = new CommandEntry("clientweather", 0, true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if(!entry.enabled) return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(ctx -> ctx.hasPermissionLevel(entry.permLevel))
                .then(CommandManager.literal("rain")
                .executes(context -> {
                    execute(context, true, false, false, false);
                    return 1;
                }))
                .then(CommandManager.literal("thunder")
                        .executes(context -> {
                            execute(context, true, true, false, false);
                            return 1;
                        })
                )
                .then(CommandManager.literal("clear")
                        .executes(context -> {
                            execute(context, false, false, true, false);
                            return 1;
                        })
                )
                .then(CommandManager.literal("reset")
                        .executes(context -> {
                            execute(context, false, false, false, true);
                            return 1;
                        })
                )
        );
    }

    private static void execute(CommandContext<ServerCommandSource> context, boolean isRaining, boolean isThundering, boolean isClear, boolean isReset) throws CommandSyntaxException {
        ServerWorld world = context.getSource().getWorld();
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();

        if(isReset) {
            resetWeather(player, world);
            player.sendMessage(Text.literal("Client weather reset, now following server's weather.").formatted(Formatting.GREEN), false);
        } else {
            if(isRaining) {
                player.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.RAIN_STOPPED, 1));
                player.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.RAIN_GRADIENT_CHANGED, 1));
            }

            if(isThundering) {
                player.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.THUNDER_GRADIENT_CHANGED, 1));
            } else {
                player.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.THUNDER_GRADIENT_CHANGED, 0));
            }

            if(isClear) {
                player.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.RAIN_STARTED, 0));
                player.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.THUNDER_GRADIENT_CHANGED, 0));
            }

            player.sendMessage(Text.literal("Weather changed.").formatted(Formatting.GREEN), false);
            player.sendMessage(Text.literal("Note: You will have to re-run this command again if the weather of the server changes.").formatted(Formatting.GREEN), false);
        }

        Commands.finishedExecution(context, defaultEntry);
    }

    private static void resetWeather(ServerPlayerEntity target, World world) {
        boolean serverIsRaining = world.isRaining();
        if(serverIsRaining) {
            target.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.RAIN_STOPPED, 0));
        } else {
            target.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.RAIN_STARTED, 0));
        }

        target.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.THUNDER_GRADIENT_CHANGED, world.getThunderGradient(1.0F)));
    }
}
