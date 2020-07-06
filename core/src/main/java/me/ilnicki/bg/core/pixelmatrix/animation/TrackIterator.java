package me.ilnicki.bg.core.pixelmatrix.animation;

import java.util.Iterator;

public class TrackIterator implements Iterator<Frame> {
  private final Track track;
  private Iterator<Frame> frameIterator;
  private Frame current;

  public TrackIterator(Track track) {
    this.track = track;
    this.frameIterator = track.getFrames().iterator();
  }

  @Override
  public boolean hasNext() {
    return track.isLooped() || frameIterator.hasNext();
  }

  @Override
  public Frame next() {
    if (!frameIterator.hasNext() && track.isLooped()) {
      frameIterator = track.getFrames().iterator();
    }

    return current = frameIterator.next();
  }

  public Frame current() {
    return current;
  }
}
