package me.ilnicki.bg.ticklwjgl;

import me.ilnicki.bg.core.TickProvider;

public class LwjglTickProvider implements TickProvider {
    private final Synchronizer synchronizer;

    private boolean isRunning;
    private long tickNumber = 0L;

    public LwjglTickProvider(int fps) {
        synchronizer = new Synchronizer(fps);
    }

    public void start(TickConsumer tickConsumer) {
        if (isRunning) throw new IllegalStateException("Consumer is already provided and running.");

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
