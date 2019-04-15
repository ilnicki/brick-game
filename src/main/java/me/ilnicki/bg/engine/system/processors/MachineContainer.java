package me.ilnicki.bg.engine.system.processors;

import me.ilnicki.bg.engine.machine.Field;
import me.ilnicki.bg.engine.machine.Machine;
import me.ilnicki.bg.engine.machine.keyboard.Keyboard;
import me.ilnicki.bg.engine.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.engine.system.container.ComponentContainer;
import me.ilnicki.bg.engine.system.container.Container;
import me.ilnicki.bg.engine.system.container.Inject;

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
