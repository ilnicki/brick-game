package me.ilnicki.bg.engine;

import me.ilnicki.bg.engine.data.DataProvider;
import me.ilnicki.bg.engine.data.json.JsonDataProvider;
import me.ilnicki.bg.engine.io.Drawer;
import me.ilnicki.bg.engine.io.KeyReader;
import me.ilnicki.bg.engine.io.SoundPlayer;
import me.ilnicki.bg.engine.io.dummy.Dummy;
import me.ilnicki.bg.engine.machine.Machine;
import me.ilnicki.bg.engine.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.engine.pixelmatrix.loaders.PixelMatrixLoaderFactory;
import me.ilnicki.bg.engine.system.*;
import me.ilnicki.bg.engine.system.processors.GameManager;
import me.ilnicki.bg.engine.system.processors.SysKeysProcessor;

public class GameRunner implements Runner {
    private boolean restartScheduled = false;

    private final ModuleSet modules = new ModuleSet();
    private final TickProvider tp = new TickProvider(60);

    public void run() {
        modules.clear();

        SystemManager systemManager = new SystemManager(this);

        systemManager.singleton(DataProvider.class, JsonDataProvider.class);

        systemManager.singleton(Machine.class);

        systemManager.bind(PixelMatrixLoader.class, new PixelMatrixLoaderFactory());

        systemManager.singleton(SysKeysProcessor.class);
        systemManager.singleton(GameManager.class);

        systemManager.singleton(Dummy.class);
        systemManager.link(Drawer.class, Dummy.class);
        systemManager.link(KeyReader.class, Dummy.class);
        systemManager.link(SoundPlayer.class, Dummy.class);

        modules.addAll(systemManager.getCompatible(CoreModule.class));
        modules.addAll(systemManager.getCompatible(MachineProcessor.class));

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
