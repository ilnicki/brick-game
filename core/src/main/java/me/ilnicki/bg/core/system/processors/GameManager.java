package me.ilnicki.bg.core.system.processors;

import me.ilnicki.bg.core.game.GamesConfig;
import me.ilnicki.bg.core.game.Manifest;
import me.ilnicki.bg.core.state.Field;
import me.ilnicki.bg.core.state.State;
import me.ilnicki.bg.core.system.CoreModule;
import me.ilnicki.bg.core.system.Kernel;
import me.ilnicki.bg.core.system.Module;
import me.ilnicki.bg.core.system.SystemConfig;
import me.ilnicki.container.Container;
import me.ilnicki.container.Inject;

import java.util.ArrayList;
import java.util.List;

public class GameManager implements CoreModule {

    private Module launcher;
    @Inject
    private State state;
    private Field launcherField;
    @Inject
    private Kernel kernel;
    @Inject
    private Container container;
    private Module currentGame;
    private Manifest nextGame;
    private List<Manifest> manifestList;
    private Stage stage;
    @Inject
    private SystemConfig systemConfig;
    @Inject
    private GamesConfig gamesConfig;
    @Inject
    private StateContainer stateContainer;

    @Override
    public void load() {
        stage = Stage.MANAGER_LAUNCHING;
        manifestList = new ArrayList<>();

        for (String gameName : gamesConfig.getGameManifests()) {
            Manifest manifest = loadGameManifest(gameName);

            if (manifest != null) {
                manifestList.add(manifest);
            }
        }

        try {
            launcher = (Module) container.get(Class.forName(gamesConfig.getLauncher()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        launcherField = state.getField();
        stage = Stage.GAME_PROCESSING;
        currentGame = launcher;

        launcher.load();
    }

    @Override
    public void update(int delta) {
        switch (stage) {
            case MANAGER_LAUNCHING:
                break;
            case GAME_LAUNCHING:
                stage = Stage.GAME_PROCESSING;
                state.refreshField();
                currentGame = stateContainer.get(nextGame.getGameClass());
                nextGame = null;
                state.score.set(0);
                currentGame.load();
                break;
            case GAME_STOPPING:
                stage = Stage.GAME_PROCESSING;
                currentGame = launcher;
                state.setField(launcherField);
                break;
            case GAME_PROCESSING:
                if (!state.pause.get() || currentGame == launcher) {
                    //If game not paused or current process is launcher
                    state.pause.set(false);
                    currentGame.update(delta);
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

    public List<Manifest> getManifestList() {
        return manifestList;
    }

    public void shareArgument(GameArgument arg) {
        stateContainer.share(arg);
    }

    public void launchGame(Manifest manifest) {
        nextGame = manifest;
        stage = Stage.GAME_LAUNCHING;
    }

    public void exitGame() {
        currentGame.stop();
        stage = Stage.GAME_STOPPING;
    }

    private Manifest loadGameManifest(String manifestName) {
        try {
            return container.get(Class.forName(manifestName).asSubclass(Manifest.class));
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    private enum Stage {
        MANAGER_LAUNCHING,
        GAME_LAUNCHING,
        GAME_STOPPING,
        GAME_PROCESSING,
    }
}
