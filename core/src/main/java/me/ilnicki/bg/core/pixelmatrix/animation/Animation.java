package me.ilnicki.bg.core.pixelmatrix.animation;

import me.ilnicki.bg.core.math.Vector;
import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;

public class Animation implements PixelMatrix {
  private final TrackIterator trackIterator;
  private int currentDraw = 0;

  public Animation(Track track) {
    this.trackIterator = track.trackIterator();
  }

  @Override
  public int getWidth() {
    return trackIterator.current().getData().getWidth();
  }

  @Override
  public int getHeight() {
    return trackIterator.current().getData().getHeight();
  }

  @Override
  public Pixel getPixel(Vector point) {
    return trackIterator.current().getData().getPixel(point);
  }

  public Animation next() {
    if (trackIterator.current() != null && currentDraw + 1 < trackIterator.current().getLength()) {
      currentDraw++;
    } else {
      if (trackIterator.hasNext()) {
        trackIterator.next();
        currentDraw = 0;
      }
    }

    return this;
  }
}
