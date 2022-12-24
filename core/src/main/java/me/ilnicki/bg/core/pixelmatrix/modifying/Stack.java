package me.ilnicki.bg.core.pixelmatrix.modifying;

import me.ilnicki.bg.core.math.Vector;
import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;

import java.util.Arrays;
import java.util.Objects;

public class Stack implements PixelMatrix {
  private final PixelMatrix[] matrices;
  private final int width;
  private final int height;

  public Stack(PixelMatrix... matrices) {
    this.matrices = matrices;

    this.width = Arrays.stream(this.matrices)
        .map(PixelMatrix::getWidth)
        .min(Integer::compare)
        .orElse(0);

    this.height = Arrays.stream(this.matrices)
        .map(PixelMatrix::getHeight)
        .min(Integer::compare)
        .orElse(0);
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public Pixel getPixel(Vector point) {
    return Arrays.stream(matrices)
        .map(m -> m.getPixel(point))
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null);
  }
}
