package me.ilnicki.bg.core.game;

import me.ilnicki.bg.core.system.Module;

public interface Game extends Module {
    default void save() {
    }

    default void recover() {
    }
}
