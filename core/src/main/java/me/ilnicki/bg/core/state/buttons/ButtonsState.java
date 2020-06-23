package me.ilnicki.bg.core.state.buttons;

public interface ButtonsState<E extends Enum<E>> {
    int getValue(E button);

    boolean isPressed(E button);
}
