package me.ilnicki.bg.demo;

import java.util.ArrayList;
import me.ilnicki.bg.core.game.Game;
import me.ilnicki.bg.core.game.Manifest;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.animation.Frame;
import me.ilnicki.bg.core.pixelmatrix.animation.Player;
import me.ilnicki.bg.core.pixelmatrix.animation.Track;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.container.Args;
import me.ilnicki.container.Inject;
import me.ilnicki.container.PostConstructor;

public class DemoManifest implements Manifest {
  @Inject
  @Args({"internal", "assets.sprites.demo"})
  private PixelMatrixLoader matrixLoader;

  private Player previewPlayer;

  @PostConstructor
  private void init() {
    previewPlayer =
        new Player(
            new Track(
                new ArrayList<Frame>() {
                  {
                    add(new Frame(matrixLoader.get("preview0"), 8));
                    add(new Frame(matrixLoader.get("preview1"), 8));
                    add(new Frame(matrixLoader.get("preview2"), 8));
                    add(new Frame(matrixLoader.get("preview3"), 8));
                  }
                },
                true));
  }

  @Override
  public String getName() {
    return "Demo Game";
  }

  @Override
  public String getVersion() {
    return "0.0.1";
  }

  @Override
  public String getAuthor() {
    return "Dmytro Ilnicki";
  }

  @Override
  public String getDescription() {
    return "Demo Game";
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
    previewPlayer.next();
    return previewPlayer;
  }

  @Override
  public Class<? extends Game> getGameClass() {
    return DemoGame.class;
  }
}
