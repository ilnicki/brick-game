package me.ilnicki.bg.core.game.splash;

import me.ilnicki.bg.core.game.Game;
import me.ilnicki.bg.core.pixelmatrix.ArrayPixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.MutablePixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Matrices;
import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.Vector;
import me.ilnicki.bg.core.pixelmatrix.layering.Layer;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.core.pixelmatrix.modifying.Invert;
import me.ilnicki.bg.core.state.Field;
import me.ilnicki.bg.core.state.keyboard.Keyboard.CtrlKey;
import me.ilnicki.bg.core.state.keyboard.Keyboard.CtrlKeyMap;
import me.ilnicki.bg.core.system.processors.GameManager;
import me.ilnicki.container.Args;
import me.ilnicki.container.Inject;

import java.util.Arrays;

public class Splash implements Game {
    @Inject
    @Args({"internal", "assets.sprites.splash"})
    private PixelMatrixLoader matrixLoader;

    @Inject
    private CtrlKeyMap keyMap;

    @Inject
    private GameManager gameManager;

    @Inject
    private Field field;

    private final MutablePixelMatrix mask = new ArrayPixelMatrix(10, 20);
    private SpiralGenerator spiral = new SpiralGenerator(mask.getWidth(), mask.getHeight());

    @Override
    public void load() {
        field.getLayers().add(
                new Layer<>(
                        new Invert(
                                matrixLoader.load("9999in1", false),
                                mask
                        ))
        );
    }

    @Override
    public void update(int delta) {
        if (Arrays.stream(CtrlKey.values()).anyMatch(key -> keyMap.isPressed(key))) {
            gameManager.exitGame();
        }

        if (spiral != null) {
            Vector pos = spiral.next();

            if (pos != null) {
                mask.setPixel(pos, Pixel.BLACK);
            } else {
                spiral = null;
                Matrices.fill(mask, null);
            }
        } else {
            gameManager.exitGame();
        }
    }
}
