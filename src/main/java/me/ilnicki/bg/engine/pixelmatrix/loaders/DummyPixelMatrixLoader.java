package me.ilnicki.bg.engine.pixelmatrix.loaders;

import me.ilnicki.bg.engine.pixelmatrix.ArrayPixelMatrix;
import me.ilnicki.bg.engine.pixelmatrix.PixelMatrix;

public class DummyPixelMatrixLoader implements PixelMatrixLoader {
    @Override
    public PixelMatrix load(String spriteName, boolean shouldBeCached) {
        return new ArrayPixelMatrix(1, 1);
    }
}
