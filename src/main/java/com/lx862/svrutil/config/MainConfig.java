package com.lx862.svrutil.config;

import com.google.gson.*;
import com.lx862.svrutil.ModInfo;
import com.lx862.svrutil.data.SvrUtilLogger;
import net.minecraft.text.Text;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class MainConfig {
    private static final Path CONFIG_PATH = Config.getConfigPath("config.json");
    private static boolean fixedItemFrame = false;
    private static int minItemFrameInteractOpLevel = 0;
    private static int fallingBlockDelay = 2; // Vanilla takes 2 tick to fall
    public static Text whitelistedMessage = null;

    public static boolean load() {
        if(!Files.exists(CONFIG_PATH)) {
            SvrUtilLogger.info("Config file not found, generating one...");
            generate();
            load();
            return true;
        }

        SvrUtilLogger.info("Reading config...");
        try {
            final JsonObject jsonConfig = new JsonParser().parse(String.join("", Files.readAllLines(CONFIG_PATH))).getAsJsonObject();
            if(jsonConfig.has("whitelistedMessage")) {
                JsonElement element = jsonConfig.get("whitelistedMessage");
                try {
                    whitelistedMessage = Text.Serializer.fromJson(element.getAsString());
                } catch (Exception ignored) {
                }

                try {
                    whitelistedMessage = Text.Serializer.fromJson(element);
                } catch (Exception ignored) {
                }
            }

            if(jsonConfig.has("fixedItemFrame")) {
                fixedItemFrame = jsonConfig.get("fixedItemFrame").getAsBoolean();
            }

            if(jsonConfig.has("minItemFrameInteractOpLevel")) {
                minItemFrameInteractOpLevel = jsonConfig.get("minItemFrameInteractOpLevel").getAsInt();
            }

            if(jsonConfig.has("fallingBlockDelay")) {
                fallingBlockDelay = jsonConfig.get("fallingBlockDelay").getAsInt();
            }
        } catch (Exception e) {
            e.printStackTrace();
            generate();
            return false;
        }

        return true;
    }

    public static void generate() {
        SvrUtilLogger.info("[{}] Generating config...", ModInfo.MOD_NAME);
        final JsonObject jsonConfig = new JsonObject();
        jsonConfig.addProperty("fallingBlockDelay", fallingBlockDelay);
        jsonConfig.addProperty("fixedItemFrame", fixedItemFrame);
        jsonConfig.addProperty("minItemFrameInteractOpLevel", minItemFrameInteractOpLevel);
        jsonConfig.addProperty("whitelistedMessage", "You are not whitelisted on the server!");

        try {
            Files.write(CONFIG_PATH, Collections.singleton(new GsonBuilder().setPrettyPrinting().create().toJson(jsonConfig)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getFallingBlockDelay() {
        return fallingBlockDelay;
    }
    public static boolean getFixedItemFrame() {
        return fixedItemFrame;
    }
    public static int getMinItemFrameInteractOpLevel() {
        return minItemFrameInteractOpLevel;
    }
}
