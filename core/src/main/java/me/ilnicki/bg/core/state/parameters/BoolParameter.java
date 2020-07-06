package me.ilnicki.bg.core.state.parameters;

import me.ilnicki.bg.core.system.Ref;

public class BoolParameter implements Ref<Boolean> {
  private Boolean value;

  public BoolParameter() {
    this(false);
  }

  public BoolParameter(Boolean value) {
    this.value = value;
  }

  public Boolean get() {
    return value;
  }

  public void set(Boolean value) {
    this.value = value;
  }

  public void toggle() {
    value = !value;
  }

  @Override
  public String toString() {
    return Boolean.toString(value);
  }
}
