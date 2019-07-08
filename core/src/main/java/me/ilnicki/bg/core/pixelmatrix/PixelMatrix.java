package me.ilnicki.bg.core.pixelmatrix;

public interface PixelMatrix {
    int getWidth();

    int getHeight();

    Pixel getPixel(Vector point);

    void setPixel(Vector point, Pixel value);
}
