package com.lx862.svrutil.commands;

import com.lx862.svrutil.Commands;
import com.lx862.svrutil.ModInfo;
import com.lx862.svrutil.config.CommandConfig;
import com.lx862.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.UUID;

public class spd {
    private static final float DEFAULT_FLY_SPEED = 0.05F;
    private static final UUID walkSpeedUUID = UUID.fromString("a1c7649f-402b-4c75-832b-3e3f418a6886");
    private static final CommandEntry defaultEntry = new CommandEntry("spd", 2, true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if(!entry.enabled) return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(ctx -> ctx.hasPermissionLevel(entry.permLevel))
                .executes(context -> execute(context, null))
                .then(CommandManager.argument("speed", DoubleArgumentType.doubleArg(0))
                        .executes(context -> execute(context, DoubleArgumentType.getDouble(context, "speed")))
        ));
    }

    private static int execute(CommandContext<ServerCommandSource> context, Double speedFactor) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        EntityAttributeInstance entityAttributeInstance = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if(entityAttributeInstance == null) return 1;

        if(speedFactor == null) {
            entityAttributeInstance.removeModifier(walkSpeedUUID);
            player.getAbilities().setFlySpeed(DEFAULT_FLY_SPEED);
            context.getSource().sendFeedback(Text.literal("Walking and flying speed has been reset").formatted(Formatting.GREEN), false);
        } else {
            EntityAttributeModifier entityAttributeModifier = new EntityAttributeModifier(walkSpeedUUID, ModInfo.MOD_NAME + " Speed Modifier", speedFactor, EntityAttributeModifier.Operation.MULTIPLY_BASE);
            player.getAbilities().setFlySpeed((float)(DEFAULT_FLY_SPEED * speedFactor));

            if(entityAttributeInstance.hasModifier(entityAttributeModifier)) {
                entityAttributeInstance.removeModifier(walkSpeedUUID);
            }

            entityAttributeInstance.addTemporaryModifier(entityAttributeModifier);
            context.getSource().sendFeedback(Text.literal("Walking and flying speed set to " + speedFactor + "x").formatted(Formatting.GREEN), false);
        }

        player.sendAbilitiesUpdate();
        Commands.finishedExecution(context, defaultEntry);
        return 1;
    }
}
