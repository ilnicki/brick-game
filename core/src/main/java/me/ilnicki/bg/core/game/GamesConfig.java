package me.ilnicki.bg.core.game;

import me.ilnicki.bg.core.data.DataBean;

public class GamesConfig implements DataBean {
    private String[] gameManifests = new String[]{};

    private String launcher = DefaultGameLauncher.class.getName();

    public String[] getGameManifests() {
        return gameManifests;
    }

    public void setGameManifests(String[] gameManifests) {
        this.gameManifests = gameManifests;
    }

    public String getLauncher() {
        return launcher;
    }

    public void setLauncher(String launcher) {
        this.launcher = launcher;
    }
}
