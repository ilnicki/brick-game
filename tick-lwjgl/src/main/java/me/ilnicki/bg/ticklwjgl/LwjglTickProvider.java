package me.ilnicki.bg.ticklwjgl;

import me.ilnicki.bg.core.TickProvider;

public class LwjglTickProvider implements TickProvider {
    private final Synchronizer synchronizer;

    private boolean isRunning;
    private long lastTickTime;

    public LwjglTickProvider(int tps) {
        synchronizer = new Synchronizer(tps);
    }

    public void start(TickConsumer tickConsumer) {
        if (isRunning) throw new IllegalStateException("Consumer is already provided and running.");

        isRunning = true;
        lastTickTime = synchronizer.getTime();

        while (isRunning) {
            tickConsumer.consume((int) (synchronizer.getTime() - lastTickTime));
            lastTickTime = synchronizer.getTime();
            synchronizer.sync();
        }
    }

    public void stop() {
        isRunning = false;
    }
}
