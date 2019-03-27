package me.ilnicki.bg.engine.system;

import me.ilnicki.bg.engine.system.container.ComponentContainer;

public class SystemManager extends ComponentContainer {
    private final Runner runner;

    public SystemManager(Runner runner) {
        this.runner = runner;
        singleton(SystemManager.class, this);
    }

    public void reset() {
        runner.reset();
    }

    public void stop() {
        runner.stop();
    }
}
