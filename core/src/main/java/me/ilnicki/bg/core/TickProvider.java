package me.ilnicki.bg.core;

import java.util.Objects;

public class TickProvider {
    private final Synchronizer synchronizer;

    private boolean isRunning;
    private long tickNumber = 0L;

    public TickProvider(int fps) {
        synchronizer = new Synchronizer(fps);
    }

    public void start(TickConsumer tickConsumer) {
        isRunning = true;

        while (isRunning) {
            tickConsumer.consume(tickNumber);
            tickNumber++;
            synchronizer.sync();
        }
    }

    public void stop() {
        isRunning = false;
    }

    public interface TickConsumer {
        void consume(long tick);

        default TickConsumer then(TickConsumer tickConsumer) {
            Objects.requireNonNull(tickConsumer);
            return (tick) -> {
                this.consume(tick);
                tickConsumer.consume(tick);
            };
        }
    }
}
