package me.ilnicki.bg.pcapp;

import me.ilnicki.bg.demo.DemoGame;
import me.ilnicki.bg.snake.SnakeGame;

public class GamesConfig extends me.ilnicki.bg.core.game.GamesConfig {
    public GamesConfig() {
        setGames(new String[]{
                DemoGame.class.getName(),
                SnakeGame.class.getName(),
        });
    }
}
