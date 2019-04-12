package me.ilnicki.bg.engine.game.test;

import me.ilnicki.bg.engine.game.Game;
import me.ilnicki.bg.engine.machine.Field;
import me.ilnicki.bg.engine.machine.Machine.Parameters;
import me.ilnicki.bg.engine.machine.keyboard.KeyMap;
import me.ilnicki.bg.engine.machine.keyboard.Keyboard;
import me.ilnicki.bg.engine.machine.keyboard.Keyboard.CtrlKey;
import me.ilnicki.bg.engine.machine.keyboard.Keyboard.CtrlKeyMap;
import me.ilnicki.bg.engine.pixelmatrix.MatrixUtils;
import me.ilnicki.bg.engine.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.engine.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.engine.system.container.Args;
import me.ilnicki.bg.engine.system.container.Inject;
import me.ilnicki.bg.engine.system.processors.GameManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TestGame implements Game {
    @Inject
    @Args({"internal", "assets.sprites.characters"})
    private PixelMatrixLoader matrixLoader;

    @Inject
    private CtrlKeyMap keyMap;

    @Inject
    private GameManager gameManager;

    @Inject
    private Field field;

    @Inject
    private Parameters params;

    @Override
    public void update(long tick) {
        System.out.print("Tick: " + tick + "; Keys: ");

        for (CtrlKey key : CtrlKey.values()) {
            if (keyMap.isPressed(key)) {
                System.out.print(key + " ");
            }
        }

        System.out.println();

        if (keyMap.isPressed(CtrlKey.UP)
                && keyMap.isPressed(CtrlKey.LEFT)
                && keyMap.isPressed(CtrlKey.RIGHT)) {
            gameManager.exitGame();
        }

        if (keyMap.getState(CtrlKey.UP) == 5) {
            params.score.inc();
            params.hiscore.dec();
        }

        if (keyMap.getState(Keyboard.CtrlKey.DOWN) == 5) {
            params.score.dec();
            params.hiscore.inc();
        }

        printTime();
    }

    private void printTime() {
        MatrixUtils.clear(field);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String date = sdf.format(cal.getTime());
        String[] nums = date.split("");

        int cursorX = 1;
        int cursorY = 15;

        for (String num : nums) {
            PixelMatrix numMatrix = matrixLoader.load(num, true);

            for (int i = 0; i < numMatrix.getHeight(); i++)
                for (int j = 0; j < numMatrix.getWidth(); j++)
                    field.setPixel(cursorX + j,
                            cursorY + i,
                            numMatrix.getPixel(j, i));

            cursorX = cursorX + numMatrix.getWidth() + 1;

            if (cursorX + numMatrix.getWidth() + 1 > field.getWidth()) {
                cursorX = 1;
                cursorY = cursorY - 7;
            }
        }
    }
}
