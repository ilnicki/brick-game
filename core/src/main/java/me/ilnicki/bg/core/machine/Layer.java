package me.ilnicki.bg.core.machine;

import me.ilnicki.bg.core.pixelmatrix.*;

public final class Layer implements Positionable, PixelMatrix {
    private Point pos;
    private PixelMatrix pixelMatrix;

    public Layer(PixelMatrix pm) {
        this.pos = new Point(0, 0);
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
    public void setPixel(Point point, Pixel value) {
        final Point realPoint = point.sub(pos);

        if (realPoint.getX() >= 0
                && realPoint.getX() < getWidth()
                && realPoint.getY() >= 0
                && realPoint.getY() < getHeight()) {
            pixelMatrix.setPixel(realPoint, value);
        }
    }

    @Override
    public Pixel getPixel(int x, int y) {
        final int realX = x - pos.getX();
        final int realY = y - pos.getY();

        if (realX >= 0 && realX < getWidth() && realY >= 0 && realY < getHeight()) {
            return pixelMatrix.getPixel(realX, realY);
        }

        return null;
    }

    @Override
    public Pixel getPixel(Point point) {
        return this.getPixel(point.getX(), point.getY());
    }

    @Override
    public String toString() {
        return pixelMatrix.toString();
    }

    @Override
    public void setPos(Point pos) {
        this.pos = pos;
    }

    @Override
    public Point getPos() {
        return pos;
    }

    public PixelMatrix getPixelMatrix() {
        return pixelMatrix;
    }

    public void setPixelMatrix(PixelMatrix pm) {
        pixelMatrix = pm;
    }
}
