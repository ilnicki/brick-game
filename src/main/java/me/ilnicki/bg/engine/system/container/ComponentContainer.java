package me.ilnicki.bg.engine.system.container;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ComponentContainer implements Container {
    private Map<Class, ComponentResolver> components = new ConcurrentHashMap<>();

    @Override
    public <T> void bind(Class<? super T> abstractClass, ComponentResolver<T> resolver) {
        components.put(abstractClass, resolver);
    }

    @Override
    public <T> void bind(Class<? super T> abstractClass, Class<T> concreteClass) {
        components.put(abstractClass, new ClassResolver<>(concreteClass));
    }

    @Override
    public <T> void singleton(Class<? super T> abstractClass, ComponentResolver<T> resolver) {
        components.put(abstractClass, new SingletonResolver<>(resolver));
    }

    @Override
    public <T> void singleton(Class<? super T> abstractClass, Class<T> concreteClass) {
        components.put(abstractClass, new SingletonResolver<>(new ClassResolver<>(concreteClass)));
    }

    @Override
    public <T> void singleton(Class<? super T> abstractClass, T concreteObject) {
        components.put(abstractClass, new SingletonResolver<>((ignored, args) -> concreteObject));
    }

    @Override
    public <T> void share(T sharedObject) {
        components.put(sharedObject.getClass(), (ignored, args) -> sharedObject);
    }

    @Override
    public <T> void link(Class<? super T> fromClass, Class<T> toClass) {
        components.put(fromClass, new LinkResolver<>(toClass));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Class<? extends T> abstractClass, String[] args) {
        try {
            ComponentResolver<T> resolver = components.computeIfAbsent(abstractClass, ClassResolver::new);
            return resolver.resolve(abstractClass, args);
        } catch (ResolvingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Set<T> getCompatible(Class<? extends T> baseClass) {
        Set<T> compatible = new HashSet<>();
        components.forEach((abstractClass, resolver) -> {
            if (baseClass.isAssignableFrom(abstractClass)) {
                try {
                    compatible.add((T) resolver.resolve(baseClass));
                } catch (ResolvingException ignored) {
                }
            }
        });
        return compatible;
    }


    private class ClassResolver<T> implements ComponentResolver<T> {
        private final Class<? extends T> concreteClass;

        private ClassResolver(Class<? extends T> concreteClass) {
            this.concreteClass = concreteClass;
        }

        @Override
        public T resolve(Class<? extends T> desiredClass, String[] args) throws ResolvingException {
            System.out.printf("Getting %s.\n", concreteClass.getName());
            try {
                @SuppressWarnings("unchecked")
                Constructor<T>[] constructors = (Constructor<T>[]) concreteClass.getConstructors();
                Constructor<T> desiredConstructor = null;

                if (constructors.length == 1) {
                    desiredConstructor = constructors[0];
                } else {
                    for (Constructor<T> constructor : constructors) {
                        if (constructor.isAnnotationPresent(Inject.class)) {
                            desiredConstructor = constructor;
                            break;
                        }
                    }

                    if (desiredConstructor == null) {
                        desiredConstructor = constructors[0];
                    }
                }

                Class[] paramTypes = desiredConstructor.getParameterTypes();
                Annotation[][] paramAnnotations = desiredConstructor.getParameterAnnotations();

                Object[] parameters = new Object[paramTypes.length];

                for (int i = 0; i < paramTypes.length; i++) {
                    Class paramType = paramTypes[i];
                    Args paramArgsAnnotation = (Args) Arrays.stream(paramAnnotations[i])
                            .filter(Args.class::isInstance)
                            .findFirst()
                            .orElse(null);
                    String[] paramArgs = paramArgsAnnotation != null ? paramArgsAnnotation.value() : NO_ARGS;

                    parameters[i] = get(paramType, paramArgs);
                }

                T instance = desiredConstructor.newInstance(parameters);

                for (Field field : concreteClass.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Inject.class)) {
                        String[] fieldArgs = NO_ARGS;

                        if (field.isAnnotationPresent(Args.class)) {
                            fieldArgs = field.getAnnotation(Args.class).value();
                        }

                        field.setAccessible(true);
                        field.set(instance, get(field.getType(), fieldArgs));
                    }
                }

                return instance;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new ResolvingException(e);
            }
        }
    }

    private class SingletonResolver<T> implements ComponentResolver<T> {
        private ComponentResolver<T> resolver;
        private T instance;

        private SingletonResolver(ComponentResolver<T> resolver) {
            this.resolver = resolver;
        }

        @Override
        public T resolve(Class<? extends T> desiredClass, String[] args) throws ResolvingException {
            if (instance == null) {
                instance = resolver.resolve(desiredClass, args);
                resolver = null;
            }

            return instance;
        }
    }

    private class LinkResolver<T> implements ComponentResolver<T> {
        private final Class<? extends T> toClass;

        LinkResolver(Class<? extends T> toClass) {
            this.toClass = toClass;
        }

        @Override
        public T resolve(Class<? extends T> desiredClass, String[] args) {
            return ComponentContainer.this.get(toClass);
        }
    }
}
