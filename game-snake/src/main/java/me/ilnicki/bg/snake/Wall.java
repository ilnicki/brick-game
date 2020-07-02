package me.ilnicki.bg.snake;

import me.ilnicki.bg.core.pixelmatrix.Matrices;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.math.Vector;

public class Wall extends Entity {
    private static final PixelMatrix sprite = Matrices.fromString("#");

    Wall(Vector pos) {
        super(pos);
    }

    @Override
    public PixelMatrix getSprite() {
        return sprite;
    }
}
