package com.lx862.svrutil.commands;

import com.lx862.svrutil.Commands;
import com.lx862.svrutil.Mappings;
import com.lx862.svrutil.config.CommandConfig;
import com.lx862.svrutil.data.CommandEntry;
import com.lx862.svrutil.SvrUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;

public class msg {
    private static final CommandEntry defaultEntry = new CommandEntry("msg", 0, true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if(!entry.enabled) return;

        // HACKS: Remove vanilla msg command, not really happy with it but whatever.
        if(entry.commandName.equals("msg")) {
            dispatcher.getRoot().getChildren().removeIf(node -> node.getName().equals("msg"));
        }

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(ctx -> ctx.hasPermissionLevel(entry.permLevel))
                .then(CommandManager.argument("target", EntityArgumentType.player())
                        .then(CommandManager.argument("message", StringArgumentType.greedyString())
                                .executes(context -> {
                                    String playerName = context.getSource().getName();
                                    ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
                                    String message = StringArgumentType.getString(context, "message");
                                    Mappings.sendFeedback(context, Mappings.literalText(String.format("§6[me §r-> §6%s]: §r%s", target.getGameProfile().getName(), message)), false);
                                    target.sendMessage(Mappings.literalText(String.format("§6[%s §r-> §6me]: §r%s", playerName, message)), false);
                                    SvrUtil.lastReply.put(target.getGameProfile().getName(), playerName);
                                    SvrUtil.lastReply.put(playerName, target.getGameProfile().getName());
                                    target.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1, 1);

                                    if(afk.afkList.containsKey(target.getUuid()) && context.getSource().isExecutedByPlayer()) {
                                        context.getSource().getPlayer().sendMessageToClient(Mappings.literalText("").append(target.getDisplayName()).append(" are AFK and may not be available at the moment.").formatted(Formatting.YELLOW), true);
                                    }

                                    Commands.finishedExecution(context, defaultEntry);
                                    return 1;
                                }))
                )
        );
    }
}
