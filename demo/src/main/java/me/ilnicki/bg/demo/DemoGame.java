package me.ilnicki.bg.demo;

import me.ilnicki.bg.core.game.Game;
import me.ilnicki.bg.core.state.Field;
import me.ilnicki.bg.core.state.Helper;
import me.ilnicki.bg.core.state.State.Parameters;
import me.ilnicki.bg.core.state.keyboard.Keyboard;
import me.ilnicki.bg.core.state.keyboard.Keyboard.CtrlKey;
import me.ilnicki.bg.core.state.keyboard.Keyboard.CtrlKeyMap;
import me.ilnicki.bg.core.pixelmatrix.ArrayPixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.EditablePixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Matrices;
import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Vector;
import me.ilnicki.bg.core.pixelmatrix.layering.Layer;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.core.system.processors.GameArgument;
import me.ilnicki.bg.core.system.processors.GameManager;
import me.ilnicki.container.Args;
import me.ilnicki.container.Inject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.stream.Collectors;

public class DemoGame implements Game {
    private final EditablePixelMatrix field;
    private final EditablePixelMatrix helper;
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

    @Inject
    public DemoGame(Field field, Helper helper) {
        field.getLayers().add(new Layer<>(this.field = new ArrayPixelMatrix(10, 20)));
        helper.getLayers().add(new Layer<>(this.helper = new ArrayPixelMatrix(4, 4)));
    }

    @Override
    public void update(long tick) {
        String keys = EnumSet.allOf(CtrlKey.class)
                .stream()
                .filter(keyMap::isPressed)
                .map(Object::toString)
                .collect(Collectors.joining(" "));

        if (keys.length() > 0) {
            System.out.println("Tick: " + tick + "; Keys: " + keys);
        }

        if (keyMap.isPressed(CtrlKey.UP)
                && keyMap.isPressed(CtrlKey.LEFT)
                && keyMap.isPressed(CtrlKey.RIGHT)
                && keyMap.isPressed(CtrlKey.DOWN)) {
            gameManager.exitGame();
        }

        if (keyMap.getValue(CtrlKey.UP) == 5) {
            params.score.inc();
            params.hiscore.dec();
        }

        if (keyMap.getValue(Keyboard.CtrlKey.DOWN) == 5) {
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
        Matrices.clear(field);
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

                    field.setPixel(point.add(new Vector(cursorX, cursorY)), numMatrix.getPixel(point));
                }
            }

            cursorX = cursorX + numMatrix.getWidth() + 1;
        }
    }
}
