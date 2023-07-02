package com.lx.svrutil.transition.data;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;

public class TransitionSlide {
    public int durationTick;
    public Identifier startSound;
    public HashMap<Integer, Keyframe> keyframes;

    public TransitionSlide(double durationInSec, String startSound, List<Keyframe> keyframes) {
        this.durationTick = (int)(durationInSec * 20);
        this.startSound = new Identifier(startSound);
        this.keyframes = new HashMap<>();
        int builtTick = 0;
        for(Keyframe keyframe : keyframes) {
            this.keyframes.put(builtTick, keyframe);
            builtTick += keyframe.duration;
        }
    }

}
