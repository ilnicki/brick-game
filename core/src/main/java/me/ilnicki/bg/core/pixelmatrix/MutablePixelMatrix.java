package me.ilnicki.bg.core.pixelmatrix;

import me.ilnicki.bg.core.math.Vector;

public interface MutablePixelMatrix extends PixelMatrix {
    void setPixel(Vector point, Pixel value);
}
