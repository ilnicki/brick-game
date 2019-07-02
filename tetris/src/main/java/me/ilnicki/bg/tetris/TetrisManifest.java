package me.ilnicki.bg.tetris;

import me.ilnicki.bg.core.game.Game;
import me.ilnicki.bg.core.game.Manifest;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.core.system.container.Args;
import me.ilnicki.bg.core.system.container.Inject;

public class TetrisManifest implements Manifest
{
    @Inject
    @Args({"internal", "assets.sprites.tetris"})
    private PixelMatrixLoader matrixLoader;

    @Override
    public String getName()
    {
        return "Tetris";
    }

    @Override
    public String getVersion()
    {
        return "0.1";
    }
    
    @Override
    public String getAuthor()
    {
        return "Dmytro Ilnicki";
    }

    @Override
    public String getDescription()
    {
        return "Classic tetris game";
    }

    @Override
    public String getWebSite()
    {
        return "http://ilnicki.me";
    }

    @Override
    public PixelMatrix getLogo() {
        return matrixLoader.load("logo", true);
    }


    @Override
    public PixelMatrix getPreview() {
        return matrixLoader.load("preview", true);
    }
    
    @Override
    public int getBufferWidth()
    {
        return 10;
    }

    @Override
    public int getBufferHeight()
    {
        return 20 + 4;
    }

    @Override
    public Class<? extends Game> getGameClass()
    {
        return TetrisGame.class;
    }
}
