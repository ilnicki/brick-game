package me.ilnicki.bg.core.machine;

import me.ilnicki.bg.core.pixelmatrix.*;

public final class Layer implements Positionable, PixelMatrix {
    private Vector pos;
    private PixelMatrix pixelMatrix;

    public Layer(PixelMatrix pm) {
        this.pos = new Vector(0, 0);
        this.pixelMatrix = pm;
    }

    public Layer(int width, int height) {
        this(new ArrayPixelMatrix(width, height));
    }

    @Override
    public int getWidth() {
        return pixelMatrix.getWidth();
    }

    @Override
    public int getHeight() {
        return pixelMatrix.getHeight();
    }

    @Override
    public void setPixel(Vector point, Pixel value) {
        final Vector realPoint = point.sub(pos);

        if (realPoint.getX() >= 0
                && realPoint.getX() < getWidth()
                && realPoint.getY() >= 0
                && realPoint.getY() < getHeight()) {
            pixelMatrix.setPixel(realPoint, value);
        }
    }


    @Override
    public Pixel getPixel(Vector point) {
        final Vector realPos = point.sub(pos);

        if (realPos.getX() >= 0 && realPos.getX() < getWidth() && realPos.getY() >= 0 && realPos.getY() < getHeight()) {
            return pixelMatrix.getPixel(realPos);
        }

        return null;
    }

    @Override
    public String toString() {
        return pixelMatrix.toString();
    }

    @Override
    public void setPos(Vector pos) {
        this.pos = pos;
    }

    @Override
    public Vector getPos() {
        return pos;
    }

    public PixelMatrix getPixelMatrix() {
        return pixelMatrix;
    }

    public void setPixelMatrix(PixelMatrix pm) {
        pixelMatrix = pm;
    }
}
