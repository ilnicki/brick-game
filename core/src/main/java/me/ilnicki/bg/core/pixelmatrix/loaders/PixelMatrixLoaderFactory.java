package me.ilnicki.bg.core.pixelmatrix.loaders;

import me.ilnicki.bg.core.data.resource.ClassResourceProvider;
import me.ilnicki.container.ProvisionException;
import me.ilnicki.container.provider.Factory;

import java.util.HashMap;
import java.util.Map;

public class PixelMatrixLoaderFactory implements Factory<PixelMatrixLoader> {
    private final static PixelMatrixLoader DUMMY_LOADER = new DummyPixelMatrixLoader();
    private final Map<String, Factory<PixelMatrixLoader>> loaders
            = new HashMap<String, Factory<PixelMatrixLoader>>() {{
        put("internal", args -> new ImagePixelMatrixLoader(args[1], new ClassResourceProvider()));
    }};

    @Override
    public PixelMatrixLoader produce(String[] args) throws ProvisionException {
        if (args.length > 1) {
            return loaders.get(args[0]).produce(args);
        }

        return DUMMY_LOADER;
    }
}
