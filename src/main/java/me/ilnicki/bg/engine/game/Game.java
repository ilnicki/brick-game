package me.ilnicki.bg.engine.game;

import me.ilnicki.bg.engine.system.Module;

public interface Game extends Module {
    default void save() {
    }

    default void recover() {
    }
}
