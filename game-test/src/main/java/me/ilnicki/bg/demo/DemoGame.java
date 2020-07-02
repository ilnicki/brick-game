package me.ilnicki.bg.demo;

import me.ilnicki.bg.core.game.AbstractGame;
import me.ilnicki.bg.core.pixelmatrix.ArrayPixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.MutablePixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Matrices;
import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.math.Vector;
import me.ilnicki.bg.core.pixelmatrix.layering.Layer;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.core.state.Field;
import me.ilnicki.bg.core.state.Helper;
import me.ilnicki.bg.core.state.buttons.ButtonsState;
import me.ilnicki.bg.core.state.buttons.GameButton;
import me.ilnicki.bg.core.state.parameters.IntParameter;
import me.ilnicki.bg.core.system.processors.gamemanager.GameArgument;
import me.ilnicki.bg.core.system.processors.gamemanager.GameManager;
import me.ilnicki.container.Args;
import me.ilnicki.container.Inject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.stream.Collectors;

public class DemoGame extends AbstractGame {
    private final MutablePixelMatrix field;
    private final MutablePixelMatrix helper;
    @Inject
    @Args({"internal", "assets.sprites.characters"})
    private PixelMatrixLoader matrixLoader;
    @Inject
    private ButtonsState<GameButton> buttons;
    @Inject
    private GameManager gameManager;

    @Inject
    @Args("score")
    private IntParameter score;

    @Inject
    @Args("hiscore")
    private IntParameter hiscore;

    @Inject
    private GameArgument argument;

    private long tick = 0;

    @Inject
    public DemoGame(Field field, Helper helper) {
        field.getLayers().add(new Layer<>(this.field = new ArrayPixelMatrix(10, 20)));
        helper.getLayers().add(new Layer<>(this.helper = new ArrayPixelMatrix(4, 4)));
    }

    @Override
    public void update(int delta) {
        String buttonsPressed = EnumSet.allOf(GameButton.class)
                .stream()
                .filter(buttons::isPressed)
                .map(Object::toString)
                .collect(Collectors.joining(" "));

        if (buttonsPressed.length() > 0) {
            System.out.printf("Tick: %d; Delta: %d; Buttons: %s;\n", tick , delta, buttonsPressed);
        }

        if (buttons.isPressed(GameButton.UP)
                && buttons.isPressed(GameButton.LEFT)
                && buttons.isPressed(GameButton.RIGHT)
                && buttons.isPressed(GameButton.DOWN)) {
            quit();
        }

        if (buttons.getValue(GameButton.UP) == 5) {
            score.inc();
            hiscore.dec();
        }

        if (buttons.getValue(GameButton.DOWN) == 5) {
            score.dec();
            hiscore.inc();
        }

        printTime();
        drawArg();

        tick++;
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
            PixelMatrix numMatrix = matrixLoader.get(digit);

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
