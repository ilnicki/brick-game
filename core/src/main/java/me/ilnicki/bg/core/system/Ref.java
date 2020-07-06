package me.ilnicki.bg.core.system;

public interface Ref<T> {
  T get();

  void set(T value);
}
