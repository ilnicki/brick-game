package me.ilnicki.container.provider;

import me.ilnicki.container.ProvisionException;

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
