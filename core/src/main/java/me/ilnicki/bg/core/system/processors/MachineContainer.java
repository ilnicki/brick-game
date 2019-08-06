package me.ilnicki.bg.core.system.processors;

import me.ilnicki.bg.core.machine.Field;
import me.ilnicki.bg.core.machine.Helper;
import me.ilnicki.bg.core.machine.Machine;
import me.ilnicki.bg.core.machine.keyboard.Keyboard;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.container.ComponentContainer;
import me.ilnicki.container.Container;
import me.ilnicki.container.Inject;

public class MachineContainer extends ComponentContainer {
    @Inject
    private void populate(Machine machine, Container container) {
        bind(Keyboard.CtrlKeyMap.class, (desiredClass, args) -> machine.getKeyboard().getCtrlKeyMap());
        bind(Machine.Parameters.class, (desiredClass, args) -> machine.getParameters());
        bind(Machine.Parameters.class, (desiredClass, args) -> machine.getParameters());
        bind(Machine.Parameters.class, (desiredClass, args) -> machine.getParameters());
        bind(Field.class, (desiredClass, args) -> machine.getField());
        bind(Helper.class, (desiredClass, args) -> machine.getHelper());

        bind(GameManager.class, container::get);
        bind(PixelMatrixLoader.class, container::get);
    }
}
