package me.ilnicki.bg.core.system;

public interface App {
    void run();

    void stop();

    void reset();

    boolean isStopped();
}
