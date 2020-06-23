package me.ilnicki.eventloop;

import java.util.ArrayDeque;
import java.util.Queue;

public class EventLoop {
    private Queue<Runnable> currentQueue = new ArrayDeque<>();
    private Queue<Runnable> nextQueue = new ArrayDeque<>();

    public void add(Runnable runnable) {
        nextQueue.add(runnable);
    }

    public void execute() {
        swapQueues();

        while (!currentQueue.isEmpty()) {
            currentQueue.poll().run();
        }
    }

    private void swapQueues() {
        final Queue<Runnable> temp = currentQueue;
        currentQueue = nextQueue;
        nextQueue = temp;
    }
}
