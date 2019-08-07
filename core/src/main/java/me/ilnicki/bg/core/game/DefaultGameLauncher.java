package me.ilnicki.bg.core.game;

import me.ilnicki.bg.core.game.splash.Splash;
import me.ilnicki.bg.core.game.splash.SplashManifest;
import me.ilnicki.bg.core.pixelmatrix.*;
import me.ilnicki.bg.core.pixelmatrix.layering.LayerList;
import me.ilnicki.bg.core.pixelmatrix.transforming.transformers.Translate;
import me.ilnicki.bg.core.system.MachineConfig;
import me.ilnicki.bg.core.machine.Field;
import me.ilnicki.bg.core.pixelmatrix.layering.Layer;
import me.ilnicki.bg.core.machine.Machine;
import me.ilnicki.bg.core.machine.keyboard.Keyboard;
import me.ilnicki.bg.core.machine.keyboard.Keyboard.CtrlKey;
import me.ilnicki.bg.core.machine.keyboard.Keyboard.SysKey;
import me.ilnicki.bg.core.machine.parameters.IntParameter;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.container.Args;
import me.ilnicki.container.Inject;
import me.ilnicki.bg.core.system.processors.GameArgument;
import me.ilnicki.bg.core.system.processors.GameManager;

import java.util.List;

public class DefaultGameLauncher implements Game {
    @Inject
    private GameManager gameManager;

    private final Machine machine;

    @Inject
    @Args({"internal", "assets.sprites.characters"})
    private PixelMatrixLoader matrixLoader;

    @Inject
    private MachineConfig config;

    private final Keyboard keyboard;

    private final Layer<PixelMatrix> logoLayer;
    private final Layer<PixelMatrix> prevLayer;
    private final List<Layer<PixelMatrix>> argLayers;

    private final GameArgument argument = new GameArgument();
    private IntParameter selectedGame;

    private List<Manifest> manifestList;

    @Inject
    public DefaultGameLauncher(Machine machine) {
        this.machine = machine;

        keyboard = machine.getKeyboard();
        Field field = machine.getField();

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
        Machine.Parameters params = machine.params;

        selectedGame.set(config.getSelectedGame());
        params.hiscore.set(config.getHiscore());
        params.level.set(config.getLevel());
        params.speed.set(config.getSpeed());
        machine.volume.set(config.getVolume());
        argument.set(config.getArgument());

        gameManager.shareArgument(argument);

        gameManager.launchGame(new SplashManifest());
    }

    @Override
    public void update(long tick) {
        int downKeyTime = keyboard.getCtrlKeyMap().getState(CtrlKey.DOWN);
        if (downKeyTime == 0 || (downKeyTime % 8 == 0 && downKeyTime > 24)) {
            argument.inc();
            drawArgument();
        }

        int upKeyTime = keyboard.getCtrlKeyMap().getState(CtrlKey.UP);
        if (upKeyTime == 0 || (upKeyTime % 8 == 0 && upKeyTime > 24)) {
            argument.dec();
            drawArgument();
        }

        if (keyboard.getCtrlKeyMap().getState(CtrlKey.RIGHT) == 3) {
            machine.params.speed.inc();
        }

        if (keyboard.getCtrlKeyMap().getState(Keyboard.CtrlKey.LEFT) == 3) {
            machine.params.level.inc();
        }

        if (keyboard.getCtrlKeyMap().getState(CtrlKey.ROTATE) == 3) {
            selectedGame.inc();
        }

        if (keyboard.getSysKeyMap().getState(SysKey.START) == 0) {
            gameManager.launchGame(manifestList.get(selectedGame.get()));
            machine.pause.set(false);
        }

        drawLogo();
        drawPreview();
        drawArgument();
    }

    @Override
    public void stop() {
        Machine.Parameters params = machine.params;

        config.setArgument(argument.get());
        config.setSelectedGame(selectedGame.get());
        config.setHiscore(params.hiscore.get());
        config.setLevel(params.level.get());
        config.setSpeed(params.speed.get());
        config.setVolume(machine.volume.get());
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
            PixelMatrix numMatrix = matrixLoader.load(numbers[i], true);
            argLayers.get(i).setData(numMatrix);
        }
    }
}
