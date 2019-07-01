package me.ilnicki.bg.core.game;

import me.ilnicki.bg.core.data.DataBean;

public class GamesConfig implements DataBean {
    private String[] games = new String[]{};

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
