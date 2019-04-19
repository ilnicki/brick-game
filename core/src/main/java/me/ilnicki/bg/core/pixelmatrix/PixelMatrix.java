package me.ilnicki.bg.core.pixelmatrix;

public interface PixelMatrix {
    int getWidth();

    int getHeight();

    Pixel getPixel(int x, int y);

    void setPixel(int x, int y, Pixel value);

    Pixel getPixel(Point point);

    void setPixel(Point point, Pixel value);
}
