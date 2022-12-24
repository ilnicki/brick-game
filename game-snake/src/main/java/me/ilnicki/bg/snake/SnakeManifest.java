package me.ilnicki.bg.snake;

import me.ilnicki.bg.core.game.Game;
import me.ilnicki.bg.core.game.Manifest;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.animation.Animation;
import me.ilnicki.container.Inject;

public class SnakeManifest implements Manifest {
  @Inject({"assets.sprites.snake.logo"})
  private PixelMatrix logo;

  @Inject({"assets.sprites.snake.preview"})
  private Animation preview;

  @Override
  public String getAuthor() {
    return "Dmytro Ilnicki";
  }

  @Override
  public String getDescription() {
    return "Classic snake game.";
  }

  @Override
  public Class<? extends Game> getGameClass() {
    return SnakeGame.class;
  }

  @Override
  public PixelMatrix getLogo() {
    return logo;
  }

  @Override
  public String getName() {
    return "Snake Game";
  }

  @Override
  public PixelMatrix getPreview() {
    return preview.next();
  }

  @Override
  public String getVersion() {
    return "0.1";
  }

  @Override
  public String getWebSite() {
    return "http://ilnicki.me";
  }
}
