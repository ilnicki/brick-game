package me.ilnicki.bg.core.pixelmatrix.transforming;

import me.ilnicki.bg.core.pixelmatrix.Vector;

import java.util.ArrayList;
import java.util.List;

public class TransformerList implements VectorTransformer {
    private List<VectorTransformer> transformers = new ArrayList<>();
    private VectorTransformer transformer = null;

    @Override
    public Vector apply(Vector vector) {
        return getTransformer().apply(vector);
    }

    public void add(VectorTransformer transformer) {
        transformers.add(transformer);
        transformer = null;
    }

    private VectorTransformer getTransformer() {
        if (transformer == null) {
            transformer = transformers
                    .stream()
                    .reduce(VectorTransformer.identity(), (acc, transformer) -> acc.andThen(transformer));
        }

        return transformer;
    }
}
