package com.lx862.svrutil.feature;

public enum FeatureSet {
    JoinMessage(new JoinMessageFeature());

    public final Feature feature;

    FeatureSet(Feature feature) {
        this.feature = feature;
    }
}
