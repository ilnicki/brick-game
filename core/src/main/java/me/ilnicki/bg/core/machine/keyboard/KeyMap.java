package me.ilnicki.bg.core.machine.keyboard;

public interface KeyMap<E extends Enum<E>> {
    int getValue(E key);

    boolean isPressed(E key);
}
