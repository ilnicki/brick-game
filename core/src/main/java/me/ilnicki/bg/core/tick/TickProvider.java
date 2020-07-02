package me.ilnicki.bg.core.tick;

public interface TickProvider {
    void start(TickConsumer tickConsumer);

    void stop();

    interface TickConsumer {
        void consume(int delta);
    }
}
