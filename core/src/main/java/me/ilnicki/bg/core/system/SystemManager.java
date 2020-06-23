package me.ilnicki.bg.core.system;

import me.ilnicki.container.ComponentContainer;
import me.ilnicki.container.Container;

public class SystemManager extends ComponentContainer implements Kernel {
    private final App app;

    public SystemManager(App app) {
        this.app = app;
        share(this);
        link(Container.class, getClass());
        link(Kernel.class, getClass());
    }

    public void reset() {
        app.reset();
    }

    public void stop() {
        app.stop();
    }
}
