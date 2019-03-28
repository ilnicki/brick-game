package me.ilnicki.bg.engine.system.container.provider;

import me.ilnicki.bg.engine.system.container.ProvisionException;

public class ObjectProvider<T> implements Provider<T> {
    private final T instance;

    public ObjectProvider(T instance) {
        this.instance = instance;
    }

    @Override
    public T provide(Class<? extends T> desiredClass, String[] args) throws ProvisionException {
        return instance;
    }
}
