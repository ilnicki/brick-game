package me.ilnicki.bg.core.game;

import me.ilnicki.bg.core.pixelmatrix.Matrices;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;

public interface Manifest {
    default String getName() {
        return "";
    }

    default String getVersion() {
        return "";
    }

    default String getAuthor() {
        return "";
    }

    default String getDescription() {
        return "";
    }

    default String getWebSite() {
        return "";
    }

    default PixelMatrix getLogo() {
        return Matrices.EMPTY;
    }

    default PixelMatrix getPreview() {
        return Matrices.EMPTY;
    }

    Class<? extends Game> getGameClass();
}
