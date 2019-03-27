package me.ilnicki.bg.engine.game.test;

import me.ilnicki.bg.engine.game.Game;
import me.ilnicki.bg.engine.game.GameInfo;
import me.ilnicki.bg.engine.pixelmatrix.PixelMatrix;

public class TestGameInfo implements GameInfo {
    @Override
    public String getName() {
        return "Test Game";
    }

    @Override
    public String getVersion() {
        return "0.0.1";
    }

    @Override
    public String getAuthor() {
        return "ilnicki";
    }

    @Override
    public String getDescription() {
        return "Test Game";
    }

    @Override
    public String getWebSite() {
        return "http://ilnicki.me";
    }

    @Override
    public PixelMatrix getLogo() {
        return null;
    }

    @Override
    public PixelMatrix getPreview() {
        return null;
    }

    @Override
    public int getBufferWidth() {
        return 0;
    }

    @Override
    public int getBufferHeight() {
        return 0;
    }

    @Override
    public Class<? extends Game> getGameClass() {
        return TestGame.class;
    }
}
