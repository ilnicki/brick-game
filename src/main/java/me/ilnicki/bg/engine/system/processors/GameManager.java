package me.ilnicki.bg.engine.system.processors;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import me.ilnicki.bg.engine.game.Game;
import me.ilnicki.bg.engine.game.GameInfo;
import me.ilnicki.bg.engine.game.GamesConfig;
import me.ilnicki.bg.engine.machine.Field;
import me.ilnicki.bg.engine.machine.Machine;
import me.ilnicki.bg.engine.system.MachineProcessor;
import me.ilnicki.bg.engine.system.Module;
import me.ilnicki.bg.engine.system.SystemConfig;
import me.ilnicki.bg.engine.system.SystemManager;
import me.ilnicki.bg.engine.system.container.Inject;

public class GameManager implements MachineProcessor {

    private enum State {
        MANAGER_LAUNCHING,
        GAME_LAUNCHING,
        GAME_STOPPING,
        GAME_PROCESSING
    }

    private Module launcher;

    private Machine machine;

    private Field launcherField;

    private SystemManager systemManager;
    private Module currentGame;

    private List<GameInfo> gameInfoList;

    private String workingPath;

    private State state;

    @Inject
    public GameManager(SystemManager systemManager,
                       Machine machine,
                       SystemConfig systemConfig,
                       GamesConfig gamesConfig) {
        this.systemManager = systemManager;
        this.machine = machine;
        workingPath = systemConfig.getWorkingPath();

        gameInfoList = new ArrayList<>();

        for (String gameName : gamesConfig.getGames()) {
            GameInfo gameInfo = loadGameInfo(gameName);

            if (gameInfo != null) {
                gameInfoList.add(gameInfo);
            }
        }

        try {
            launcher = (Module) systemManager.get(Class.forName(gamesConfig.getLauncher()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        launcherField = machine.getField();
    }

    @Override
    public void load() {
        launcher.load();
        currentGame = launcher;
        state = State.GAME_PROCESSING;
    }

    @Override
    public void update(long tick) {
        switch (state) {
            case MANAGER_LAUNCHING:
            case GAME_LAUNCHING:
                break;
            case GAME_STOPPING:
                this.doExitGame();
                break;
            case GAME_PROCESSING:
                if (!machine.pause.get() || currentGame == launcher) {
                    //If game not paused or current process is launcher
                    machine.pause.set(false);
                    currentGame.update(tick);
                }
                break;
        }
    }

    @Override
    public void stop() {
        currentGame.stop();
        if (currentGame != launcher) {
            launcher.stop();
        }
    }

    public List<GameInfo> getGameInfoList() {
        return gameInfoList;
    }

    public void launchGame(GameInfo gameInfo, int argument) {
        try {
            Game game = gameInfo.getGameClass().newInstance();

            machine.recreateField(gameInfo.getBufferWidth(), gameInfo.getBufferHeight());

            currentGame = game;
            currentGame.load();
        } catch (InstantiationException | IllegalAccessException ex) {
            systemManager.stop();
        }
    }

    public void exitGame() {
        currentGame.stop();
        state = State.GAME_STOPPING;
    }

    private void doExitGame() {
        machine.getParameters().score.set(0);
        currentGame = launcher;
        machine.setField(launcherField);

        state = State.GAME_PROCESSING;
    }

    private GameInfo loadGameInfo(String gameName) {
        GameInfo gi;

        try {
            gi = (GameInfo) Class.forName(gameName + "Info").newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            try {
                URL[] jarFile = new URL[]{
                        new File(this.workingPath + "/games/" + gameName + "/").getAbsoluteFile().toURI().toURL(),
                        new File(this.workingPath + "/games/" + gameName + ".jar").getAbsoluteFile().toURI().toURL()
                };

                ClassLoader urlCl = new URLClassLoader(jarFile);

                gi = (GameInfo) urlCl.loadClass(gameName + "Info").newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | MalformedURLException ex1) {
                gi = null;
            }
        }

        return gi;
    }
}
