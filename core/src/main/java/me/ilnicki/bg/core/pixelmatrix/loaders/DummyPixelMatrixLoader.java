package me.ilnicki.bg.core.pixelmatrix.loaders;

import me.ilnicki.bg.core.pixelmatrix.ArrayPixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.ConstantPixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;

public class DummyPixelMatrixLoader implements PixelMatrixLoader {
    private final static PixelMatrix DUMMY = (new ConstantPixelMatrix.Builder(1, 1)).build();

    @Override
    public void load(String... spriteName) {
    }

    @Override
    public PixelMatrix get(String spriteName) {
        return DUMMY;
    }
}
