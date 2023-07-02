package com.lx.svrutil.commands;

import com.lx.svrutil.Commands;
import com.lx.svrutil.Mappings;
import com.lx.svrutil.config.CommandConfig;
import com.lx.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.UUID;

public class afk {
    private static final CommandEntry defaultEntry = new CommandEntry("afk", 0, true);
    public static final HashMap<UUID, String> afkList = new HashMap<>();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);

        if(!entry.enabled) return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(ctx -> ctx.hasPermissionLevel(entry.permLevel))
                .executes(context -> execute(context, null))
                .then(CommandManager.argument("reason", StringArgumentType.greedyString())
                        .executes(context -> execute(context, StringArgumentType.getString(context, "reason")))
                )
        );
    }

    private static int execute(CommandContext<ServerCommandSource> context, String reason) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if(player == null) return 1;
        Text playerName = player.getDisplayName();

        MutableText finalMessage = Mappings.literalText("* ").append(playerName);
        if(afkList.containsKey(player.getUuid())) {
            finalMessage = finalMessage.append(" is no longer AFK");
            afkList.remove(player.getUuid());
        } else {
            finalMessage.append(" has marked themselves as AFK");
            afkList.put(player.getUuid(), reason);
        }

        if(reason != null) {
            finalMessage = finalMessage.append(" due to \"" + reason + "\"");
        }

        finalMessage.append(".");

        context.getSource().getServer().getPlayerManager().broadcast(finalMessage.formatted(Formatting.GRAY).formatted(Formatting.ITALIC), false);
        Commands.finishedExecution(context, defaultEntry);
        return 1;
    }
}
