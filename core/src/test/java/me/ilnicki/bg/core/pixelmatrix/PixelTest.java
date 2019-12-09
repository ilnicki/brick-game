package me.ilnicki.bg.core.pixelmatrix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PixelTest {

    @Test
    void stringifying() {
        assertEquals("[ ]", Pixel.WHITE.toString());
        assertEquals("[X]", Pixel.BLACK.toString());
    }

    @Test
    void merge() {
        assertEquals(null, Pixel.merge(null, null));
        assertEquals(Pixel.WHITE, Pixel.merge(null, Pixel.WHITE));
        assertEquals(Pixel.BLACK, Pixel.merge(null, Pixel.BLACK));
        assertEquals(Pixel.WHITE, Pixel.merge(Pixel.WHITE, null));
        assertEquals(Pixel.WHITE, Pixel.merge(Pixel.WHITE, Pixel.WHITE));
        assertEquals(Pixel.WHITE, Pixel.merge(Pixel.WHITE, Pixel.BLACK));
        assertEquals(Pixel.BLACK, Pixel.merge(Pixel.BLACK, null));
        assertEquals(Pixel.BLACK, Pixel.merge(Pixel.BLACK, Pixel.WHITE));
        assertEquals(Pixel.BLACK, Pixel.merge(Pixel.BLACK, Pixel.BLACK));
    }
}