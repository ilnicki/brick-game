package me.ilnicki.bg.core.system.processors;

import me.ilnicki.bg.core.game.Manifest;
import me.ilnicki.bg.core.game.GamesConfig;
import me.ilnicki.bg.core.machine.Field;
import me.ilnicki.bg.core.machine.Machine;
import me.ilnicki.bg.core.system.Kernel;
import me.ilnicki.bg.core.system.MachineProcessor;
import me.ilnicki.bg.core.system.Module;
import me.ilnicki.bg.core.system.SystemConfig;
import me.ilnicki.container.Container;
import me.ilnicki.container.Inject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class GameManager implements MachineProcessor {

    private enum State {
        MANAGER_LAUNCHING,
        GAME_LAUNCHING,
        GAME_STOPPING,
        GAME_PROCESSING,
    }

    private Module launcher;

    @Inject
    private Machine machine;

    private Field launcherField;

    @Inject
    private Kernel kernel;

    @Inject
    private Container container;

    private Module currentGame;

    private List<Manifest> manifestList;

    private State state;

    @Inject
    private SystemConfig systemConfig;

    @Inject
    private GamesConfig gamesConfig;

    @Inject
    private MachineContainer machineContainer;

    @Inject
    private void shareMachine() {
        container.share(machine.getScreen());
    }

    @Override
    public void load() {
        manifestList = new ArrayList<>();

        for (String gameName : gamesConfig.getGameManifests()) {
            Manifest manifest = loadGameManifests(gameName);

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

    public List<Manifest> getManifestList() {
        return manifestList;
    }

    public void shareArgument(GameArgument arg) {
        machineContainer.share(arg);
    }

    public void launchGame(Manifest manifest) {
        machine.recreateField(manifest.getBufferWidth(), manifest.getBufferHeight());
        currentGame = machineContainer.get(manifest.getGameClass());
        currentGame.load();
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

    private Manifest loadGameManifests(String manifestName) {
        try {
            return container.get(Class.forName(manifestName).asSubclass(Manifest.class));
        } catch (ClassNotFoundException ex) {
            try {
                URL[] jarFile = new URL[]{
                        new File(this.systemConfig.getWorkingPath() + "/games/" + manifestName + "/").getAbsoluteFile().toURI().toURL(),
                        new File(this.systemConfig.getWorkingPath() + "/games/" + manifestName + ".jar").getAbsoluteFile().toURI().toURL()
                };

                ClassLoader urlCl = new URLClassLoader(jarFile);

                return container.get(urlCl.loadClass(manifestName).asSubclass(Manifest.class));
            } catch (ClassNotFoundException | MalformedURLException ex1) {
                return null;
            }
        }

    }
}
