package me.ilnicki.bg.engine.system.processors;

import me.ilnicki.bg.engine.machine.Machine;
import me.ilnicki.bg.engine.machine.keyboard.KeyMap;
import me.ilnicki.bg.engine.machine.keyboard.Keyboard.SysKey;
import me.ilnicki.bg.engine.machine.parameters.BoolParameter;
import me.ilnicki.bg.engine.machine.parameters.IntParameter;
import me.ilnicki.bg.engine.system.MachineProcessor;
import me.ilnicki.bg.engine.system.SystemManager;
import me.ilnicki.bg.engine.system.container.Inject;

public class SysKeysProcessor implements MachineProcessor {
    private final KeyMap<SysKey> keyMap;
    private final IntParameter volume;
    private final BoolParameter pause;

    @Inject
    private SystemManager systemManager;

    @Inject
    public SysKeysProcessor(Machine machine) {
        keyMap = machine.getKeyboard().getSysKeyMap();
        volume = machine.volume;
        pause = machine.pause;
    }

    @Override
    public void update(long tick) {
        if (keyMap.getState(SysKey.SOUND) == 0) {
            volume.inc();
        }

        if (keyMap.getState(SysKey.START) == 0) {
            pause.toggle();
        }

        if (keyMap.isPressed(SysKey.RESET)) {
            systemManager.reset();
        }

        if (keyMap.isPressed(SysKey.ONOFF)) {
            systemManager.stop();
        }
    }
}
