package me.ilnicki.bg.engine.machine.keyboard;

public class Keyboard {
    public enum CtrlKey {
        UP,
        RIGHT,
        DOWN,
        LEFT,
        ROTATE
    }

    public enum SysKey {
        RESET,
        SOUND,
        START,
        ONOFF
    }

    private final UpdatableKeyMap<CtrlKey> ctrlKeyMap;
    private final UpdatableKeyMap<SysKey> sysKeyMap;

    public Keyboard() {
        ctrlKeyMap = new ArrayKeyMap<>(CtrlKey.class);
        sysKeyMap = new ArrayKeyMap<>(SysKey.class);
    }

    public UpdatableKeyMap<CtrlKey> getCtrlKeyMap() {
        return ctrlKeyMap;
    }

    public UpdatableKeyMap<SysKey> getSysKeyMap() {
        return sysKeyMap;
    }
}
