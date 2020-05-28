package me.ilnicki.bg.core.state.keyboard;

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

    public static class CtrlKeyMap extends ArrayKeyMap<CtrlKey> {
        CtrlKeyMap() {
            super(CtrlKey.class);
        }
    }

    public static class SysKeyMap extends ArrayKeyMap<SysKey> {
        SysKeyMap() {
            super(SysKey.class);
        }
    }

    private final CtrlKeyMap ctrlKeyMap = new CtrlKeyMap();
    private final SysKeyMap sysKeyMap = new SysKeyMap();

    public CtrlKeyMap getCtrlKeyMap() {
        return ctrlKeyMap;
    }

    public SysKeyMap getSysKeyMap() {
        return sysKeyMap;
    }
}
