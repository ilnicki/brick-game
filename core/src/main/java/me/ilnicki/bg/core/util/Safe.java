package me.ilnicki.bg.core.util;

import java.util.Optional;
import java.util.function.Supplier;

public final class Safe {
  public static <T> Optional<T> of(Supplier<T> supplier) {
    T value = null;

    try {
      value = supplier.get();
    } catch (Exception ignored) {
    }

    return Optional.ofNullable(value);
  }
}
