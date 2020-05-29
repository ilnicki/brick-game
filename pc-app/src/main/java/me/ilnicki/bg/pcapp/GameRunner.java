package me.ilnicki.bg.pcapp;

import me.ilnicki.bg.core.TickProvider;
import me.ilnicki.bg.core.io.Drawer;
import me.ilnicki.bg.core.io.KeyReader;
import me.ilnicki.bg.core.io.SoundPlayer;
import me.ilnicki.bg.core.io.dummy.Dummy;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoaderFactory;
import me.ilnicki.bg.core.state.State;
import me.ilnicki.bg.core.system.CoreModule;
import me.ilnicki.bg.core.system.ModuleSet;
import me.ilnicki.bg.core.system.RootProcessor;
import me.ilnicki.bg.core.system.Runner;
import me.ilnicki.bg.core.system.SystemManager;
import me.ilnicki.bg.core.system.processors.GameManager;
import me.ilnicki.bg.core.system.processors.SysKeysProcessor;
import me.ilnicki.bg.iolwjgl3opengl.Lwjgl3;
import me.ilnicki.bg.ticklwjgl.LwjglTickProvider;
import me.ilnicki.container.Container;

public class GameRunner implements Runner {
    private boolean restartScheduled = false;

    private final ModuleSet modules = new ModuleSet();
    private final TickProvider tp = new LwjglTickProvider(60);

    public void run() {
        Container container = new SystemManager(this);

        container.bind(PixelMatrixLoader.class, new PixelMatrixLoaderFactory());

        container.singleton(State.class);

        container.singleton(GameManager.class);
        container.singleton(SysKeysProcessor.class);

        container.singleton(Dummy.class);
        container.singleton(Lwjgl3.class);

        container.link(Drawer.class, Lwjgl3.class);
        container.link(KeyReader.class, Lwjgl3.class);
        container.link(SoundPlayer.class, Dummy.class);

        container.bind(me.ilnicki.bg.core.game.GamesConfig.class, me.ilnicki.bg.pcapp.GamesConfig.class);

        modules.clear();

        modules.addAll(container.getCompatible(RootProcessor.class));
        modules.addAll(container.getCompatible(CoreModule.class));

        modules.load();

        tp.start(modules::update);
    }

    public void stop() {
        modules.stop();
        tp.stop();
    }

    public void reset() {
        stop();
        restartScheduled = true;
    }

    public boolean isRestartScheduled() {
        return restartScheduled;
    }

    public static void main(String[] args) {
        GameRunner runner = new GameRunner();

        do {
            runner.run();
        }
        while (runner.isRestartScheduled());
    }
}
