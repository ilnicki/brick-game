package me.ilnicki.bg.core.game.splash;

import me.ilnicki.bg.core.game.Game;
import me.ilnicki.bg.core.machine.Field;
import me.ilnicki.bg.core.machine.keyboard.Keyboard.CtrlKey;
import me.ilnicki.bg.core.machine.keyboard.Keyboard.CtrlKeyMap;
import me.ilnicki.bg.core.pixelmatrix.layering.Layer;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.core.system.processors.GameManager;
import me.ilnicki.container.Args;
import me.ilnicki.container.Inject;

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

    @Override
    public void load() {
        field.getLayers().add(new Layer<>(matrixLoader.load("9999in1", false)));
    }

    @Override
    public void update(long tick) {
        for (CtrlKey key : CtrlKey.values()) {
            if (keyMap.isPressed(key)) {
                gameManager.exitGame();
            }
        }
    }
}
