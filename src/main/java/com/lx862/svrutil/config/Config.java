package com.lx862.svrutil.config;

import com.lx862.svrutil.ModInfo;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private static final Path configFolder = Paths.get(FabricLoader.getInstance().getConfigDir().toString(), ModInfo.MOD_ID);

    public static Path getConfigPath(String filename) {
        try {
            configFolder.toFile().mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return configFolder.resolve(filename);
    }

    public static List<String> loadAll() {
        List<String> error = new ArrayList<>();
        if(!CommandConfig.load()) {
            error.add("Command Config");
        }

        if(!FeatureConfig.load()) {
            error.add("Feature Config");
        }
        return error;
    }
}
