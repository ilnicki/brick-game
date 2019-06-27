package me.ilnicki.bg.snake;

import me.ilnicki.bg.core.pixelmatrix.MatrixUtils;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;

public class Food extends Entity {
    private static final PixelMatrix sprite = MatrixUtils.fromString("#");
    private static final PixelMatrix spriteBlink = MatrixUtils.fromString(" ");

    private static final int blinkOn = 7;
    private static final int blinkTime = 3;

    private int callCounter = 0;

    public Food(int posX, int posY) {
        super(posX, posY);
    }

    @Override
    public PixelMatrix getSprite() {
        if (++this.callCounter % blinkOn < blinkTime) {
            return spriteBlink;
        } else {
            return sprite;
        }
    }
}
