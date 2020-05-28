package me.ilnicki.bg.core.system.processors;

import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.core.state.Field;
import me.ilnicki.bg.core.state.Helper;
import me.ilnicki.bg.core.state.State;
import me.ilnicki.bg.core.state.keyboard.Keyboard;
import me.ilnicki.container.ComponentContainer;
import me.ilnicki.container.Container;
import me.ilnicki.container.Inject;

public class StateContainer extends ComponentContainer {
    @Inject
    private void populate(State state, Container container) {
        bind(Keyboard.CtrlKeyMap.class, (desiredClass, args) -> state.getKeyboard().getCtrlKeyMap());
        bind(State.Parameters.class, (desiredClass, args) -> state.params);
        bind(Field.class, (desiredClass, args) -> state.getField());
        bind(Helper.class, (desiredClass, args) -> state.getHelper());

        bind(GameManager.class, container::get);
        bind(PixelMatrixLoader.class, container::get);
    }
}
