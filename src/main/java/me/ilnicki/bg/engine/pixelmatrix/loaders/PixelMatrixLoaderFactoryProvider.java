package me.ilnicki.bg.engine.pixelmatrix.loaders;

import me.ilnicki.bg.engine.system.container.provider.FactoryProvider;
import me.ilnicki.bg.engine.system.container.ProvisionException;

public class PixelMatrixLoaderFactoryProvider implements FactoryProvider<PixelMatrixLoader> {
    @Override
    public PixelMatrixLoader produce(String[] args) throws ProvisionException {
        return new DummyPixelMatrixLoader();
    }
}
