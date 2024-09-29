package com.lx862.svrutil.feature;

public enum FeatureSet {
    JOIN_MESSAGE(new JoinMessageFeature()),
    HUNGER(new HungerFeature()),
    TEXT_OVERRIDE(new TextOverrideFeature()),
    VANILLA_MECHANICS(new VanillaMechanicsFeature()),
    FANCY_MESSAGE(new FancyMessageFeature());

    public final Feature feature;

    FeatureSet(Feature feature) {
        this.feature = feature;
    }
}