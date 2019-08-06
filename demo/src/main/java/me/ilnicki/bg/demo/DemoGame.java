package me.ilnicki.bg.demo;

import me.ilnicki.bg.core.game.Game;
import me.ilnicki.bg.core.machine.Field;
import me.ilnicki.bg.core.machine.Helper;
import me.ilnicki.bg.core.pixelmatrix.layering.Layer;
import me.ilnicki.bg.core.machine.Machine.Parameters;
import me.ilnicki.bg.core.machine.keyboard.Keyboard;
import me.ilnicki.bg.core.machine.keyboard.Keyboard.CtrlKey;
import me.ilnicki.bg.core.machine.keyboard.Keyboard.CtrlKeyMap;
import me.ilnicki.bg.core.pixelmatrix.*;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.container.Args;
import me.ilnicki.container.Inject;
import me.ilnicki.bg.core.system.processors.GameArgument;
import me.ilnicki.bg.core.system.processors.GameManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DemoGame implements Game {
    @Inject
    @Args({"internal", "assets.sprites.characters"})
    private PixelMatrixLoader matrixLoader;

    @Inject
    private CtrlKeyMap keyMap;

    @Inject
    private GameManager gameManager;

    @Inject
    private Parameters params;

    @Inject
    private GameArgument argument;

    private final EditablePixelMatrix main;

    @Inject
    private Helper helper;

    @Inject
    public DemoGame(Field field) {
        field.getLayers().add(new Layer<>(main = new ArrayPixelMatrix(10, 20)));
    }

    @Override
    public void update(long tick) {
        StringBuilder sb = new StringBuilder();

        for (CtrlKey key : CtrlKey.values()) {
            if (keyMap.isPressed(key)) {
                sb.append(key).append(' ');
            }
        }

        if (sb.length() > 0) {
            System.out.println("Tick: " + tick + "; Keys: " + sb.toString());
        }

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
        drawArg();
    }

    private void drawArg() {
        int arg = argument.get();
        int area = helper.getWidth() * helper.getHeight();
        int value = arg / argument.getMax() * area;

        for (int y = 0; y < helper.getHeight(); y++) {
            for (int x = 0; x < helper.getWidth(); x++) {
                if (value-- > 0) {
                    helper.setPixel(new Vector(x, y), Pixel.BLACK);
                } else {
                    return;
                }
            }
        }
    }

    private void printTime() {
        Matrices.clear(main);
        Date date = Calendar.getInstance().getTime();

        int cursorY = 15;

        printNumber(cursorY, new SimpleDateFormat("HH").format(date));
        printNumber(cursorY - 7, new SimpleDateFormat("mm").format(date));
        printNumber(cursorY - 7 * 2, new SimpleDateFormat("ss").format(date));
    }

    private void printNumber(int cursorY, String number) {
        int cursorX = 1;
        for (String digit : number.split("")) {
            PixelMatrix numMatrix = matrixLoader.load(digit, true);

            for (int y = 0; y < numMatrix.getHeight(); y++) {
                for (int x = 0; x < numMatrix.getWidth(); x++) {
                    final Vector point = new Vector(x, y);

                    main.setPixel(point.add(new Vector(cursorX, cursorY)), numMatrix.getPixel(point));
                }
            }

            cursorX = cursorX + numMatrix.getWidth() + 1;
        }
    }
}
