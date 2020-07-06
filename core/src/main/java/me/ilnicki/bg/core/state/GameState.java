package me.ilnicki.bg.core.state;

import me.ilnicki.bg.core.state.buttons.ArrayButtonsState;
import me.ilnicki.bg.core.state.buttons.GameButton;
import me.ilnicki.bg.core.state.buttons.UpdatableButtonsState;
import me.ilnicki.bg.core.state.parameters.IntParameter;

public class GameState {
  public final IntParameter score = new IntParameter(0, 999999);
  public final IntParameter hiscore = new IntParameter(0, 999999);
  public final IntParameter speed = new IntParameter(1, 10);
  public final IntParameter level = new IntParameter(1, 10);

  public final Field field = new Field();
  public final Helper helper = new Helper();

  public final UpdatableButtonsState<GameButton> buttons =
      new ArrayButtonsState<>(GameButton.class);
}
