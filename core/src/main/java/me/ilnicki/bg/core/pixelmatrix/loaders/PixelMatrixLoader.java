package me.ilnicki.bg.core.pixelmatrix.loaders;

import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;

public interface PixelMatrixLoader {
  void load(String... spriteNames);

  PixelMatrix get(String spriteName);
}
