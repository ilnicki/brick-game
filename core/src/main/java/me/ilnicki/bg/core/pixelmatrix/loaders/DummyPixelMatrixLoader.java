package me.ilnicki.bg.core.pixelmatrix.loaders;

import me.ilnicki.bg.core.pixelmatrix.ConstantPixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;

public class DummyPixelMatrixLoader implements PixelMatrixLoader {
  private static final PixelMatrix DUMMY = (new ConstantPixelMatrix.Builder(1, 1)).build();

  @Override
  public void load(String... spriteNames) {}

  @Override
  public PixelMatrix get(String spriteName) {
    return DUMMY;
  }
}
