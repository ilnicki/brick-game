package me.ilnicki.bg.engine.system.container;

public interface ComponentFactory<T> extends ComponentResolver<T> {
    T produce(String[] args) throws ResolvingException;

    default T resolve(Class<? extends T> desiredClass, String[] args) throws ResolvingException {
        return produce(args);
    }
}
