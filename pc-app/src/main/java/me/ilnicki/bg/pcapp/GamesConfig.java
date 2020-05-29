package me.ilnicki.bg.pcapp;

import me.ilnicki.bg.demo.DemoManifest;
import me.ilnicki.bg.snake.SnakeManifest;
import me.ilnicki.bg.tanks.TanksManifest;
import me.ilnicki.bg.tetris.TetrisManifest;
import me.ilnicki.container.Inject;

public class GamesConfig extends me.ilnicki.bg.core.game.GamesConfig {
    @Inject
    public GamesConfig() {
        setGameManifests(new String[]{
                DemoManifest.class.getName(),
                SnakeManifest.class.getName(),
                TanksManifest.class.getName(),
                TetrisManifest.class.getName(),
        });
    }
}
