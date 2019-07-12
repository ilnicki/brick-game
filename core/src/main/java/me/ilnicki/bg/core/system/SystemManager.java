package me.ilnicki.bg.core.system;

import me.ilnicki.container.ComponentContainer;
import me.ilnicki.container.Container;

public class SystemManager extends ComponentContainer implements Kernel {
    private final Runner runner;

    public SystemManager(Runner runner) {
        this.runner = runner;
        share(this);
        link(Container.class, this.getClass());
        link(Kernel.class, this.getClass());
    }

    public void reset() {
        runner.reset();
    }

    public void stop() {
        runner.stop();
    }
}
