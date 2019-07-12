package me.ilnicki.container.provider;

import me.ilnicki.container.ProvisionException;

public interface Factory<T> extends Provider<T> {
    T produce(String[] args) throws ProvisionException;

    default T provide(Class<? extends T> desiredClass, String[] args) throws ProvisionException {
        return produce(args);
    }
}
