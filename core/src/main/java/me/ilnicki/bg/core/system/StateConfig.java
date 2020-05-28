package me.ilnicki.bg.core.system;

import me.ilnicki.bg.core.data.DataBean;

public class StateConfig implements DataBean {
    private int argument = 1;
    private int selectedGame = 0;
    private int hiscore = 0;
    private int level = 1;
    private int speed = 1;
    private int volume = 1;

    public int getArgument() {
        return argument;
    }

    public void setArgument(int argument) {
        this.argument = argument;
    }

    public int getSelectedGame() {
        return selectedGame;
    }

    public void setSelectedGame(int selectedGame) {
        this.selectedGame = selectedGame;
    }

    public int getHiscore() {
        return hiscore;
    }

    public void setHiscore(int hiscore) {
        this.hiscore = hiscore;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
