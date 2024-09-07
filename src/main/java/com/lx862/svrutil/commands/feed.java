package com.lx862.svrutil.commands;

import com.lx862.svrutil.Commands;
import com.lx862.svrutil.config.CommandConfig;
import com.lx862.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class feed {
    private static final CommandEntry defaultEntry = new CommandEntry("feed", 2, true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if(!entry.enabled) return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(ctx -> ctx.hasPermissionLevel(entry.permLevel))
                        .executes(context -> {
                            int result = execute(context.getSource().getPlayerOrThrow());
                            if(result > 0) Commands.finishedExecution(context, defaultEntry);
                            return result;
                        })
                .then(CommandManager.argument("target", EntityArgumentType.player())
                .executes(context -> {
                    int result = execute(EntityArgumentType.getPlayer(context, "target"));
                    if(result > 0) Commands.finishedExecution(context, defaultEntry);
                    return result;
                }))
        );
    }

    public static int execute(ServerPlayerEntity target) {
        target.getHungerManager().setFoodLevel(20);
        target.getHungerManager().setSaturationLevel(20);
        target.sendMessage(Text.literal("Fed").formatted(Formatting.GREEN), false);
        return 1;
    }
}
