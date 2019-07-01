package me.ilnicki.bg.pcapp;

import me.ilnicki.bg.demo.DemoManifest;
import me.ilnicki.bg.snake.SnakeManifest;

public class GamesConfig extends me.ilnicki.bg.core.game.GamesConfig {
    public GamesConfig() {
        setGameManifests(new String[]{
                DemoManifest.class.getName(),
                SnakeManifest.class.getName(),
        });
    }
}
