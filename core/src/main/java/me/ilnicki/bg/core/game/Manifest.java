package me.ilnicki.bg.core.game;

import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;

public interface Manifest {
    String getName();

    String getVersion();

    String getAuthor();

    String getDescription();

    String getWebSite();

    PixelMatrix getLogo();

    PixelMatrix getPreview();

    Class<? extends Game> getGameClass();
}
