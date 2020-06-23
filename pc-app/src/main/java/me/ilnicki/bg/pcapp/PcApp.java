package me.ilnicki.bg.pcapp;

import me.ilnicki.bg.core.TickProvider;
import me.ilnicki.bg.core.io.Drawer;
import me.ilnicki.bg.core.io.ButtonReader;
import me.ilnicki.bg.core.io.SoundPlayer;
import me.ilnicki.bg.core.io.dummy.Dummy;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoaderFactory;
import me.ilnicki.bg.core.state.State;
import me.ilnicki.bg.core.system.CoreModule;
import me.ilnicki.bg.core.system.ModuleSet;
import me.ilnicki.bg.core.system.App;
import me.ilnicki.bg.core.system.SystemManager;
import me.ilnicki.bg.core.system.processors.gamemanager.GameManager;
import me.ilnicki.bg.core.system.processors.SystemButtonsProcessor;
import me.ilnicki.bg.iolwjgl3opengl.Lwjgl3;
import me.ilnicki.bg.ticklwjgl.LwjglTickProvider;
import me.ilnicki.container.Container;

public class PcApp implements App {
    private boolean stopped = false;

    private final ModuleSet modules = new ModuleSet();
    private final TickProvider tp = new LwjglTickProvider(60);

    public void run() {
        Container container = new SystemManager(this);

        container.singleton(State.class);

        container.singleton(GameManager.class);
        container.singleton(SystemButtonsProcessor.class);

        container.singleton(Dummy.class);
        container.singleton(Lwjgl3.class);

        container.link(Drawer.class, Lwjgl3.class);
        container.link(ButtonReader.class, Lwjgl3.class);
        container.link(SoundPlayer.class, Dummy.class);

        container.bind(PixelMatrixLoader.class, new PixelMatrixLoaderFactory());

        container.bind(me.ilnicki.bg.core.game.GamesConfig.class, me.ilnicki.bg.pcapp.GamesConfig.class);

        modules.addAll(container.getCompatible(CoreModule.class));
        modules.load();
        tp.start(modules::update);
    }

    public void stop() {
        stopModules();
        stopped = true;
    }

    public void reset() {
        stopModules();
    }

    public boolean isStopped() {
        return stopped;
    }

    private void stopModules() {
        modules.stop();
        tp.stop();
    }

    public static void main(String[] args) {
        PcApp app = new PcApp();

        do {
            app.run();
        }
        while (!app.isStopped());
    }
}
