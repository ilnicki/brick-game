package me.ilnicki.bg.snake;

import me.ilnicki.bg.core.math.Vector;
import me.ilnicki.bg.core.pixelmatrix.Matrices;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;

class Food extends Entity {
  private static final PixelMatrix sprite = Matrices.fromString("#");
  private static final PixelMatrix spriteBlink = Matrices.fromString(" ");

  private static final int blinkOn = 7;
  private static final int blinkTime = 3;

  private int callCounter = 0;

  Food(Vector pos) {
    super(pos);
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
