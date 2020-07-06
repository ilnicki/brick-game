package me.ilnicki.bg.core.system.processors;

import me.ilnicki.bg.core.state.State;
import me.ilnicki.bg.core.state.SystemState;
import me.ilnicki.bg.core.state.buttons.ButtonsState;
import me.ilnicki.bg.core.state.buttons.SystemButton;
import me.ilnicki.bg.core.state.parameters.BoolParameter;
import me.ilnicki.bg.core.state.parameters.IntParameter;
import me.ilnicki.bg.core.system.CoreModule;
import me.ilnicki.bg.core.system.Kernel;
import me.ilnicki.container.Inject;

public class SystemButtonsProcessor implements CoreModule {
  private final ButtonsState<SystemButton> buttonsState;
  private final IntParameter volume;
  private final BoolParameter pause;

  @Inject private Kernel kernel;

  @Inject
  public SystemButtonsProcessor(State state) {
    SystemState systemState = state.getSystemState();

    buttonsState = systemState.buttons;
    volume = systemState.volume;
    pause = systemState.pause;
  }

  @Override
  public void update(int delta) {
    if (buttonsState.getValue(SystemButton.SOUND) == 0) {
      volume.inc();
    }

    if (buttonsState.getValue(SystemButton.START) == 0) {
      pause.toggle();
    }

    if (buttonsState.isPressed(SystemButton.RESET)) {
      kernel.reset();
    }

    if (buttonsState.isPressed(SystemButton.ONOFF)) {
      kernel.stop();
    }
  }
}
