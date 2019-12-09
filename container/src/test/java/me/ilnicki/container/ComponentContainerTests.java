package me.ilnicki.container;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComponentContainerTests {
    @Test
    void emptyContainerObjectProviding() {
        final Container container = new ComponentContainer();
        assertEquals(container.get(String.class), "");
    }

    @Test
    void bindContainerObjectByProvider() {
        final Container container = new ComponentContainer();
        container.bind(String.class, (desiredClass, args) -> "test");

        assertEquals(container.get(String.class), "test");
    }

    @Test
    void bindContainerObjectByClass() {
        final Container container = new ComponentContainer();
        container.bind(Object.class, String.class);

        assertEquals(container.get(Object.class), "");
    }
}
