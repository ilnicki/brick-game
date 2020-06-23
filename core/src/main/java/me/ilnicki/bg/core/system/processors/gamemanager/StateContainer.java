package me.ilnicki.bg.core.system.processors.gamemanager;

import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.core.state.Field;
import me.ilnicki.bg.core.state.GameState;
import me.ilnicki.bg.core.state.Helper;
import me.ilnicki.bg.core.state.State;
import me.ilnicki.bg.core.state.buttons.ButtonsState;
import me.ilnicki.bg.core.state.parameters.IntParameter;
import me.ilnicki.bg.core.system.processors.gamemanager.GameManager;
import me.ilnicki.container.ComponentContainer;
import me.ilnicki.container.Container;
import me.ilnicki.container.Inject;
import me.ilnicki.container.provider.Provider;

import java.util.HashMap;
import java.util.Map;

public class StateContainer extends ComponentContainer {
    @Inject
    private void populate(State state, Container container) {
        bind(ButtonsState.class, (desiredClass, args) -> state.getGameState().buttons);

        Map<String, Provider<IntParameter>> params = new HashMap<String, Provider<IntParameter>>() {{
            put("score", (c, a) -> state.getGameState().score);
            put("hiscore", (c, a) -> state.getGameState().hiscore);
            put("speed", (c, a) -> state.getGameState().speed);
            put("level", (c, a) -> state.getGameState().level);
        }};
        bind(IntParameter.class, (desiredClass, args) -> params.get(args[0]).provide(desiredClass, args));

        bind(Field.class, (desiredClass, args) -> state.getGameState().field);
        bind(Helper.class, (desiredClass, args) -> state.getGameState().helper);

        bind(GameManager.class, container::get);
        bind(PixelMatrixLoader.class, container::get);
    }
}