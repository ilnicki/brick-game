package me.ilnicki.bg.snake;

import me.ilnicki.bg.core.math.Vector;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.animation.Animation;
import me.ilnicki.container.Inject;

class Food extends Entity {
  @Inject({"assets.sprites.snake.food"})
  private Animation sprite;

  Food(Vector pos) {
    super(pos);
  }

  @Override
  public PixelMatrix getSprite() {
    return sprite.next();
  }
}
