package me.ilnicki.bg.core.system.container;

import me.ilnicki.bg.core.system.container.provider.Provider;

import java.util.Set;

public interface Container {
    String[] NO_ARGS = new String[0];

    /**
     * Binds abstract class with the instance provider.
     *
     * @param abstractClass Class which supposed to be used as contract to fetch from container.
     * @param provider Instance provider.
     * @param <T>
     */
    <T> void bind(Class<? super T> abstractClass, Provider<T> provider);

    /**
     * Binds abstract class with the default instance provider for concrete class. Also binds concrete class to itself.
     *
     * @param abstractClass Class which supposed to be used as contract to fetch from container.
     * @param concreteClass Class to be used with default instance provider.
     * @param <T>
     */
    <T> void bind(Class<? super T> abstractClass, Class<T> concreteClass);

    /**
     * @param abstractClass Class which supposed to be used as contract to fetch from container.
     * @param provider
     * @param <T>
     */
    <T> void singleton(Class<? super T> abstractClass, Provider<T> provider);

    /**
     * Binds abstract class with the singleton provider for concrete class. Also binds concrete class to itself.
     *
     * @param abstractClass Class which supposed to be used as contract to fetch from container.
     * @param concreteClass
     * @param <T>
     */
    <T> void singleton(Class<? super T> abstractClass, Class<T> concreteClass);

    /**
     * @param abstractClass Class which supposed to be used as contract to fetch from container.
     * @param <T>
     */
    default <T> void singleton(Class<T> abstractClass) {
        singleton(abstractClass, abstractClass);
    }

    /**
     * @param abstractClass Class which supposed to be used as contract to fetch from container.
     * @param concreteObject
     * @param <T>
     */
    <T> void singleton(Class<? super T> abstractClass, T concreteObject);

    /**
     * Equal to binding singleton with concrete object.
     *
     * @param sharedObject
     * @param <T>
     */
    <T> void share(T sharedObject);

    /**
     * Creates link to another container binding.
     *
     * @param fromClass Class which supposed to be used as contract to fetch from container.
     * @param toClass
     * @param <T>
     */
    <T> void link(Class<? super T> fromClass, Class<T> toClass);

    /**
     * @param desiredClass Class to fetch from container.
     * @param args
     * @param <T>
     * @return
     * @throws ProvisionException
     */
    <T> T get(Class<? extends T> desiredClass, String[] args) throws ProvisionException;

    /**
     * @param abstractClass Class which supposed to be used as contract to fetch from container.
     * @param <T>
     * @return
     * @throws ProvisionException
     */
    default <T> T get(Class<? extends T> abstractClass) throws ProvisionException {
        return get(abstractClass, NO_ARGS);
    }

    /**
     * @param baseClass
     * @param <T>
     * @return
     * @throws ProvisionException
     */
    <T> Set<T> getCompatible(Class<? extends T> baseClass) throws ProvisionException;
}
