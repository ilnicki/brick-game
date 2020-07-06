package me.ilnicki.bg.core.game.splash;

import java.util.Arrays;
import me.ilnicki.bg.core.game.AbstractGame;
import me.ilnicki.bg.core.math.Vector;
import me.ilnicki.bg.core.pixelmatrix.ArrayPixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Matrices;
import me.ilnicki.bg.core.pixelmatrix.MutablePixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.layering.Layer;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.core.pixelmatrix.modifying.Invert;
import me.ilnicki.bg.core.state.Field;
import me.ilnicki.bg.core.state.buttons.ButtonsState;
import me.ilnicki.bg.core.state.buttons.GameButton;
import me.ilnicki.bg.core.system.processors.gamemanager.GameManager;
import me.ilnicki.container.Args;
import me.ilnicki.container.Inject;

public class Splash extends AbstractGame {
  @Inject
  @Args({"internal", "assets.sprites.splash"})
  private PixelMatrixLoader matrixLoader;

  @Inject private ButtonsState<GameButton> buttons;

  @Inject private GameManager gameManager;

  @Inject private Field field;

  private final MutablePixelMatrix mask = new ArrayPixelMatrix(10, 20);
  private SpiralGenerator spiral = new SpiralGenerator(mask.getWidth(), mask.getHeight());

  @Override
  public void load() {
    field.getLayers().add(new Layer<>(new Invert(matrixLoader.get("9999in1"), mask)));

    super.load();
  }

  @Override
  public void update(int delta) {
    if (Arrays.stream(GameButton.values()).anyMatch(key -> buttons.isPressed(key))
        || spiral == null) {
      quit();
      return;
    }

    final Vector pos = spiral.next();

    if (pos != null) {
      mask.setPixel(pos, Pixel.BLACK);
    } else {
      spiral = null;
      Matrices.fill(mask, null);
    }
  }
}
