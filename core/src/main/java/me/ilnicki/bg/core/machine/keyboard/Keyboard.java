package me.ilnicki.bg.core.machine.keyboard;

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

    public class CtrlKeyMap extends ArrayKeyMap<CtrlKey> {
        CtrlKeyMap() {
            super(CtrlKey.class);
        }
    }

    public class SysKeyMap extends ArrayKeyMap<SysKey> {
        SysKeyMap() {
            super(SysKey.class);
        }
    }

    private final CtrlKeyMap ctrlKeyMap;
    private final SysKeyMap sysKeyMap;

    public Keyboard() {
        ctrlKeyMap = new CtrlKeyMap();
        sysKeyMap = new SysKeyMap();
    }

    public CtrlKeyMap getCtrlKeyMap() {
        return ctrlKeyMap;
    }

    public SysKeyMap getSysKeyMap() {
        return sysKeyMap;
    }
}
