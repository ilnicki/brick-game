package me.ilnicki.bg.core.system.processors;

import me.ilnicki.bg.core.state.State;
import me.ilnicki.bg.core.state.keyboard.KeyMap;
import me.ilnicki.bg.core.state.keyboard.Keyboard;
import me.ilnicki.bg.core.state.parameters.BoolParameter;
import me.ilnicki.bg.core.state.parameters.IntParameter;
import me.ilnicki.bg.core.system.Kernel;
import me.ilnicki.bg.core.system.RootProcessor;
import me.ilnicki.container.Inject;

public class SysKeysProcessor implements RootProcessor {
    private final KeyMap<Keyboard.SysKey> keyMap;
    private final IntParameter volume;
    private final BoolParameter pause;

    @Inject
    private Kernel kernel;

    @Inject
    public SysKeysProcessor(State state) {
        keyMap = state.getKeyboard().getSysKeyMap();
        volume = state.volume;
        pause = state.pause;
    }

    @Override
    public void update(long tick) {
        if (keyMap.getValue(Keyboard.SysKey.SOUND) == 0) {
            volume.inc();
        }

        if (keyMap.getValue(Keyboard.SysKey.START) == 0) {
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
