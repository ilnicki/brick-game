package me.ilnicki.bg.core.machine;


import me.ilnicki.bg.core.pixelmatrix.*;

public final class Layer<T extends PixelMatrix> implements Positionable, PixelMatrix {
    private Vector pos;
    private T data;

    public Layer(T pm) {
        this.pos = new Vector(0, 0);
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
        final Vector realPos = point.sub(pos);

        if (realPos.getX() >= 0 && realPos.getX() < getWidth() && realPos.getY() >= 0 && realPos.getY() < getHeight()) {
            return data.getPixel(realPos);
        }

        return null;
    }

    @Override
    public String toString() {
        return data.toString();
    }

    @Override
    public void setPos(Vector pos) {
        this.pos = pos;
    }

    @Override
    public Vector getPos() {
        return pos;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
