package me.ilnicki.bg.core.system;

public interface Module {
    default void load() {
    }

    default void update(int delta) {
    }

    default void stop() {
    }
}
