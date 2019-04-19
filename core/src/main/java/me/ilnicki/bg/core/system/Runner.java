package me.ilnicki.bg.core.system;

public interface Runner {
    void run();

    void stop();

    void reset();

    boolean isRestartScheduled();
}
