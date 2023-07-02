package com.lx.svrutil.commands;

import com.lx.svrutil.Commands;
import com.lx.svrutil.config.CommandConfig;
import com.lx.svrutil.data.CommandEntry;
import com.lx.svrutil.transition.TransitionConfig;
import com.lx.svrutil.transition.data.TransitioningData;
import com.lx.svrutil.Mappings;
import com.lx.svrutil.transition.TransitionManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class transition {
    private static final CommandEntry defaultEntry = new CommandEntry("transition", 2, true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if(!entry.enabled) return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(ctx -> ctx.hasPermissionLevel(entry.permLevel))
                .then(CommandManager.argument("target", EntityArgumentType.player())
                        .then(CommandManager.argument("transitionName", StringArgumentType.greedyString())
                                .suggests((context, suggestionsBuilder) -> CommandSource.suggestMatching(TransitionConfig.transitions.keySet(), suggestionsBuilder))
                                .executes(context -> {
                                    ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
                                    String transitionName = StringArgumentType.getString(context, "transitionName");
                                    if(!TransitionConfig.transitions.containsKey(transitionName)) {
                                        Mappings.sendFeedback(context, Mappings.literalText("Cannot find transition id: " + transitionName).formatted(Formatting.RED), false);
                                        return 0;
                                    }
                                    TransitioningData data = new TransitioningData(transitionName, target.getPos(), new Vec3d(0, 0, 0));
                                    data.cameraEntity = spawnAreaEffectCloud(target, context.getSource().getWorld());
                                    TransitionManager.playerInTransition.put(target, data);
                                    Mappings.sendFeedback(context, Mappings.literalText("Action!").formatted(Formatting.GREEN), false);
                                    Commands.finishedExecution(context, defaultEntry);
                                    return 1;
                                })
                        )
                )
        );
    }

    public static Entity spawnAreaEffectCloud(ServerPlayerEntity player, World world) {
        ArmorStandEntity entity = EntityType.ARMOR_STAND.create(world);
        entity.setInvisible(true);
        entity.setNoGravity(true);
        entity.setInvulnerable(true);
        world.spawnEntity(entity);
        return entity;
    }
}
