package me.ilnicki.bg.core.state.buttons;

public interface UpdatableButtonsState<E extends Enum<E>> extends ButtonsState<E> {
    void update(E key, boolean state);
}
