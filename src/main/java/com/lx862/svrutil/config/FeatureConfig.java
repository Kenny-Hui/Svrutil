package com.lx862.svrutil.config;

import com.google.gson.*;
import com.lx862.svrutil.data.SvrUtilLogger;
import com.lx862.svrutil.feature.FeatureSet;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class FeatureConfig {
    private static final Path CONFIG_PATH = Config.getConfigPath("feature.json");

    public static boolean load() {
        SvrUtilLogger.info("Reading feature config...");

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
                SvrUtilLogger.info("Feature config file changed, writing to disk...");
                Files.write(CONFIG_PATH, Collections.singleton(new GsonBuilder().setPrettyPrinting().create().toJson(jsonConfig)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
