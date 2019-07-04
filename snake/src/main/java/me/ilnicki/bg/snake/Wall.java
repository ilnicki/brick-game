package me.ilnicki.bg.snake;

import me.ilnicki.bg.core.pixelmatrix.MatrixUtils;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Point;

public class Wall extends Entity {
    private static final PixelMatrix sprite = MatrixUtils.fromString("#");

    Wall(Point pos) {
        super(pos);
    }

    @Override
    public PixelMatrix getSprite() {
        return sprite;
    }
}
