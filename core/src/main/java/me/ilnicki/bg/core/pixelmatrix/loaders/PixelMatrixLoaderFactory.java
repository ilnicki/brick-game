package me.ilnicki.bg.core.pixelmatrix.loaders;

import me.ilnicki.bg.core.pixelmatrix.loaders.internal.InternalPixelMatrixLoader;
import me.ilnicki.bg.core.system.container.provider.Factory;
import me.ilnicki.bg.core.system.container.ProvisionException;

public class PixelMatrixLoaderFactory implements Factory<PixelMatrixLoader> {
    @Override
    public PixelMatrixLoader produce(String[] args) throws ProvisionException {
        if(args.length == 2) {
            switch (args[0]) {
                case "internal":
                    return new InternalPixelMatrixLoader(args[1]);
            }
        }

        return new DummyPixelMatrixLoader();
    }
}
