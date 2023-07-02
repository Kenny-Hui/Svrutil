package com.lx.svrutil.commands;

import com.lx.svrutil.Commands;
import com.lx.svrutil.config.CommandConfig;
import com.lx.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class lightblock {
    private static final CommandEntry defaultEntry = new CommandEntry("lightblock", 2, true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if(!entry.enabled) return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(ctx -> ctx.hasPermissionLevel(entry.permLevel))
                        .then(CommandManager.argument("level", IntegerArgumentType.integer(0, 15))
                                .executes(context -> execute(context, IntegerArgumentType.getInteger(context, "level"))))
                .executes(context -> execute(context, 15))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> context, int level) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        ItemStack stack = new ItemStack(Items.LIGHT);
        NbtCompound blockStateTag = new NbtCompound();
        blockStateTag.putInt("level", level);
        stack.getOrCreateNbt().put("BlockStateTag", blockStateTag);
        player.giveItemStack(stack);
        Commands.finishedExecution(context, defaultEntry);
        return 1;
    }
}
