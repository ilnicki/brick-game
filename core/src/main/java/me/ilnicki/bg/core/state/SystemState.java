package me.ilnicki.bg.core.state;

import me.ilnicki.bg.core.state.buttons.ArrayButtonsState;
import me.ilnicki.bg.core.state.buttons.SystemButton;
import me.ilnicki.bg.core.state.buttons.UpdatableButtonsState;
import me.ilnicki.bg.core.state.parameters.BoolParameter;
import me.ilnicki.bg.core.state.parameters.IntParameter;

public class SystemState {
    public final IntParameter volume = new IntParameter(0, 3);
    public final BoolParameter pause = new BoolParameter(false);

    public final UpdatableButtonsState<SystemButton> buttons = new ArrayButtonsState<>(SystemButton.class);
}
