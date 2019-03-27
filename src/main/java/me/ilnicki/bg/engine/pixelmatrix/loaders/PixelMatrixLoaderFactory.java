package me.ilnicki.bg.engine.pixelmatrix.loaders;

import me.ilnicki.bg.engine.system.container.ComponentFactory;
import me.ilnicki.bg.engine.system.container.ResolvingException;

public class PixelMatrixLoaderFactory implements ComponentFactory<PixelMatrixLoader> {
    @Override
    public PixelMatrixLoader produce(String[] args) throws ResolvingException {
        return new DummyPixelMatrixLoader();
    }
}
