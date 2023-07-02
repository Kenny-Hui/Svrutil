package com.lx.svrutil.commands;

import com.lx.svrutil.Commands;
import com.lx.svrutil.Mappings;
import com.lx.svrutil.config.CommandConfig;
import com.lx.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;

public class heal {
    private static final CommandEntry defaultEntry = new CommandEntry("heal", 2, true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if(!entry.enabled) return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(ctx -> ctx.hasPermissionLevel(entry.permLevel))
                .executes(context -> execute(context, context.getSource().getPlayerOrThrow()))
                .then(CommandManager.argument("target", EntityArgumentType.player())
                    .executes(context -> execute(context, EntityArgumentType.getPlayer(context, "target")))
                )
        );
    }

    public static int execute(CommandContext<ServerCommandSource> context, ServerPlayerEntity target) {
        target.setHealth(20);
        target.getHungerManager().setFoodLevel(20);
        target.getHungerManager().setSaturationLevel(20);
        target.sendMessage(Mappings.literalText("Healed!").formatted(Formatting.GREEN), false);
        Commands.finishedExecution(context, defaultEntry);
        return 1;
    }
}
