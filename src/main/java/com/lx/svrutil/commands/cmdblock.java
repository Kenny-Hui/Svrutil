package com.lx.svrutil.commands;

import com.lx.svrutil.Commands;
import com.lx.svrutil.config.CommandConfig;
import com.lx.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class cmdblock {
    private static final CommandEntry defaultEntry = new CommandEntry("cmdblock", 2, true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if(!entry.enabled) return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(ctx -> ctx.hasPermissionLevel(entry.permLevel))
                .executes(context -> {
                    context.getSource().getPlayerOrThrow().giveItemStack(new ItemStack(Items.COMMAND_BLOCK));
                    Commands.finishedExecution(context, defaultEntry);
                    return 1;
                })
        );
    }
}
