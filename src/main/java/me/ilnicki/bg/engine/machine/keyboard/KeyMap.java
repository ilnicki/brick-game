package me.ilnicki.bg.engine.machine.keyboard;

public interface KeyMap<E extends Enum<E>> {
    int getState(E key);
    boolean isPressed(E key);
}
