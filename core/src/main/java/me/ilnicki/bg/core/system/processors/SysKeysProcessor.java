package me.ilnicki.bg.core.system.processors;

import me.ilnicki.bg.core.machine.Machine;
import me.ilnicki.bg.core.machine.keyboard.KeyMap;
import me.ilnicki.bg.core.machine.keyboard.Keyboard;
import me.ilnicki.bg.core.machine.parameters.BoolParameter;
import me.ilnicki.bg.core.machine.parameters.IntParameter;
import me.ilnicki.bg.core.system.Kernel;
import me.ilnicki.bg.core.system.MachineProcessor;
import me.ilnicki.container.Inject;

public class SysKeysProcessor implements MachineProcessor {
    private final KeyMap<Keyboard.SysKey> keyMap;
    private final IntParameter volume;
    private final BoolParameter pause;

    @Inject
    private Kernel kernel;

    @Inject
    public SysKeysProcessor(Machine machine) {
        keyMap = machine.getKeyboard().getSysKeyMap();
        volume = machine.volume;
        pause = machine.pause;
    }

    @Override
    public void update(long tick) {
        if (keyMap.getState(Keyboard.SysKey.SOUND) == 0) {
            volume.inc();
        }

        if (keyMap.getState(Keyboard.SysKey.START) == 0) {
            pause.toggle();
        }

        if (keyMap.isPressed(Keyboard.SysKey.RESET)) {
            kernel.reset();
        }

        if (keyMap.isPressed(Keyboard.SysKey.ONOFF)) {
            kernel.stop();
        }
    }
}
