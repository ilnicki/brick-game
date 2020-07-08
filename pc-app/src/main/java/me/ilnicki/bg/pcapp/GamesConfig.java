package me.ilnicki.bg.pcapp;

import java.util.stream.Stream;
import me.ilnicki.bg.demo.DemoManifest;
import me.ilnicki.bg.snake.SnakeManifest;
import me.ilnicki.bg.tanks.TanksManifest;
import me.ilnicki.bg.tetris.TetrisManifest;
import me.ilnicki.container.Inject;

public class GamesConfig extends me.ilnicki.bg.core.game.GamesConfig {
  @Inject
  public GamesConfig() {
    setGameManifests(
        Stream.of(
                DemoManifest.class, SnakeManifest.class, TanksManifest.class, TetrisManifest.class)
            .map(Class::getName)
            .toArray(String[]::new));
  }
}
