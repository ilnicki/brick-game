package me.ilnicki.bg.core.pixelmatrix.animation;

import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TrackIterator implements Iterator<PixelMatrix> {
  private final boolean isLooped;
  private final List<PixelMatrix> trackData;
  private Iterator<PixelMatrix> frameIterator;
  private PixelMatrix current;

  public TrackIterator(Track track) {
    this.isLooped = track.isLooped();
    this.trackData = track.getFrames()
        .stream()
        .flatMap(frame -> Stream
            .generate(frame::getData)
            .limit(frame.getLength())
        )
        .collect(Collectors.toList());
    this.frameIterator = trackData.iterator();
  }

  @Override
  public boolean hasNext() {
    return isLooped || frameIterator.hasNext();
  }

  @Override
  public PixelMatrix next() {
    if (!frameIterator.hasNext() && isLooped) {
      frameIterator = trackData.iterator();
    }

    return current = frameIterator.next();
  }

  public PixelMatrix current() {
    return current;
  }
}
