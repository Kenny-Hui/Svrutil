package com.lx862.svrutil;

import com.lx862.svrutil.config.CommandConfig;
import com.lx862.svrutil.data.CommandEntry;
import com.lx862.svrutil.commands.*;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.ServerCommandSource;

public class Commands {

    public static void register(CommandDispatcher<net.minecraft.server.command.ServerCommandSource> dispatcher) {
        afk.register(dispatcher);
        barrierblock.register(dispatcher);
        clienttime.register(dispatcher);
        clientweather.register(dispatcher);
        cmdblock.register(dispatcher);
        fancyKick.register(dispatcher);
        feed.register(dispatcher);
        fly.register(dispatcher);
        gma.register(dispatcher);
        gmc.register(dispatcher);
        gms.register(dispatcher);
        gmsp.register(dispatcher);
        heal.register(dispatcher);
        playsoundarea.register(dispatcher);
        rootCommand.register(dispatcher);
        lightblock.register(dispatcher);
        msg.register(dispatcher);
        opLevel.register(dispatcher);
        nether.register(dispatcher);
        overworld.register(dispatcher);
        r.register(dispatcher);
        selfkill.register(dispatcher);
        spawn.register(dispatcher);
        spd.register(dispatcher);
        spectate.register(dispatcher);
        silentTp.register(dispatcher);
        silentKick.register(dispatcher);
        theend.register(dispatcher);
        if(FabricLoader.getInstance().isDevelopmentEnvironment()) transition.register(dispatcher);
        unspectate.register(dispatcher);
        where.register(dispatcher);
    }

    public static void finishedExecution(CommandContext<ServerCommandSource> context, CommandEntry defaultEntry) {
        CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if(entry != null) {
            if(context.getSource().isExecutedByPlayer()) {
                for(String playerCommand : entry.chainedPlayerCommand) {
                    context.getSource().getServer().getCommandManager().executeWithPrefix(context.getSource().getPlayer().getCommandSource(), playerCommand);
                }
            }

            for(String serverCommand : entry.chainedServerCommand) {
                String finalServerCommand = context.getSource().isExecutedByPlayer() ? serverCommand.replace("{playerName}", context.getSource().getPlayer().getGameProfile().getName()) : serverCommand;
                context.getSource().getServer().getCommandManager().executeWithPrefix(context.getSource().getServer().getCommandSource().withSilent(), finalServerCommand);
            }
        }
    }
}
