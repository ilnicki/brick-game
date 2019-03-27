package me.ilnicki.bg.engine.game;

import me.ilnicki.bg.engine.pixelmatrix.PixelMatrix;

public interface GameInfo {
    String getName();
    String getVersion();
    String getAuthor();
    String getDescription();
    String getWebSite();
    PixelMatrix getLogo();
    PixelMatrix getPreview();
    int getBufferWidth();
    int getBufferHeight();
    Class<? extends Game> getGameClass();
}
