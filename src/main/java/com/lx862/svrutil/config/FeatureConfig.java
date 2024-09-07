package com.lx862.svrutil.config;

import com.google.gson.*;
import com.lx862.svrutil.ModInfo;
import com.lx862.svrutil.SvrUtil;
import com.lx862.svrutil.feature.FeatureSet;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class FeatureConfig {
    private static final Path CONFIG_PATH = Config.getConfigPath("feature.json");

    public static boolean load() {
        SvrUtil.LOGGER.info("[{}] Reading feature config...", ModInfo.MOD_NAME);

        try {
            boolean needWriteConfig = false;
            final JsonObject jsonConfig;

            if(!Files.exists(CONFIG_PATH)) {
                jsonConfig = new JsonObject();
            } else {
                jsonConfig = JsonParser.parseString(String.join("", Files.readAllLines(CONFIG_PATH))).getAsJsonObject();
            }

            for(FeatureSet featureSet : FeatureSet.values()) {
                String configEntryName = featureSet.feature.configName;
                if(jsonConfig.has(configEntryName)) {
                    featureSet.feature.readConfig(jsonConfig.getAsJsonObject(configEntryName));
                } else {
                    needWriteConfig = true;
                    jsonConfig.add(configEntryName, featureSet.feature.writeConfig());
                }
            }

            if(needWriteConfig) {
                SvrUtil.LOGGER.info("[Svrutil] Feature config file changed, writing to disk...");
                Files.write(CONFIG_PATH, Collections.singleton(new GsonBuilder().setPrettyPrinting().create().toJson(jsonConfig)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
