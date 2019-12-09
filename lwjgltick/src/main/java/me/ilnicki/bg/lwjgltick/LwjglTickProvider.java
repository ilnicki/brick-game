package me.ilnicki.bg.lwjgltick;

import me.ilnicki.bg.core.TickProvider;

public class LwjglTickProvider implements TickProvider {
    private final Synchronizer synchronizer;

    private boolean isRunning;
    private long tickNumber = 0L;

    public LwjglTickProvider(int fps) {
        synchronizer = new Synchronizer(fps);
    }

    public void start(TickConsumer tickConsumer) {
        isRunning = true;

        while (isRunning) {
            tickConsumer.consume(tickNumber++);
            synchronizer.sync();
        }
    }

    public void stop() {
        isRunning = false;
    }
}
