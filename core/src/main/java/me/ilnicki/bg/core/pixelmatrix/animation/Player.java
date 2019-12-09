package me.ilnicki.bg.core.pixelmatrix.animation;

import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Vector;

public class Player implements PixelMatrix {
    private final TrackIterator trackIterator;
    private int currentDraw = 0;

    public Player(Track track) {
        this.trackIterator = track.trackIterator();
    }

    @Override
    public int getWidth() {
        return trackIterator.current().getData().getWidth();
    }

    @Override
    public int getHeight() {
        return trackIterator.current().getData().getHeight();
    }

    @Override
    public Pixel getPixel(Vector point) {
        return trackIterator.current().getData().getPixel(point);
    }

    public void next() {
        if (trackIterator.current() != null && currentDraw + 1 < trackIterator.current().getLength()) {
            currentDraw++;
        } else {
            if (trackIterator.hasNext()) {
                trackIterator.next();
                currentDraw = 0;
            }
        }
    }
}
