package me.ilnicki.bg.core.system.processors.gamemanager;

import me.ilnicki.bg.core.game.Game;
import me.ilnicki.bg.core.system.Module;

public class LauncherWrapper implements Game {
    private final Module launcher;
    private Status status = Status.LOADING;

    public LauncherWrapper(Module launcher) {
        this.launcher = launcher;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void load() {
        launcher.load();
        status = Status.RUNNING;
    }

    @Override
    public void update(int delta) {
        launcher.update(delta);
    }

    @Override
    public void stop() {
        launcher.stop();
    }
}
