package com.lx862.svrutil.commands;

import com.lx862.svrutil.Commands;
import com.lx862.svrutil.config.CommandConfig;
import com.lx862.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;

public class gma {
    private static final CommandEntry defaultEntry = new CommandEntry("gma", 2, true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if(!entry.enabled) return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(ctx -> ctx.hasPermissionLevel(entry.permLevel))
                .executes(context -> {
                    Text gamemodeText = Text.literal("Adventure").formatted(Formatting.GOLD);
                    context.getSource().getPlayerOrThrow().changeGameMode(GameMode.ADVENTURE);
                    context.getSource().getPlayerOrThrow().sendMessage(Text.literal("Gamemode: ").append(gamemodeText), false);
                    Commands.finishedExecution(context, defaultEntry);
                    return 1;
                }));
    }
}
