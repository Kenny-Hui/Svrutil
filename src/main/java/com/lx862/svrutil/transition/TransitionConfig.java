package com.lx862.svrutil.transition;

import com.google.gson.*;
import com.lx862.svrutil.ModInfo;
import com.lx862.svrutil.transition.data.Keyframe;
import com.lx862.svrutil.transition.data.Transition;
import com.lx862.svrutil.transition.data.TransitionSlide;
import com.lx862.svrutil.SvrUtil;
import com.lx862.svrutil.Util;
import com.lx862.svrutil.transition.data.TransitionViewType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.math.Vec3d;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TransitionConfig {
    private static final Path CONFIG_PATH = Paths.get(FabricLoader.getInstance().getConfigDir().toString(), "svrutil", "transition.json");
    public static final HashMap<String, Transition> transitions = new HashMap<>();
    public static boolean load() {
        if(!FabricLoader.getInstance().isDevelopmentEnvironment()) {
            // Don't load if not in dev env, this is WIP feature
            return true;
        }

        if(!Files.exists(CONFIG_PATH)) {
            SvrUtil.LOGGER.warn("[{}] Transition file not found.", ModInfo.MOD_NAME);
            return true;
        }

        SvrUtil.LOGGER.info("[{}] Reading Transition config...", ModInfo.MOD_NAME);
        transitions.clear();
        try {
            final JsonObject jsonConfig = new JsonParser().parse(String.join("", Files.readAllLines(CONFIG_PATH))).getAsJsonObject();
            jsonConfig.entrySet().forEach(entry -> {
                String transitionId = entry.getKey();
                JsonObject transitionData = entry.getValue().getAsJsonObject();
                TransitionViewType transitionViewType;
                try {
                    transitionViewType = TransitionViewType.valueOf(transitionData.get("transitionViewType").getAsString());
                } catch (Exception ignored) {
                    transitionViewType = TransitionViewType.SPECTATE;
                }

                ArrayList<TransitionSlide> slides = new ArrayList<>();

                JsonArray transitionSlides = transitionData.getAsJsonArray("slides");
                for(JsonElement e : transitionSlides) {
                    JsonObject slideObject = e.getAsJsonObject();
                    String startSound = slideObject.get("startSound").getAsString();
                    List<Keyframe> keyframes = new ArrayList<>();
                    double slideDuration = 0;
                    for(JsonElement keyframeElement : slideObject.get("keyframes").getAsJsonArray()) {
                        double kfDuration = 0;
                        JsonObject keyframeObject = keyframeElement.getAsJsonObject();
                        JsonArray posArray = keyframeObject.get("pos").getAsJsonArray();
                        JsonArray camArray = keyframeObject.get("camera").getAsJsonArray();
                        if(keyframeObject.has("duration")) kfDuration = keyframeObject.get("duration").getAsDouble();
                        Vec3d pos = Util.getVec3dFromJsonArray(posArray);
                        Vec3d cam = Util.getXZFromJsonArray(camArray);

                        keyframes.add(new Keyframe(pos, cam, kfDuration));
                        slideDuration += kfDuration;
                    }

                    slides.add(new TransitionSlide(slideDuration, startSound, keyframes));
                }
                transitions.put(transitionId, new Transition(slides, transitionViewType));
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
