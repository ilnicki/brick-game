package me.ilnicki.bg.engine.system.container.provider;

import me.ilnicki.bg.engine.system.container.Container;
import me.ilnicki.bg.engine.system.container.ProvisionException;

public class LinkProvider<T> implements Provider<T> {
    private final Container container;
    private final Class<? extends T> toClass;

    public LinkProvider(Container container, Class<? extends T> toClass) {
        this.container = container;
        this.toClass = toClass;
    }

    @Override
    public T provide(Class<? extends T> desiredClass, String[] args) throws ProvisionException {
        return container.get(toClass);
    }
}