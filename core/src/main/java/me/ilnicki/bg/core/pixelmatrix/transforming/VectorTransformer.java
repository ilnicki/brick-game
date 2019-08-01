package me.ilnicki.bg.core.pixelmatrix.transforming;

import me.ilnicki.bg.core.pixelmatrix.Vector;

import java.util.Objects;
import java.util.function.UnaryOperator;

@FunctionalInterface
public interface VectorTransformer extends UnaryOperator<Vector> {
    default VectorTransformer compose(VectorTransformer transformer) {
        Objects.requireNonNull(transformer);
        return vector -> this.apply(transformer.apply(vector));
    }

    default VectorTransformer andThen(VectorTransformer transformer) {
        Objects.requireNonNull(transformer);
        return vector -> transformer.apply(this.apply(vector));
    }

    default VectorTransformer combine(VectorTransformer transformer) {
        return compose(transformer);
    }

    static VectorTransformer identity() {
        return v -> v;
    }
}
