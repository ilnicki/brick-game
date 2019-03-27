package me.ilnicki.bg.engine.system.container;

import static me.ilnicki.bg.engine.system.container.Container.NO_ARGS;

public interface ComponentResolver<T> {
    T resolve(Class<? extends T> desiredClass, String[] args) throws ResolvingException;
    default T resolve(Class<? extends T> desiredClass) throws ResolvingException {
        return resolve(desiredClass, NO_ARGS);
    }
}
