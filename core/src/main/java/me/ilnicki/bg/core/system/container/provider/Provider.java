package me.ilnicki.bg.core.system.container.provider;

import me.ilnicki.bg.core.system.container.ProvisionException;

import static me.ilnicki.bg.core.system.container.Container.NO_ARGS;

@FunctionalInterface
public interface Provider<T> {
    T provide(Class<? extends T> desiredClass, String[] args) throws ProvisionException;

    default T provide(Class<? extends T> desiredClass) throws ProvisionException {
        return provide(desiredClass, NO_ARGS);
    }
}
