package me.ilnicki.bg.core.pixelmatrix.animation;

import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;

public interface Animation extends PixelMatrix {
  Animation next();

  boolean hasNext();

  void reset();
}
