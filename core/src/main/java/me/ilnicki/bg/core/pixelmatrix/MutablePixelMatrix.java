package me.ilnicki.bg.core.pixelmatrix;

public interface MutablePixelMatrix extends PixelMatrix {
    void setPixel(Vector point, Pixel value);
}
