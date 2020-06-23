package me.ilnicki.bg.core.state.buttons;

import java.util.Arrays;

public class ArrayButtonsState<E extends Enum<E>> implements UpdatableButtonsState<E> {
    private final int[] states;

    public ArrayButtonsState(Class<E> enumClass) {
        states = new int[enumClass.getEnumConstants().length];

        Arrays.fill(states, -1);
    }

    public int getValue(E key) {
        return states[key.ordinal()];
    }

    public boolean isPressed(E key) {
        return getValue(key) > -1;
    }

    public void update(E key, boolean state) {
        states[key.ordinal()] = state ? getValue(key) + 1 : -1;
    }
}
