package me.ilnicki.bg.engine.system.container.provider;

import me.ilnicki.bg.engine.system.container.ProvisionException;

public interface FactoryProvider<T> extends Provider<T> {
    T produce(String[] args) throws ProvisionException;

    default T provide(Class<? extends T> desiredClass, String[] args) throws ProvisionException {
        return produce(args);
    }
}
