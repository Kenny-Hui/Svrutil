package com.lx862.svrutil.commands;

import com.lx862.svrutil.Commands;
import com.lx862.svrutil.Mappings;
import com.lx862.svrutil.config.CommandConfig;
import com.lx862.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class where {
    private static final CommandEntry defaultEntry = new CommandEntry("where", 2, true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if(!entry.enabled) return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(ctx -> ctx.hasPermissionLevel(entry.permLevel))
                .then(CommandManager.argument("target", EntityArgumentType.entity())
                    .executes(context -> {
                        Entity target = EntityArgumentType.getEntity(context, "target");
                        String coords = Math.round(target.getX()) + " " + Math.round(target.getY()) + " " + Math.round(target.getZ());
                        ClickEvent tpClickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + coords);
                        HoverEvent tpHoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Mappings.literalText("Click to teleport").formatted(Formatting.GREEN));
                        Text coordsText = Text.literal(coords).formatted(Formatting.GOLD).styled(style -> style.withClickEvent(tpClickEvent).withHoverEvent(tpHoverEvent));
                        Text distText = Mappings.literalText("");
                        if(context.getSource().isExecutedByPlayer()) {
                            distText = Mappings.literalText(" (" + Math.round(target.getPos().distanceTo(context.getSource().getPlayerOrThrow().getPos())) + "m away)").formatted(Formatting.GREEN);
                        }

                        Mappings.sendFeedback(context, Mappings.literalText("").append(target.getDisplayName()).append(" is at ").append(coordsText).append(distText), false);
                        Commands.finishedExecution(context, defaultEntry);
                        return 1;
                    })
                )
        );
    }
}
