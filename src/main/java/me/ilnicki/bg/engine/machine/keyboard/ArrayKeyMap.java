package me.ilnicki.bg.engine.machine.keyboard;

import java.util.Arrays;

public class ArrayKeyMap<E extends Enum<E>> implements UpdatableKeyMap<E> {
    private int[] states;

    ArrayKeyMap(Class<E> enumClass) {
        states = new int[enumClass.getEnumConstants().length];

        Arrays.fill(states, -1);
    }

    public int getState(E key) {
        return states[key.ordinal()];
    }

    public boolean isPressed(E key) {
        return getState(key) > -1;
    }

    public void update(E key, boolean state) {
        states[key.ordinal()] = state ? getState(key) + 1 : -1;
    }
}
