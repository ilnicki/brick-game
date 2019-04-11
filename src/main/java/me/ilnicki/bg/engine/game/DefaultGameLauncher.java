package me.ilnicki.bg.engine.game;

import me.ilnicki.bg.engine.machine.Field;
import me.ilnicki.bg.engine.machine.Layer;
import me.ilnicki.bg.engine.machine.Machine;
import me.ilnicki.bg.engine.machine.keyboard.Keyboard;
import me.ilnicki.bg.engine.machine.keyboard.Keyboard.CtrlKey;
import me.ilnicki.bg.engine.machine.keyboard.Keyboard.SysKey;
import me.ilnicki.bg.engine.machine.parameters.IntParameter;
import me.ilnicki.bg.engine.pixelmatrix.MatrixUtils;
import me.ilnicki.bg.engine.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.engine.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.engine.system.MachineConfig;
import me.ilnicki.bg.engine.system.container.Args;
import me.ilnicki.bg.engine.system.container.Inject;
import me.ilnicki.bg.engine.system.processors.GameManager;

import java.util.List;

public class DefaultGameLauncher implements Game {
    @Inject
    private GameManager gameManager;

    private final Machine machine;

    @Inject
    @Args("characters")
    private PixelMatrixLoader matrixLoader;

    @Inject
    private MachineConfig config;
    private final Keyboard keyboard;
    private final Field field;

    private final Layer logoLayer;
    private final Layer prevLayer;
    private final Layer argLayer;

    private final IntParameter argument = new IntParameter(1, 99);
    private IntParameter selectedGame;

    private List<GameInfo> gameInfoList;

    @Inject
    public DefaultGameLauncher(Machine machine) {
        this.machine = machine;

        keyboard = machine.getKeyboard();
        field = machine.getField();

        logoLayer = new Layer(10, 5);
        logoLayer.setPosition(0, 15);
        field.getLayers().add(logoLayer);

        prevLayer = new Layer(10, 7);
        prevLayer.setPosition(0, 6);
        field.getLayers().add(prevLayer);

        argLayer = new Layer(10, 5);
        argLayer.setPosition(0, 0);
        field.getLayers().add(argLayer);
    }

    @Override
    public void load() {
        gameInfoList = gameManager.getGameInfoList();

        selectedGame = new IntParameter(0, gameInfoList.size() - 1);
        Machine.Parameters params = machine.getParameters();

        argument.set(config.getArgument());
        selectedGame.set(config.getSelectedGame());
        params.hiscore.set(config.getHiscore());
        params.level.set(config.getLevel());
        params.speed.set(config.getSpeed());
        machine.volume.set(config.getVolume());

        drawLogo();
        drawPreview();
        drawArgument();
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
            machine.getParameters().speed.inc();
        }

        if (keyboard.getCtrlKeyMap().getState(Keyboard.CtrlKey.LEFT) == 3) {
            machine.getParameters().level.inc();
        }

        if (keyboard.getCtrlKeyMap().getState(CtrlKey.ROTATE) == 3) {
            selectedGame.inc();

            drawLogo();
            drawPreview();
        }

        if (keyboard.getSysKeyMap().getState(SysKey.START) == 0) {
            gameManager.launchGame(gameInfoList.get(selectedGame.get()), argument.get());
        }
    }

    @Override
    public void stop() {
        Machine.Parameters params = machine.getParameters();

        config.setArgument(argument.get());
        config.setSelectedGame(selectedGame.get());
        config.setHiscore(params.hiscore.get());
        config.setLevel(params.level.get());
        config.setSpeed(params.speed.get());
        config.setVolume(machine.volume.get());
    }

    private void drawLogo() {
        logoLayer.setPixelMatrix(gameInfoList.get(selectedGame.get()).getLogo());
    }

    private void drawPreview() {
        prevLayer.setPixelMatrix(gameInfoList.get(selectedGame.get()).getPreview());
    }

    private void drawArgument() {
        MatrixUtils.clear(argLayer);

        String[] numbers = String.format("%02d", argument.get()).split("");

        int cursorX = 1;
        int cursorY = 0;

        for (String number : numbers) {
            PixelMatrix numMatrix = matrixLoader.load(number, true);

            for (int i = 0; i < numMatrix.getHeight(); i++) {
                for (int j = 0; j < numMatrix.getWidth(); j++) {
                    argLayer.setPixel(cursorX + j, cursorY + i, numMatrix.getPixel(j, i));
                }
            }

            cursorX = cursorX + numMatrix.getWidth() + 1;
        }
    }
}
