package me.ilnicki.bg.core.machine;

import me.ilnicki.bg.core.machine.keyboard.Keyboard;
import me.ilnicki.bg.core.machine.parameters.BoolParameter;
import me.ilnicki.bg.core.machine.parameters.IntParameter;

public class Machine {
    public static class Parameters {
        public final IntParameter score = new IntParameter(0, 999999);
        public final IntParameter hiscore = new IntParameter(0, 999999);
        public final IntParameter speed = new IntParameter(1, 10);
        public final IntParameter level = new IntParameter(1, 10);
    }

    public final Parameters params = new Parameters();
    public final IntParameter volume = new IntParameter(0, 3);
    public final BoolParameter pause = new BoolParameter(false);

    private final Keyboard keyboard = new Keyboard();
    private Field field = new Field();
    private final Helper helper = new Helper();

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public Field getField() {
        return field;
    }

    public Helper getHelper() {
        return helper;
    }

    public void refreshField() {
        setField(new Field());
    }

    public void setField(Field field) {
        this.field = field;
    }
}
