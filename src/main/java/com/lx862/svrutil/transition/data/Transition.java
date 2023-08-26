package com.lx862.svrutil.transition.data;

import java.util.HashMap;
import java.util.List;

public class Transition {
    public TransitionViewType transitionViewType;
    public HashMap<Integer, TransitionSlide> slides;

    public Transition(List<TransitionSlide> slides, TransitionViewType transitionViewType) {
        this.slides = new HashMap<>();
        this.transitionViewType = transitionViewType;
        int totalDuration = 0;

        for(TransitionSlide slide : slides) {
            this.slides.put(totalDuration, slide);
            totalDuration += slide.durationTick;
        }
    }
}
