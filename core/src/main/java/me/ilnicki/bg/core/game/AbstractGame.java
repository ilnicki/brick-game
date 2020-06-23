package me.ilnicki.bg.core.game;

public abstract class AbstractGame implements Game {
    private Status status = Status.LOADING;

    public Status getStatus() {
        return status;
    }

    public void load() {
        status = Status.RUNNING;
    }

    protected void quit() {
        status = Status.FINISHING;
    }
}
