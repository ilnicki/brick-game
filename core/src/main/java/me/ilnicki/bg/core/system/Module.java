package me.ilnicki.bg.core.system;

public interface Module {
    default void load() {
    }

    default void update(long tick) {
    }

    default void stop() {
    }
}
