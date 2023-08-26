package com.lx862.svrutil.config;

import com.lx862.svrutil.transition.TransitionConfig;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private static final Path configFolder = Paths.get(FabricLoader.getInstance().getConfigDir().toString(), "svrutil");

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
        if(!MainConfig.load()) {
            error.add("Main Config");
        }

        if(!TransitionConfig.load()) {
            error.add("Transition Config");
        }

        if(!CommandConfig.load()) {
            error.add("Command Config");
        }

        return error;
    }
}
