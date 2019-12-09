package me.ilnicki.bg.core.pixelmatrix.loaders;

import me.ilnicki.bg.core.pixelmatrix.loaders.internal.InternalPixelMatrixLoader;
import me.ilnicki.container.ProvisionException;
import me.ilnicki.container.provider.Factory;

public class PixelMatrixLoaderFactory implements Factory<PixelMatrixLoader> {
    @Override
    public PixelMatrixLoader produce(String[] args) throws ProvisionException {
        if (args.length == 2) {
            switch (args[0]) {
                case "internal":
                    return new InternalPixelMatrixLoader(args[1]);
            }
        }

        return new DummyPixelMatrixLoader();
    }
}
