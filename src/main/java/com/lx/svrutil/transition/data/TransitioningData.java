package com.lx.svrutil.transition.data;

import com.lx.svrutil.transition.TransitionConfig;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class TransitioningData {
    public Transition transition;
    public Entity cameraEntity;
    public int elapsedTicks;
    public Vec3d originalPlayerPos;
    public Vec3d originalPlayerCamera;

    public TransitioningData(String transitionId, Vec3d originalPlayerPos, Vec3d originalPlayerCamera) {
        this.transition = TransitionConfig.transitions.get(transitionId);
        this.originalPlayerCamera = originalPlayerCamera;
        this.originalPlayerPos = originalPlayerPos;
        this.elapsedTicks = 0;
    }
}
