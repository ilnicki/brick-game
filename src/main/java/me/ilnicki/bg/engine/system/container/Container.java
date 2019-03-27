package me.ilnicki.bg.engine.system.container;

import java.util.Set;

public interface Container {
    String[] NO_ARGS = new String[0];

    /**
     * Binds abstract class with the instance resolver method.
     * @param abstractClass
     * @param resolver
     */
    <T> void bind(Class<? super T> abstractClass, ComponentResolver<T> resolver);
    <T> void bind(Class<? super T> abstractClass, Class<T> concreteClass);

    <T> void singleton(Class<? super T> abstractClass, ComponentResolver<T> resolver);
    <T> void singleton(Class<? super T> abstractClass, Class<T> concreteClass);
    default <T> void singleton(Class<T> abstractClass) {
        singleton(abstractClass, abstractClass);
    }
    <T> void singleton(Class<? super T> abstractClass, T concreteObject);

    <T> void share(T sharedObject);

    <T> void link(Class<? super T> fromClass, Class<T> toClass);

    <T> T get(Class<? extends T> abstractClass, String[] args);

    default <T> T get(Class<? extends T> abstractClass) {
        return get(abstractClass, NO_ARGS);
    }

    <T> Set<T> getCompatible(Class<? extends T> baseClass);
}
