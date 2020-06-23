package me.ilnicki.bg.core.state.buttons;

public interface ButtonsState<E extends Enum<E>> {
    int getValue(E key);

    boolean isPressed(E key);
}
