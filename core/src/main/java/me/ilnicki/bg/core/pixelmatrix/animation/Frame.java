package me.ilnicki.bg.core.pixelmatrix.animation;

import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;

public class Frame {
  private final PixelMatrix data;
  private final int length;

  public Frame(PixelMatrix data, int length) {
    this.data = data;
    this.length = length;
  }

  public PixelMatrix getData() {
    return data;
  }

  public int getLength() {
    return length;
  }
}
