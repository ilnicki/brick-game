package me.ilnicki.bg.engine.game;

import me.ilnicki.bg.engine.data.DataBean;
import me.ilnicki.bg.engine.game.test.TestGame;

public class GamesConfig implements DataBean {
    private String[] games = new String[]{
            TestGame.class.getName(),
    };

    private String launcher = DefaultGameLauncher.class.getName();

    public String[] getGames() {
        return games;
    }

    public void setGames(String[] games) {
        this.games = games;
    }

    public String getLauncher() {
        return launcher;
    }

    public void setLauncher(String launcher) {
        this.launcher = launcher;
    }
}
