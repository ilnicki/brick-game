package me.ilnicki.bg.core.pixelmatrix.animation;

import java.util.Collections;
import java.util.List;

public class Track {
    private final List<Frame> frames;
    private final boolean looped;

    public Track(List<Frame> frames, boolean looped) {
        this.frames = Collections.unmodifiableList(frames);
        this.looped = looped;
    }

    public List<Frame> getFrames() {
        return frames;
    }

    public boolean isLooped() {
        return looped;
    }

    public TrackIterator trackIterator() {
        return new TrackIterator(this);
    }
}
