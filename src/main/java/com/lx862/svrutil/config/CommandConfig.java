package com.lx862.svrutil.config;

import com.google.gson.*;
import com.lx862.svrutil.SvrUtil;
import com.lx862.svrutil.ModInfo;
import com.lx862.svrutil.data.CommandEntry;
import com.lx862.svrutil.data.SvrUtilLogger;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CommandConfig {
    private static final Path CONFIG_PATH = Config.getConfigPath("commands.json");
    public static final HashMap<String, CommandEntry> commandEntries = new HashMap<>();

    public static boolean load() {
        if(!Files.exists(CONFIG_PATH)) {
            SvrUtilLogger.debug("Command config file not found, generating one...");
            generate();
            load();
            return true;
        }

        SvrUtilLogger.info("Reading Command config...");
        commandEntries.clear();
        try {
            final JsonObject jsonConfig = new JsonParser().parse(String.join("", Files.readAllLines(CONFIG_PATH))).getAsJsonObject();

            if(jsonConfig.has("overrides")) {
                JsonObject commandConfig = jsonConfig.getAsJsonObject("overrides");
                commandConfig.entrySet().forEach(entries -> {
                    String originalCmdName = entries.getKey();
                    JsonObject jsonCommandEntry = entries.getValue().getAsJsonObject();
                    CommandEntry commandEntry = new CommandEntry(jsonCommandEntry);
                    commandEntries.put(originalCmdName, commandEntry);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            generate();
            return false;
        }

        return true;
    }

    public static void generate() {
        SvrUtilLogger.info("Generating command config...");
        final JsonObject jsonConfig = new JsonObject();
        jsonConfig.addProperty("_COMMENT_1", "This config is used to define what commands are enabled/disabled, it's required permission level to use those commands and such");
        jsonConfig.addProperty("_COMMENT_2", "Unless you are not satisfied with the current command configuration, you don't need to modify this file.");
        jsonConfig.addProperty("_COMMENT_3", "Otherwise, remove the underscore at the front of '_overrides' below and you may override the default command configuration.");

        final JsonObject commandConfigs = new JsonObject();
        final JsonObject sampleConfig1 = new CommandEntry("afk_remapped_command", 2, true).toJson();
        commandConfigs.add("afk", sampleConfig1);
        jsonConfig.add("_overrides", commandConfigs);

        try {
            Files.write(CONFIG_PATH, Collections.singleton(new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(jsonConfig)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CommandEntry getCommandEntry(@NotNull CommandEntry defaultEntry) {
        if(commandEntries.containsKey(defaultEntry.commandName)) {
            return defaultEntry.apply(commandEntries.get(defaultEntry.commandName));
        } else {
            return  defaultEntry;
        }
    }
}
