package me.ilnicki.bg.core.pixelmatrix.animation;

import me.ilnicki.bg.core.math.Vector;
import me.ilnicki.bg.core.pixelmatrix.Pixel;

public class TrackAnimation implements Animation {
  private final Track track;
  private TrackIterator trackIterator;

  public TrackAnimation(Track track) {
    this.track = track;
    setup();
  }

  @Override
  public int getWidth() {
    return trackIterator.current().getWidth();
  }

  @Override
  public int getHeight() {
    return trackIterator.current().getHeight();
  }

  @Override
  public Pixel getPixel(Vector point) {
    return trackIterator.current().getPixel(point);
  }

  @Override
  public Animation next() {
    if (trackIterator.hasNext()) {
      trackIterator.next();
    }

    return this;
  }

  @Override
  public boolean hasNext() {
    return trackIterator.hasNext();
  }

  @Override
  public void reset() {
    setup();
  }

  private void setup() {
    this.trackIterator = track.trackIterator();
  }
}
