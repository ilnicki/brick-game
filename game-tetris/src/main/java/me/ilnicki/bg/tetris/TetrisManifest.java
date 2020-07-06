package me.ilnicki.bg.tetris;

import me.ilnicki.bg.core.game.Game;
import me.ilnicki.bg.core.game.Manifest;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.container.Args;
import me.ilnicki.container.Inject;

public class TetrisManifest implements Manifest {
  @Inject
  @Args({"internal", "assets.sprites.tetris"})
  private PixelMatrixLoader matrixLoader;

  @Override
  public String getName() {
    return "Tetris";
  }

  @Override
  public String getVersion() {
    return "0.1";
  }

  @Override
  public String getAuthor() {
    return "Dmytro Ilnicki";
  }

  @Override
  public String getDescription() {
    return "Classic tetris game";
  }

  @Override
  public String getWebSite() {
    return "http://ilnicki.me";
  }

  @Override
  public PixelMatrix getLogo() {
    return matrixLoader.get("logo");
  }

  @Override
  public PixelMatrix getPreview() {
    return matrixLoader.get("preview");
  }

  @Override
  public Class<? extends Game> getGameClass() {
    return TetrisGame.class;
  }
}
