package com.lx.svrutil.transition.data;

import net.minecraft.util.math.Vec3d;

public class Keyframe {
    public Vec3d position;
    public Vec3d camera;
    public int duration;

    public Keyframe(Vec3d position, Vec3d camera, double durationInSec) {
        this.position = position;
        this.camera = camera;
        this.duration = (int)Math.round(durationInSec * 20);
    }
}
