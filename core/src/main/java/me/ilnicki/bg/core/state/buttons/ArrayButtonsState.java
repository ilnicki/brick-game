package me.ilnicki.bg.core.state.buttons;

import java.util.Arrays;

public class ArrayButtonsState<E extends Enum<E>> implements UpdatableButtonsState<E> {
  private final int[] states;

  public ArrayButtonsState(Class<E> enumClass) {
    states = new int[enumClass.getEnumConstants().length];

    Arrays.fill(states, -1);
  }

  public int getValue(E button) {
    return states[button.ordinal()];
  }

  public boolean isPressed(E button) {
    return getValue(button) > -1;
  }

  public void update(E button, boolean state) {
    states[button.ordinal()] = state ? getValue(button) + 1 : -1;
  }
}
