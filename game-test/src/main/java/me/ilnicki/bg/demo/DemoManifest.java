package me.ilnicki.bg.demo;

import java.util.ArrayList;

import me.ilnicki.bg.core.game.Game;
import me.ilnicki.bg.core.game.Manifest;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.animation.Frame;
import me.ilnicki.bg.core.pixelmatrix.animation.Animation;
import me.ilnicki.bg.core.pixelmatrix.animation.Track;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.container.Inject;
import me.ilnicki.container.PostConstructor;

public class DemoManifest implements Manifest {
  @Inject({"assets.sprites.demo.logo"})
  private PixelMatrix logo;

  @Inject({"assets.sprites.demo.preview"})
  private Animation preview;

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
    return logo;
  }

  @Override
  public PixelMatrix getPreview() {
    return preview.next();
  }

  @Override
  public Class<? extends Game> getGameClass() {
    return DemoGame.class;
  }
}
