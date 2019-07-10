package me.ilnicki.bg.core.pixelmatrix;

public interface EditablePixelMatrix extends PixelMatrix {
    void setPixel(Vector point, Pixel value);
}
