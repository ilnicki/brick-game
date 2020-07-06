package me.ilnicki.bg.core.pixelmatrix;

import me.ilnicki.bg.core.math.Vector;

public interface PixelMatrix {
  int getWidth();

  int getHeight();

  Pixel getPixel(Vector point);
}
