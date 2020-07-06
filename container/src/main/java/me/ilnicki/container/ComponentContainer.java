package me.ilnicki.container;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import me.ilnicki.container.provider.LinkProvider;
import me.ilnicki.container.provider.ObjectProvider;
import me.ilnicki.container.provider.Provider;
import me.ilnicki.container.provider.SingletonProvider;

public class ComponentContainer implements Container {
  private final Map<Class, Provider> components = new ConcurrentHashMap<>();

  @Override
  public <T> void bind(Class<? super T> abstractClass, Provider<T> provider) {
    components.put(abstractClass, provider);
  }

  @Override
  public <T> void bind(Class<? super T> abstractClass, Class<T> concreteClass) {
    Provider provider = new ComponentProvider<>(concreteClass);

    components.put(abstractClass, provider);
    components.put(concreteClass, provider);
  }

  @Override
  public <T> void singleton(Class<? super T> abstractClass, Provider<T> provider) {
    components.put(abstractClass, new SingletonProvider<>(provider));
  }

  @Override
  public <T> void singleton(Class<? super T> abstractClass, Class<T> concreteClass) {
    Provider provider = new SingletonProvider<>(new ComponentProvider<>(concreteClass));

    components.put(abstractClass, provider);
    components.put(concreteClass, provider);
  }

  @Override
  public <T> void singleton(Class<? super T> abstractClass, T concreteObject) {
    components.put(abstractClass, new SingletonProvider<>(new ObjectProvider<>(concreteObject)));
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
  public <T> T get(Class<? extends T> desiredClass, String[] args) throws ProvisionException {
    Provider<T> provider =
        components.getOrDefault(desiredClass, new ComponentProvider<>(desiredClass));
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

  private Object[] prepareExecutableArguments(Executable exec, boolean allowEmpty) {
    if (exec.isAnnotationPresent(Inject.class)) {
      Class[] paramTypes = exec.getParameterTypes();

      if (!allowEmpty && paramTypes.length == 0) {
        throw new ProvisionException(
            String.format(
                "Empty parameters list in injection method %s. Use @PostConstructor instead.",
                exec.getName()));
      }

      Annotation[][] paramAnnotations = exec.getParameterAnnotations();

      Object[] arguments = new Object[paramTypes.length];

      for (int i = 0; i < paramTypes.length; i++) {
        arguments[i] =
            get(
                Arrays.stream(paramAnnotations[i])
                    .filter(Type.class::isInstance)
                    .map(Type.class::cast)
                    .map(Type::value)
                    .findFirst()
                    .orElse(paramTypes[i]),
                Arrays.stream(paramAnnotations[i])
                    .filter(Args.class::isInstance)
                    .map(Args.class::cast)
                    .map(Args::value)
                    .findFirst()
                    .orElse(NO_ARGS));
      }

      return arguments;
    }

    return null;
  }

  private <T> T instantiate(Class<? extends T> instanceClass) throws ProvisionException {
    @SuppressWarnings("unchecked")
    Constructor<T>[] constructors = (Constructor<T>[]) instanceClass.getConstructors();
    Constructor<T> desiredConstructor = null;

    // Double jump usage in a single loop. Seems legit. Well done.
    for (Constructor<T> constructor : constructors) {
      if (constructor.getParameterCount() == 0) {
        desiredConstructor = constructor;
        continue;
      }

      if (constructor.isAnnotationPresent(Inject.class)) {
        desiredConstructor = constructor;
        break;
      }
    }

    if (desiredConstructor == null) {
      throw new ProvisionException(
          String.format("No suitable constructor found for %s", instanceClass.getCanonicalName()));
    }

    Object[] arguments = prepareExecutableArguments(desiredConstructor, true);

    try {
      return desiredConstructor.newInstance(arguments);
    } catch (InstantiationException
        | IllegalAccessException
        | InvocationTargetException exception) {
      throw new ProvisionException(
          String.format("Can not inject into constructor of %s.", instanceClass.getCanonicalName()),
          exception);
    }
  }

  private <T> T injectIntoFields(T instance) throws ProvisionException {
    for (Field field : instance.getClass().getDeclaredFields()) {
      if (field.isAnnotationPresent(Inject.class)) {
        String[] fieldArgs = NO_ARGS;

        if (field.isAnnotationPresent(Args.class)) {
          fieldArgs = field.getAnnotation(Args.class).value();
        }

        try {
          field.setAccessible(true);
          field.set(instance, get(field.getType(), fieldArgs));
        } catch (IllegalAccessException | ProvisionException exception) {
          String message =
              String.format(
                  "Can not inject into field %s of %s.",
                  field.getName(), instance.getClass().getCanonicalName());

          if (field.getAnnotation(Inject.class).required()) {
            throw new ProvisionException(message, exception);
          } else {
            System.err.println(message);
          }
        }
      }
    }

    return instance;
  }

  private <T> T injectIntoMethods(T instance) throws ProvisionException {
    for (Method method : instance.getClass().getDeclaredMethods()) {
      try {
        Object[] arguments = prepareExecutableArguments(method, false);

        if (arguments != null) {
          method.setAccessible(true);
          method.invoke(instance, arguments);
        }
      } catch (InvocationTargetException | IllegalAccessException | ProvisionException exception) {
        String message =
            String.format(
                "Can not inject into into %s of %s.",
                method.getName(), instance.getClass().getCanonicalName());

        if (method.getAnnotation(Inject.class).required()) {
          throw new ProvisionException(message, exception);
        } else {
          System.err.println(message);
        }
      }
    }

    return instance;
  }

  private <T> T postConstruct(T instance) throws ProvisionException {
    for (Method method : instance.getClass().getDeclaredMethods()) {
      if (method.isAnnotationPresent(PostConstructor.class)) {
        if (method.getParameterCount() == 0) {
          try {
            method.setAccessible(true);
            method.invoke(instance);
          } catch (InvocationTargetException
              | IllegalAccessException
              | ProvisionException exception) {
            String message =
                String.format(
                    "Can not inject into into %s of %s.",
                    method.getName(), instance.getClass().getCanonicalName());

            throw new ProvisionException(message, exception);
          }
        } else {
          throw new ProvisionException("Can not run PostConstructor with parameters.");
        }
      }
    }

    return instance;
  }

  private class ComponentProvider<T> implements Provider<T> {
    private final Class<? extends T> concreteClass;

    private ComponentProvider(Class<? extends T> concreteClass) {
      this.concreteClass = concreteClass;
    }

    @Override
    public T provide(Class<? extends T> desiredClass, String[] args) throws ProvisionException {
      return postConstruct(injectIntoMethods(injectIntoFields(instantiate(concreteClass))));
    }
  }
}
