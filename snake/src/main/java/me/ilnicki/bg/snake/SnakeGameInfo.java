package me.ilnicki.bg.snake;

import me.ilnicki.bg.core.game.Game;
import me.ilnicki.bg.core.game.GameInfo;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.core.system.container.Args;
import me.ilnicki.bg.core.system.container.Inject;

public class SnakeGameInfo implements GameInfo {
    @Inject
    @Args({"internal", "assets.sprites.snake"})
    private PixelMatrixLoader matrixLoader;

    @Override
    public String getAuthor() {
        return "Dmytro Ilnicki";
    }

    @Override
    public int getBufferHeight() {
        return 20;
    }

    @Override
    public int getBufferWidth() {
        return 10;
    }

    @Override
    public String getDescription() {
        return "Classic snake game.";
    }

    @Override
    public Class<? extends Game> getGameClass() {
        return SnakeGame.class;
    }

    @Override
    public PixelMatrix getLogo() {
        return matrixLoader.load("logo", true);
    }

    @Override
    public String getName() {
        return "Snake Game";
    }

    @Override
    public PixelMatrix getPreview() {
        return matrixLoader.load("preview", true);
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    @Override
    public String getWebSite() {
        return "http://ilnicki.me";
    }

}
