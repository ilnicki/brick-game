package me.ilnicki.bg.core.system.processors;

import me.ilnicki.bg.core.game.GamesConfig;
import me.ilnicki.bg.core.game.Manifest;
import me.ilnicki.bg.core.machine.Field;
import me.ilnicki.bg.core.machine.Machine;
import me.ilnicki.bg.core.system.Kernel;
import me.ilnicki.bg.core.system.MachineProcessor;
import me.ilnicki.bg.core.system.Module;
import me.ilnicki.bg.core.system.SystemConfig;
import me.ilnicki.container.Container;
import me.ilnicki.container.Inject;

import java.util.ArrayList;
import java.util.List;

public class GameManager implements MachineProcessor {

    private Module launcher;
    @Inject
    private Machine machine;
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
    private MachineContainer machineContainer;

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

        launcherField = machine.getField();
        stage = Stage.GAME_PROCESSING;
        currentGame = launcher;

        launcher.load();
    }

    @Override
    public void update(long tick) {
        switch (stage) {
            case MANAGER_LAUNCHING:
                break;
            case GAME_LAUNCHING:
                stage = Stage.GAME_PROCESSING;
                machine.refreshField();
                currentGame = machineContainer.get(nextGame.getGameClass());
                nextGame = null;
                machine.params.score.set(0);
                currentGame.load();
                break;
            case GAME_STOPPING:
                stage = Stage.GAME_PROCESSING;
                currentGame = launcher;
                machine.setField(launcherField);
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

    public List<Manifest> getManifestList() {
        return manifestList;
    }

    public void shareArgument(GameArgument arg) {
        machineContainer.share(arg);
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
