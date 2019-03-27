package me.ilnicki.bg.engine.system;

public interface Holder<T> {
    T get();
    void set(T value);
}
