package me.ilnicki.bg.core.game.splash;

import java.util.Arrays;

import me.ilnicki.bg.core.game.AbstractGame;
import me.ilnicki.bg.core.math.Vector;
import me.ilnicki.bg.core.pixelmatrix.*;
import me.ilnicki.bg.core.pixelmatrix.animation.Animation;
import me.ilnicki.bg.core.pixelmatrix.layering.Layer;
import me.ilnicki.bg.core.pixelmatrix.modifying.Invert;
import me.ilnicki.bg.core.pixelmatrix.modifying.Stack;
import me.ilnicki.bg.core.state.Field;
import me.ilnicki.bg.core.state.buttons.ButtonsState;
import me.ilnicki.bg.core.state.buttons.GameButton;
import me.ilnicki.bg.core.system.processors.gamemanager.GameManager;
import me.ilnicki.container.Inject;

public class Splash extends AbstractGame {
  @Inject({"assets.sprites.splash.9999in1"})
  private PixelMatrix splashText;

  @Inject
  private ButtonsState<GameButton> buttons;

  @Inject
  private GameManager gameManager;

  @Inject
  private Field field;

  private final MutablePixelMatrix invertMask = new ArrayPixelMatrix(10, 20);

  private final Animation spiral = new Spiral(10, 20);
  private final Animation blink = new ScreenBlink(10, 20, Pixel.BLACK);

  @Override
  public void load() {
    field.getLayers().add(new Layer<>(
        new Invert(
            new Stack(
                blink,
                new Invert(splashText, spiral)
            ),
            invertMask
        )
    ));

    super.load();
  }

  @Override
  public void update(int delta) {
    if (Arrays.stream(GameButton.values())
        .anyMatch(key -> buttons.isPressed(key))
    ) {
      quit();
      return;
    }

    if (spiral.hasNext()) {
      spiral.next();
    } else {
      if (blink.hasNext()) {
        blink.next();
      } else {
        spiral.reset();
        blink.reset();
        invert();
      }
    }
  }

  private void invert() {
    if (invertMask.getPixel(new Vector(0, 0)) == null) {
      Matrices.fill(invertMask, Pixel.BLACK);
    } else {
      Matrices.fill(invertMask, null);
    }
  }
}
