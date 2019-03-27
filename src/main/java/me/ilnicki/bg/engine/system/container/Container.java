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

    /**
     *
     * @param abstractClass
     * @param concreteClass
     * @param <T>
     */
    <T> void bind(Class<? super T> abstractClass, Class<T> concreteClass);

    /**
     *
     * @param abstractClass
     * @param resolver
     * @param <T>
     */
    <T> void singleton(Class<? super T> abstractClass, ComponentResolver<T> resolver);

    /**
     *
     * @param abstractClass
     * @param concreteClass
     * @param <T>
     */
    <T> void singleton(Class<? super T> abstractClass, Class<T> concreteClass);

    /**
     *
     * @param abstractClass
     * @param <T>
     */
    default <T> void singleton(Class<T> abstractClass) {
        singleton(abstractClass, abstractClass);
    }

    /**
     *
     * @param abstractClass
     * @param concreteObject
     * @param <T>
     */
    <T> void singleton(Class<? super T> abstractClass, T concreteObject);

    /**
     *
     * @param sharedObject
     * @param <T>
     */
    <T> void share(T sharedObject);

    /**
     *
     * @param fromClass
     * @param toClass
     * @param <T>
     */
    <T> void link(Class<? super T> fromClass, Class<T> toClass);

    /**
     *
     * @param abstractClass
     * @param args
     * @param <T>
     * @return
     * @throws ResolvingException
     */
    <T> T get(Class<? extends T> abstractClass, String[] args) throws ResolvingException;

    /**
     *
     * @param abstractClass
     * @param <T>
     * @return
     * @throws ResolvingException
     */
    default <T> T get(Class<? extends T> abstractClass) throws ResolvingException {
        return get(abstractClass, NO_ARGS);
    }

    /**
     *
     * @param baseClass
     * @param <T>
     * @return
     * @throws ResolvingException
     */
    <T> Set<T> getCompatible(Class<? extends T> baseClass) throws ResolvingException;
}
