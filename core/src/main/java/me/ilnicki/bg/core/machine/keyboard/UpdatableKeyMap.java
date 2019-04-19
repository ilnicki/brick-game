package me.ilnicki.bg.core.machine.keyboard;

public interface UpdatableKeyMap<E extends Enum<E>> extends KeyMap<E> {
    void update(E key, boolean state);
}
