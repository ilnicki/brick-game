package me.ilnicki.bg.core.game;

import me.ilnicki.bg.core.game.splash.Splash;
import me.ilnicki.bg.core.pixelmatrix.ArrayPixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Matrices;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Vector;
import me.ilnicki.bg.core.pixelmatrix.layering.Layer;
import me.ilnicki.bg.core.pixelmatrix.layering.LayerList;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.core.pixelmatrix.transforming.transformers.Translate;
import me.ilnicki.bg.core.state.Field;
import me.ilnicki.bg.core.state.GameState;
import me.ilnicki.bg.core.state.State;
import me.ilnicki.bg.core.state.SystemState;
import me.ilnicki.bg.core.state.buttons.GameButton;
import me.ilnicki.bg.core.state.buttons.SystemButton;
import me.ilnicki.bg.core.state.parameters.IntParameter;
import me.ilnicki.bg.core.system.Module;
import me.ilnicki.bg.core.system.StateConfig;
import me.ilnicki.bg.core.system.processors.gamemanager.GameArgument;
import me.ilnicki.bg.core.system.processors.gamemanager.GameManager;
import me.ilnicki.container.Args;
import me.ilnicki.container.Inject;

import java.util.List;

public class DefaultGameLauncher implements Module {
    @Inject
    private GameManager gameManager;

    private final GameState gameState;
    private final SystemState systemState;

    @Inject
    @Args({"internal", "assets.sprites.characters"})
    private PixelMatrixLoader matrixLoader;

    @Inject
    private StateConfig config;

    private final Layer<PixelMatrix> logoLayer;
    private final Layer<PixelMatrix> prevLayer;
    private final List<Layer<PixelMatrix>> argLayers;

    @Inject
    private GameArgument argument;

    private IntParameter selectedGame;

    private List<Manifest> manifestList;

    @Inject
    public DefaultGameLauncher(State state) {
        gameState = state.getGameState();
        systemState = state.getSystemState();

        Field field = state.getGameState().field;

        logoLayer = new Layer<>(new ArrayPixelMatrix(10, 5));
        logoLayer.transform(new Translate(new Vector(0, 15)));
        field.getLayers().add(logoLayer);

        prevLayer = new Layer<>(new ArrayPixelMatrix(10, 7));
        prevLayer.transform(new Translate(new Vector(0, 6)));
        field.getLayers().add(prevLayer);

        LayerList argLayerList = new LayerList(10, 5);
        argLayers = argLayerList.getLayers();
        argLayers.add(new Layer<>(Matrices.EMPTY).transform(new Translate(new Vector(1, 0))));
        argLayers.add(new Layer<>(Matrices.EMPTY).transform(new Translate(new Vector(5, 0))));

        field.getLayers().add(new Layer<PixelMatrix>(argLayerList).transform(new Translate(new Vector(0, 0))));
    }

    @Override
    public void load() {
        manifestList = gameManager.getManifests();

        selectedGame = new IntParameter(0, manifestList.size() - 1);

        selectedGame.set(config.getSelectedGame());
        systemState.volume.set(config.getVolume());
        argument.set(config.getArgument());

        gameState.hiscore.set(config.getHiscore());
        gameState.level.set(config.getLevel());
        gameState.speed.set(config.getSpeed());

        gameManager.launchGame(Splash.class);
    }

    @Override
    public void update(int delta) {
        int downKeyTime = gameState.buttons.getValue(GameButton.DOWN);
        if (downKeyTime == 0 || (downKeyTime % 8 == 0 && downKeyTime > 24)) {
            argument.inc();
            drawArgument();
        }

        int upKeyTime = gameState.buttons.getValue(GameButton.UP);
        if (upKeyTime == 0 || (upKeyTime % 8 == 0 && upKeyTime > 24)) {
            argument.dec();
            drawArgument();
        }

        if (gameState.buttons.getValue(GameButton.RIGHT) == 3) {
            gameState.speed.inc();
        }

        if (gameState.buttons.getValue(GameButton.LEFT) == 3) {
            gameState.level.inc();
        }

        if (gameState.buttons.getValue(GameButton.ROTATE) == 3) {
            selectedGame.inc();
        }

        if (systemState.buttons.getValue(SystemButton.START) == 0) {
            gameManager.launchGame(manifestList.get(selectedGame.get()).getGameClass());
            systemState.pause.set(false);
        }

        drawLogo();
        drawPreview();
        drawArgument();
    }

    @Override
    public void stop() {
        config.setArgument(argument.get());
        config.setSelectedGame(selectedGame.get());
        config.setHiscore(gameState.hiscore.get());
        config.setLevel(gameState.level.get());
        config.setSpeed(gameState.speed.get());
        config.setVolume(systemState.volume.get());
    }

    private void drawLogo() {
        logoLayer.setData(manifestList.get(selectedGame.get()).getLogo());
    }

    private void drawPreview() {
        prevLayer.setData(manifestList.get(selectedGame.get()).getPreview());
    }

    private void drawArgument() {
        String[] numbers = String.format("%02d", argument.get()).split("");

        for (int i = 0; i < numbers.length; i++) {
            argLayers.get(i).setData(matrixLoader.get(numbers[i]));
        }
    }
}
