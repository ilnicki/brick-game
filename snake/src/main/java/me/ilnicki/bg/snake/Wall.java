package me.ilnicki.bg.snake;

import me.ilnicki.bg.core.pixelmatrix.MatrixUtils;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;

public class Wall extends Entity {
    private static final PixelMatrix sprite = MatrixUtils.fromString("#");

    public Wall(int posX, int posY) {
        super(posX, posY);
    }

    @Override
    public PixelMatrix getSprite() {
        return sprite;
    }
}
