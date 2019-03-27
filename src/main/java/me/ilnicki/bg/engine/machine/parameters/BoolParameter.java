package me.ilnicki.bg.engine.machine.parameters;

import me.ilnicki.bg.engine.system.Holder;

public class BoolParameter implements Holder<Boolean> {
    private boolean value;

    public BoolParameter() {
        this(false);
    }

    public BoolParameter(boolean value) {
        this.value = value;
    }

    public Boolean get() {
        return value;
    }

    public void set(Boolean value) {
        this.value = value;
    }

    public void toggle() {
        value ^= true;
    }
}
