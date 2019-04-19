package me.ilnicki.bg.core.system.processors;

import me.ilnicki.bg.core.machine.Field;
import me.ilnicki.bg.core.machine.Machine;
import me.ilnicki.bg.core.machine.keyboard.Keyboard;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.core.system.container.ComponentContainer;
import me.ilnicki.bg.core.system.container.Container;
import me.ilnicki.bg.core.system.container.Inject;

public class MachineContainer extends ComponentContainer {
    @Inject
    private void populate(Machine machine, Container container) {
        bind(Keyboard.CtrlKeyMap.class, (desiredClass, args) -> machine.getKeyboard().getCtrlKeyMap());
        bind(Machine.Parameters.class, (desiredClass, args) -> machine.getParameters());
        bind(Machine.Parameters.class, (desiredClass, args) -> machine.getParameters());
        bind(Machine.Parameters.class, (desiredClass, args) -> machine.getParameters());
        bind(Field.class, (desiredClass, args) -> machine.getField());
        bind(Machine.Helper.class, (desiredClass, args) -> machine.getHelper());

        bind(GameManager.class, container::get);
        bind(PixelMatrixLoader.class, container::get);
    }
}
