package me.ilnicki.container.provider;

import static me.ilnicki.container.Container.NO_ARGS;

import me.ilnicki.container.ProvisionException;

@FunctionalInterface
public interface Provider<T> {
  T provide(Class<? extends T> desiredClass, String[] args) throws ProvisionException;

  default T provide(Class<? extends T> desiredClass) throws ProvisionException {
    return provide(desiredClass, NO_ARGS);
  }
}
