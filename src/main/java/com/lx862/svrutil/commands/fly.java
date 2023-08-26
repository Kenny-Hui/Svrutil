package com.lx862.svrutil.commands;

import com.lx862.svrutil.Commands;
import com.lx862.svrutil.Mappings;
import com.lx862.svrutil.config.CommandConfig;
import com.lx862.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;

public class fly {
    private static final CommandEntry defaultEntry = new CommandEntry("fly", 2, true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if(!entry.enabled) return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(ctx -> ctx.hasPermissionLevel(entry.permLevel))
                .executes(context -> execute(context, context.getSource().getPlayerOrThrow()))
                .then(CommandManager.argument("target", EntityArgumentType.player())
                .executes(context -> execute(context, EntityArgumentType.getPlayer(context, "target"))))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> context, ServerPlayerEntity player) {
        if (player.getAbilities().allowFlying) {
            player.getAbilities().allowFlying = false;
            player.getAbilities().flying = false;
            player.sendMessage(Mappings.literalText("You can no longer fly").formatted(Formatting.RED), false);
        } else {
            player.getAbilities().allowFlying = true;
            player.sendMessage(Mappings.literalText("You can now fly").formatted(Formatting.GREEN), false);
        }
        player.sendAbilitiesUpdate();
        Commands.finishedExecution(context, defaultEntry);
        return 1;
    }
}
