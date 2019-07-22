package me.ilnicki.bg.core.pixelmatrix.transforming.transformers;

import me.ilnicki.bg.core.pixelmatrix.Vector;
import me.ilnicki.bg.core.pixelmatrix.transforming.VectorTransformer;

public class Translate implements VectorTransformer {
    private final Vector value;

    public Translate(Vector value) {
        this.value = value;
    }

    @Override
    public Vector apply(Vector vector) {
        return vector.sub(value);
    }
}
