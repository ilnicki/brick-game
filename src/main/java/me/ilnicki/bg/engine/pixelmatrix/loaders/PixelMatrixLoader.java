package me.ilnicki.bg.engine.pixelmatrix.loaders;

import me.ilnicki.bg.engine.pixelmatrix.PixelMatrix;

public interface PixelMatrixLoader {
     PixelMatrix load(String spriteName, boolean shouldBeCached);
}
