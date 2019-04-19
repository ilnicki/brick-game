package me.ilnicki.bg.core.pixelmatrix.loaders;

import me.ilnicki.bg.core.pixelmatrix.ArrayPixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;

public class DummyPixelMatrixLoader implements PixelMatrixLoader {
    @Override
    public PixelMatrix load(String spriteName, boolean shouldBeCached) {
        return new ArrayPixelMatrix(1, 1);
    }
}
