package me.ilnicki.bg.core.system;

public interface Holder<T> {
    T get();

    void set(T value);
}
