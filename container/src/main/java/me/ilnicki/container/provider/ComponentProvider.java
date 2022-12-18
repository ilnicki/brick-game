package me.ilnicki.container.provider;

import me.ilnicki.container.*;
import me.ilnicki.container.Type;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;

import static me.ilnicki.container.Container.NO_ARGS;

public class ComponentProvider<T> implements Provider<T> {
  private final Class<? extends T> concreteClass;
  private final Container container;

  public ComponentProvider(Class<? extends T> concreteClass, Container container) {
    this.concreteClass = concreteClass;
    this.container = container;
  }

  @Override
  public T provide(Class<? extends T> desiredClass, String[] args)
      throws ProvisionException {
    return postConstruct(
        injectIntoMethods(
            injectIntoFields(
                instantiate(concreteClass, args),
                args),
            args)
    );
  }

  private <I> I get(
      Class<? extends I> desiredClass,
      String[] args,
      String[] instanceArgs
  ) throws ProvisionException {
    if (args.length > 0 && args[0].equals("args")
        && desiredClass.getComponentType() == String.class) {
      return (I) instanceArgs;
    }

    return this.container.get(desiredClass, args);
  }


  private Object[] prepareExecutableArguments(Executable exec, String[] args, boolean allowEmpty) {
    if (exec.isAnnotationPresent(Inject.class)) {
      Class<?>[] paramTypes = exec.getParameterTypes();

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
                    .filter(Inject.class::isInstance)
                    .map(Inject.class::cast)
                    .map(Inject::value)
                    .findFirst()
                    .orElse(NO_ARGS),
                args
            );
      }

      return arguments;
    }

    return null;
  }

  private T instantiate(Class<? extends T> instanceClass, String[] args)
      throws ProvisionException {
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

    Object[] arguments = prepareExecutableArguments(desiredConstructor, args, true);

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

  private T injectIntoFields(T instance, String[] args) throws ProvisionException {
    for (Field field : instance.getClass().getDeclaredFields()) {
      if (field.isAnnotationPresent(Inject.class)) {
        String[] injectValue = field.getAnnotation(Inject.class).value();
        String[] fieldArgs = injectValue != null ? injectValue : NO_ARGS;

        try {
          field.setAccessible(true);
          field.set(instance, get(field.getType(), fieldArgs, args));
        } catch (IllegalAccessException | ProvisionException exception) {
          String message =
              String.format(
                  "Can not inject into field %s of %s.",
                  field.getName(), instance.getClass().getCanonicalName());

          if (field.isAnnotationPresent(Optional.class)) {
            System.err.println(message);
          } else {
            throw new ProvisionException(message, exception);
          }
        }
      }
    }

    return instance;
  }

  private T injectIntoMethods(T instance, String[] args) throws ProvisionException {
    for (Method method : instance.getClass().getDeclaredMethods()) {
      try {
        Object[] arguments = prepareExecutableArguments(method, args, false);

        if (arguments != null) {
          method.setAccessible(true);
          method.invoke(instance, arguments);
        }
      } catch (InvocationTargetException | IllegalAccessException |
               ProvisionException exception) {
        String message =
            String.format(
                "Can not inject into into %s of %s.",
                method.getName(), instance.getClass().getCanonicalName());

        if (method.isAnnotationPresent(Optional.class)) {
          System.err.println(message);
        } else {
          throw new ProvisionException(message, exception);
        }
      }
    }

    return instance;
  }

  private T postConstruct(T instance) throws ProvisionException {
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
}
