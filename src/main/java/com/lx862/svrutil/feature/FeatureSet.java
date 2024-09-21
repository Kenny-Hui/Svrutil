package com.lx862.svrutil.feature;

public enum FeatureSet {
    JOIN_MESSAGE(new JoinMessageFeature()),
    HUNGER(new HungerFeature()),
    VANILLA_MECHANICS(new VanillaMechanicsFeature());

    public final Feature feature;

    FeatureSet(Feature feature) {
        this.feature = feature;
    }
}