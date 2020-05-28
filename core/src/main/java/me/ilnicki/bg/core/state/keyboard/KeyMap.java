package me.ilnicki.bg.core.state.keyboard;

public interface KeyMap<E extends Enum<E>> {
    int getValue(E key);

    boolean isPressed(E key);
}
