package me.ilnicki.bg.core.pixelmatrix.layering;


import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.math.Vector;
import me.ilnicki.bg.core.pixelmatrix.transforming.TransformerList;
import me.ilnicki.bg.core.pixelmatrix.transforming.VectorTransformer;

public final class Layer<T extends PixelMatrix> implements PixelMatrix {
    private T data;
    private final TransformerList transformers = new TransformerList();

    public Layer(T pm) {
        this.data = pm;
    }

    @Override
    public int getWidth() {
        return data.getWidth();
    }

    @Override
    public int getHeight() {
        return data.getHeight();
    }

    @Override
    public Pixel getPixel(Vector point) {
        final Vector realPos = transformers.apply(point);

        if (realPos.getX() >= 0 && realPos.getX() < getWidth() && realPos.getY() >= 0 && realPos.getY() < getHeight()) {
            return data.getPixel(realPos);
        }

        return null;
    }

    public Layer<T> transform(VectorTransformer transformer) {
        transformers.add(transformer);
        return this;
    }

    @Override
    public String toString() {
        return data.toString();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
