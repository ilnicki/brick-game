package me.ilnicki.container;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import me.ilnicki.container.provider.*;

public class ComponentContainer implements Container {
  private final Map<Class, Provider> components = new ConcurrentHashMap<>();

  @Override
  public <T> void bind(Class<? super T> abstractClass, Provider<T> provider) {
    components.put(abstractClass, provider);
  }

  @Override
  public <T> void bind(Class<? super T> abstractClass, Class<T> concreteClass) {
    Provider<?> provider = new ComponentProvider<>(concreteClass, this);

    components.put(abstractClass, provider);
    components.put(concreteClass, provider);
  }

  @Override
  public <T> void singleton(Class<? super T> abstractClass, Provider<T> provider) {
    components.put(abstractClass, new SingletonProvider<>(provider));
  }

  @Override
  public <T> void singleton(Class<? super T> abstractClass, Class<T> concreteClass) {
    Provider<?> provider = new SingletonProvider<>(
        new ComponentProvider<>(concreteClass, this)
    );

    components.put(abstractClass, provider);
    components.put(concreteClass, provider);
  }

  @Override
  public <T> void singleton(Class<? super T> abstractClass, T concreteObject) {
    components.put(abstractClass, new SingletonProvider<>(
        new ObjectProvider<>(concreteObject))
    );
  }

  @Override
  public <T> void share(T sharedObject) {
    components.put(sharedObject.getClass(), new ObjectProvider<>(sharedObject));
  }

  @Override
  public <T> void link(Class<? super T> fromClass, Class<T> toClass) {
    components.put(fromClass, new LinkProvider<>(this, toClass));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T get(Class<? extends T> desiredClass, String... args) throws ProvisionException {
    Provider<T> provider = components.getOrDefault(desiredClass,
        new ComponentProvider<>(desiredClass, this)
    );
    return provider.provide(desiredClass, args);
  }

  @SuppressWarnings("unchecked")
  public <T> Set<T> getCompatible(Class<? extends T> baseClass) throws ProvisionException {
    return components.entrySet().stream()
        .filter(e -> baseClass.isAssignableFrom(e.getKey()))
        .map(Map.Entry::getValue)
        .distinct()
        .map(r -> r.provide(baseClass))
        .map(baseClass::cast)
        .collect(Collectors.toSet());
  }
}
