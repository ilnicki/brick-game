package me.ilnicki.bg.core.game.test;

import me.ilnicki.bg.core.game.Game;
import me.ilnicki.bg.core.machine.Field;
import me.ilnicki.bg.core.machine.Layer;
import me.ilnicki.bg.core.machine.Machine.Parameters;
import me.ilnicki.bg.core.machine.keyboard.Keyboard;
import me.ilnicki.bg.core.machine.keyboard.Keyboard.CtrlKey;
import me.ilnicki.bg.core.machine.keyboard.Keyboard.CtrlKeyMap;
import me.ilnicki.bg.core.pixelmatrix.ArrayPixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.MatrixUtils;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.core.system.container.Args;
import me.ilnicki.bg.core.system.container.Inject;
import me.ilnicki.bg.core.system.processors.GameManager;

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
    private Parameters params;

    private PixelMatrix main;

    @Inject
    public TestGame(Field field) {
        field.getLayers().add(new Layer(main = new ArrayPixelMatrix(10, 20)));
    }

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
        MatrixUtils.clear(main);

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
                    main.setPixel(cursorX + j,
                            cursorY + i,
                            numMatrix.getPixel(j, i));

            cursorX = cursorX + numMatrix.getWidth() + 1;

            if (cursorX + numMatrix.getWidth() + 1 > main.getWidth()) {
                cursorX = 1;
                cursorY = cursorY - 7;
            }
        }
    }
}
