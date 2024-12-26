package com.lx862.svrutil.commands;

import com.lx862.svrutil.Commands;
import com.lx862.svrutil.ModInfo;
import com.lx862.svrutil.config.CommandConfig;
import com.lx862.svrutil.config.Config;
import com.lx862.svrutil.data.CommandEntry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class rootCommand {
    private static final CommandEntry defaultEntry = new CommandEntry("svrutil", 2, true);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        final CommandEntry entry = CommandConfig.getCommandEntry(defaultEntry);
        if(!entry.enabled) return;

        dispatcher.register(CommandManager.literal(entry.commandName)
                .requires(ctx -> ctx.hasPermissionLevel(entry.permLevel))
                .executes(rootCommand::executeAbout)
                .then(CommandManager.literal("reload")
                    .executes(rootCommand::executeReload)
                )
        );
    }

    private static int executeAbout(CommandContext<ServerCommandSource> context) {
        String homepageUrl;
        try {
            // Terrible but it works
            homepageUrl = FabricLoader.getInstance().getModContainer(ModInfo.MOD_ID).get().getMetadata().getContact().get("homepage").get();
        } catch (Exception e) {
            e.printStackTrace();
            Commands.finishedExecution(context, defaultEntry);
            return 1;
        }

        context.getSource().sendFeedback(() -> Text.literal(ModInfo.MOD_NAME).formatted(Formatting.GOLD), false);
        context.getSource().sendFeedback(() -> Text.literal("Version " + ModInfo.getVersion()), false);
        final ClickEvent openHomepageEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, homepageUrl);
        context.getSource().sendFeedback(() -> Text.literal(homepageUrl).styled(style -> style.withClickEvent(openHomepageEvent)).formatted(Formatting.UNDERLINE).formatted(Formatting.GREEN), false);
        Commands.finishedExecution(context, defaultEntry);
        return 1;
    }

    private static int executeReload(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> Text.literal("Reloading Config...").formatted(Formatting.GOLD), false);
        List<String> error = Config.loadAll();
        if(!error.isEmpty()) {
            String failed = String.join(",", error);
            context.getSource().sendFeedback(() -> Text.literal("Config Reloaded. " + failed + " failed to load.").formatted(Formatting.RED), false);
            context.getSource().sendFeedback(() -> Text.literal("Please check whether the JSON syntax is correct!").formatted(Formatting.RED), false);
        } else {
            context.getSource().sendFeedback(() -> Text.literal(ModInfo.MOD_NAME + " Config Reloaded!").formatted(Formatting.GREEN), false);
        }
        Commands.finishedExecution(context, defaultEntry);
        return 1;
    }
}
