package me.ilnicki.bg.core.system.container.provider;

import me.ilnicki.bg.core.system.container.ProvisionException;

public class SingletonProvider<T> implements Provider<T> {
    private Provider<T> provider;
    private T instance;

    public SingletonProvider(Provider<T> provider) {
        this.provider = provider;
    }

    @Override
    public T provide(Class<? extends T> desiredClass, String[] args) throws ProvisionException {
        if (instance == null) {
            instance = provider.provide(desiredClass, args);
            provider = null;
        }

        return instance;
    }
}
