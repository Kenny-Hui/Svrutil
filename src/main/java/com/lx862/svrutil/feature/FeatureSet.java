package com.lx862.svrutil.feature;

public enum FeatureSet {
    JOIN_MESSAGE(new JoinMessageFeature()),
    HUNGER(new HungerFeature()),
    CUSTOM_MESSAGE(new CustomMessageFeature()),
    VANILLA_MECHANICS(new VanillaMechanicsFeature());

    public final Feature feature;

    FeatureSet(Feature feature) {
        this.feature = feature;
    }
}