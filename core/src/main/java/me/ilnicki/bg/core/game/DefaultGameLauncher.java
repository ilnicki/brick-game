package me.ilnicki.bg.core.game;

import me.ilnicki.bg.core.game.splash.SplashManifest;
import me.ilnicki.bg.core.pixelmatrix.ArrayPixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Matrices;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Vector;
import me.ilnicki.bg.core.pixelmatrix.layering.Layer;
import me.ilnicki.bg.core.pixelmatrix.layering.LayerList;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.core.pixelmatrix.transforming.transformers.Translate;
import me.ilnicki.bg.core.state.Field;
import me.ilnicki.bg.core.state.State;
import me.ilnicki.bg.core.state.keyboard.Keyboard;
import me.ilnicki.bg.core.state.keyboard.Keyboard.CtrlKey;
import me.ilnicki.bg.core.state.keyboard.Keyboard.SysKey;
import me.ilnicki.bg.core.state.parameters.IntParameter;
import me.ilnicki.bg.core.system.Module;
import me.ilnicki.bg.core.system.StateConfig;
import me.ilnicki.bg.core.system.processors.GameArgument;
import me.ilnicki.bg.core.system.processors.GameManager;
import me.ilnicki.container.Args;
import me.ilnicki.container.Inject;

import java.util.List;

public class DefaultGameLauncher implements Module {
    @Inject
    private GameManager gameManager;

    private final State state;

    @Inject
    @Args({"internal", "assets.sprites.characters"})
    private PixelMatrixLoader matrixLoader;

    @Inject
    private StateConfig config;

    private final Keyboard keyboard;

    private final Layer<PixelMatrix> logoLayer;
    private final Layer<PixelMatrix> prevLayer;
    private final List<Layer<PixelMatrix>> argLayers;

    private final GameArgument argument = new GameArgument();
    private IntParameter selectedGame;

    private List<Manifest> manifestList;

    @Inject
    public DefaultGameLauncher(State state) {
        this.state = state;

        keyboard = state.keyboard;
        Field field = state.getField();

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
        manifestList = gameManager.getManifestList();

        selectedGame = new IntParameter(0, manifestList.size() - 1);

        selectedGame.set(config.getSelectedGame());
        state.hiscore.set(config.getHiscore());
        state.level.set(config.getLevel());
        state.speed.set(config.getSpeed());
        state.volume.set(config.getVolume());
        argument.set(config.getArgument());

        gameManager.shareArgument(argument);

        gameManager.launchGame(new SplashManifest());
    }

    @Override
    public void update(int delta) {
        int downKeyTime = keyboard.getCtrlKeyMap().getValue(CtrlKey.DOWN);
        if (downKeyTime == 0 || (downKeyTime % 8 == 0 && downKeyTime > 24)) {
            argument.inc();
            drawArgument();
        }

        int upKeyTime = keyboard.getCtrlKeyMap().getValue(CtrlKey.UP);
        if (upKeyTime == 0 || (upKeyTime % 8 == 0 && upKeyTime > 24)) {
            argument.dec();
            drawArgument();
        }

        if (keyboard.getCtrlKeyMap().getValue(CtrlKey.RIGHT) == 3) {
            state.speed.inc();
        }

        if (keyboard.getCtrlKeyMap().getValue(Keyboard.CtrlKey.LEFT) == 3) {
            state.level.inc();
        }

        if (keyboard.getCtrlKeyMap().getValue(CtrlKey.ROTATE) == 3) {
            selectedGame.inc();
        }

        if (keyboard.getSysKeyMap().getValue(SysKey.START) == 0) {
            gameManager.launchGame(manifestList.get(selectedGame.get()));
            state.pause.set(false);
        }

        drawLogo();
        drawPreview();
        drawArgument();
    }

    @Override
    public void stop() {
        config.setArgument(argument.get());
        config.setSelectedGame(selectedGame.get());
        config.setHiscore(state.hiscore.get());
        config.setLevel(state.level.get());
        config.setSpeed(state.speed.get());
        config.setVolume(state.volume.get());
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
            PixelMatrix numMatrix = matrixLoader.get(numbers[i]);
            argLayers.get(i).setData(numMatrix);
        }
    }
}
