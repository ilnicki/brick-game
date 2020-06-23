package me.ilnicki.bg.core.game;

import me.ilnicki.bg.core.system.Module;

public interface Game extends Module {
    Status getStatus();

    enum Status {
        LOADING,
        RUNNING,
        FINISHING,
    }
}
