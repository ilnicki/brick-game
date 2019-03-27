package me.ilnicki.bg.engine;

import java.util.Objects;

class TickProvider {
    private final Synchronizer synchronizer;

    private boolean isRunning;
    private long tickNumber = 0L;

    TickProvider(int fps) {
        synchronizer = new Synchronizer(fps);
    }

    void start(TickConsumer tickConsumer) {
        isRunning = true;

        while (isRunning) {
            tickConsumer.consume(tickNumber);
            tickNumber++;
            synchronizer.sync();
        }
    }

    void stop() {
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
