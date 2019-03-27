package me.ilnicki.bg.engine.machine;

import me.ilnicki.bg.engine.pixelmatrix.*;

public final class Layer implements Positionable, PixelMatrix {
    private Point position;
    private PixelMatrix pixelMatrix;

    public Layer(PixelMatrix pm) {
        this.position = new Point(0, 0);
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
    public void setPixel(int x, int y, Pixel value) {
        pixelMatrix.setPixel(x - position.getX(), y - position.getY(), value);
    }

    @Override
    public Pixel getPixel(int x, int y) {
        try {
            return pixelMatrix.getPixel(x - position.getX(), y - position.getY());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Pixel getPixel(Point point) {
        return this.getPixel(point.getX(), point.getY());
    }

    @Override
    public void setPixel(Point point, Pixel value) {
        this.setPixel(point.getX(), point.getY(), value);
    }

    @Override
    public String toString() {
        return pixelMatrix.toString();
    }

    @Override
    public int getX() {
        return position.getX();
    }

    @Override
    public void setX(int positionX) {
        position.setX(positionX);
    }

    @Override
    public int getY() {
        return this.position.getY();
    }

    @Override
    public void setY(int positionY) {
        position.setY(positionY);
    }

    @Override
    public void setPosition(int positionX, int positionY) {
        this.setX(positionX);
        this.setY(positionY);
    }

    @Override
    public void setPosition(Point point) {
        position = point;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    public PixelMatrix getPixelMatrix() {
        return pixelMatrix;
    }

    public void setPixelMatrix(PixelMatrix pm) {
        pixelMatrix = pm;
    }
}
