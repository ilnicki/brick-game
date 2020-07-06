package me.ilnicki.bg.ticknaive;

import me.ilnicki.bg.core.tick.TickProvider;

public class NaiveTickProvider implements TickProvider {
  private final long frameTime;

  private boolean isRunning = true;

  public NaiveTickProvider(int tps) {
    frameTime = Math.round(1e9 / tps);
  }

  @Override
  public void start(TickConsumer tickConsumer) {
    long lastTickTime = System.nanoTime();

    while (isRunning) {
      long tickStart = System.nanoTime();

      tickConsumer.consume((int) (System.nanoTime() - lastTickTime));
      lastTickTime = System.nanoTime();

      while (System.nanoTime() - tickStart < frameTime) {
        try {
          Thread.sleep(1);
        } catch (InterruptedException ignored) {
        }
      }
    }
  }

  @Override
  public void stop() {
    isRunning = false;
  }
}
