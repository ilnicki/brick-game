package me.ilnicki.container.provider;

import me.ilnicki.container.*;
import me.ilnicki.container.Type;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;

import static me.ilnicki.container.Container.NO_ARGS;

public class ComponentProvider<T> implements Provider<T> {
  private final Class<? extends T> concreteClass;
  private final Injector injector;

  public ComponentProvider(Class<? extends T> concreteClass, Injector injector) {
    this.concreteClass = concreteClass;
    this.injector = injector;
  }

  @Override
  public T provide(Class<? extends T> desiredClass, String[] args)
      throws ProvisionException {
    return
        injector.postConstruct(
            injector.injectIntoMethods(
                injector.injectIntoFields(
                    injector.instantiate(concreteClass, args),
                    args),
                args)
        );
  }
}
