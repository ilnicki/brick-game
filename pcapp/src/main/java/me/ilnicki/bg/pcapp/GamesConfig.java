package me.ilnicki.bg.pcapp;

import me.ilnicki.bg.core.game.test.TestGame;
import me.ilnicki.bg.snake.SnakeGame;

public class GamesConfig extends me.ilnicki.bg.core.game.GamesConfig {
    public GamesConfig() {
        setGames(new String[]{
                TestGame.class.getName(),
                SnakeGame.class.getName(),
        });
    }
}
