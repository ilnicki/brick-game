package me.ilnicki.bg.engine;

import me.ilnicki.bg.engine.data.DataProvider;
import me.ilnicki.bg.engine.data.json.JsonDataProvider;
import me.ilnicki.bg.engine.io.Drawer;
import me.ilnicki.bg.engine.io.KeyReader;
import me.ilnicki.bg.engine.io.SoundPlayer;
import me.ilnicki.bg.engine.io.dummy.Dummy;
import me.ilnicki.bg.engine.io.lwjgl3.Lwjgl3;
import me.ilnicki.bg.engine.machine.Machine;
import me.ilnicki.bg.engine.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.engine.pixelmatrix.loaders.PixelMatrixLoaderFactoryProvider;
import me.ilnicki.bg.engine.system.*;
import me.ilnicki.bg.engine.system.container.Container;
import me.ilnicki.bg.engine.system.processors.GameManager;
import me.ilnicki.bg.engine.system.processors.SysKeysProcessor;

public class GameRunner implements Runner {
    private boolean restartScheduled = false;

    private final ModuleSet modules = new ModuleSet();
    private final TickProvider tp = new TickProvider(60);

    public void run() {
        modules.clear();

        Container container = new SystemManager(this);

        container.singleton(DataProvider.class, JsonDataProvider.class);

        container.singleton(Machine.class);

        container.bind(PixelMatrixLoader.class, new PixelMatrixLoaderFactoryProvider());

        container.singleton(SysKeysProcessor.class);
        container.singleton(GameManager.class);

        container.singleton(Dummy.class);
        container.link(Drawer.class, Dummy.class);
        container.link(KeyReader.class, Dummy.class);
        container.link(SoundPlayer.class, Dummy.class);

        modules.addAll(container.getCompatible(CoreModule.class));
        modules.addAll(container.getCompatible(MachineProcessor.class));

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
