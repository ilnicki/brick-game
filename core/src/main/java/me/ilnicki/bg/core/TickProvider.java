package me.ilnicki.bg.core;

public interface TickProvider {
    void start(TickConsumer tickConsumer);

    void stop();

    interface TickConsumer {
        void consume(long tick);
    }
}
