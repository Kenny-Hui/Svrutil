package com.lx862.svrutil;

import com.lx862.svrutil.config.CommandConfig;
import com.lx862.svrutil.data.CommandEntry;
import com.lx862.svrutil.commands.*;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;

public class Commands {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        AfkCommand.register(dispatcher, registryAccess);
        BarrierBlockCommand.register(dispatcher, registryAccess);
        ClientTimeCommand.register(dispatcher, registryAccess);
        ClientWeatherCommand.register(dispatcher, registryAccess);
        CmdBlockCommand.register(dispatcher, registryAccess);
        FancyKickCommand.register(dispatcher, registryAccess);
        FeedCommand.register(dispatcher, registryAccess);
        FlyCommand.register(dispatcher, registryAccess);
        GmaCommand.register(dispatcher, registryAccess);
        GmcCommand.register(dispatcher, registryAccess);
        GmsCommand.register(dispatcher, registryAccess);
        GmspCommand.register(dispatcher, registryAccess);
        HealCommand.register(dispatcher, registryAccess);
        PlaySoundAreaCommand.register(dispatcher, registryAccess);
        RootCommand.register(dispatcher, registryAccess);
        LightBlockCommand.register(dispatcher, registryAccess);
        OpLevelCommand.register(dispatcher, registryAccess);
        NetherCommand.register(dispatcher, registryAccess);
        OverworldCommand.register(dispatcher, registryAccess);
        RCommand.register(dispatcher, registryAccess);
        SelfKillCommand.register(dispatcher, registryAccess);
        SpawnCommand.register(dispatcher, registryAccess);
        SpdCommand.register(dispatcher, registryAccess);
        SpectateCommand.register(dispatcher, registryAccess);
        SilentTpCommand.register(dispatcher, registryAccess);
        TheEndCommand.register(dispatcher, registryAccess);
        UnspectateCommand.register(dispatcher, registryAccess);
        WhereCommand.register(dispatcher, registryAccess);
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
