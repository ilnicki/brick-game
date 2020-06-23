package me.ilnicki.bg.core.system.processors.gamemanager;

import me.ilnicki.bg.core.game.Game;
import me.ilnicki.bg.core.state.GameState;

public class GameProcess {
    private final Game game;
    private final GameState gameState;

    public GameProcess(Game game, GameState gameState) {
        this.game = game;
        this.gameState = gameState;
    }

    public Game getGame() {
        return game;
    }

    public GameState getGameState() {
        return gameState;
    }
}
