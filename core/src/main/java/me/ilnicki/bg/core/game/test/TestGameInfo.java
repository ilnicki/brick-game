package me.ilnicki.bg.core.game.test;

import me.ilnicki.bg.core.game.Game;
import me.ilnicki.bg.core.game.GameInfo;
import me.ilnicki.bg.core.pixelmatrix.ArrayPixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;

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
        return new ArrayPixelMatrix(1, 1);
    }

    @Override
    public PixelMatrix getPreview() {
        return new ArrayPixelMatrix(1, 1);
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
