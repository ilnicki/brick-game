package me.ilnicki.bg.core.pixelmatrix.transforming;

import me.ilnicki.bg.core.pixelmatrix.Vector;

import java.util.ArrayList;
import java.util.List;

public class TransformerList implements VectorTransformer {
    private final List<VectorTransformer> transformers = new ArrayList<>();
    private VectorTransformer applier = null;

    @Override
    public Vector apply(Vector vector) {
        return getApplier().apply(vector);
    }

    public void add(VectorTransformer transformer) {
        transformers.add(transformer);
        applier = null;
    }

    private VectorTransformer getApplier() {
        if (applier == null) {
            applier = transformers
                    .stream()
                    .reduce(VectorTransformer.identity(), (acc, transformer) -> acc.andThen(transformer));
        }

        return applier;
    }
}
