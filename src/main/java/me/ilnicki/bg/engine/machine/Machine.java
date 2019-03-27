package me.ilnicki.bg.engine.machine;

import me.ilnicki.bg.engine.machine.keyboard.Keyboard;
import me.ilnicki.bg.engine.machine.parameters.BoolParameter;
import me.ilnicki.bg.engine.machine.parameters.IntParameter;
import me.ilnicki.bg.engine.pixelmatrix.ArrayPixelMatrix;
import me.ilnicki.bg.engine.pixelmatrix.PixelMatrix;

public class Machine {
    public static class Parameters {
        public final IntParameter score = new IntParameter(0, 999999);
        public final IntParameter hiscore = new IntParameter(0, 999999);
        public final IntParameter speed = new IntParameter(1, 10);
        public final IntParameter level = new IntParameter(1, 10);
    }

    private final Parameters parameters = new Parameters();
    public final IntParameter volume = new IntParameter(0, 3);
    public final BoolParameter pause = new BoolParameter(false);

    private final Keyboard keyboard = new Keyboard();
    private Field field = new Field(10, 20);
    private final Screen screen = new Screen(10, 20, field);
    private final PixelMatrix helper = new ArrayPixelMatrix(4, 4);

    public Parameters getParameters() {
        return parameters;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public Field getField() {
        return field;
    }

    public Screen getScreen() {
        return screen;
    }

    public PixelMatrix getHelper() {
        return this.helper;
    }

    public void recreateField(int width, int height) {
        setField(new Field(width, height));
    }

    public void setField(Field field) {
        this.field = field;
        this.screen.setField(this.field);
    }
}
