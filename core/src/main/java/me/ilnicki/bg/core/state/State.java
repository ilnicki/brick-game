package me.ilnicki.bg.core.state;

public class State {
    private final SystemState systemState = new SystemState();
    private GameState gameState = new GameState();

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public SystemState getSystemState() {
        return systemState;
    }
}
