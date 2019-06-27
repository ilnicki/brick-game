package me.ilnicki.bg.core.game.test;

import me.ilnicki.bg.core.game.Game;
import me.ilnicki.bg.core.game.GameInfo;
import me.ilnicki.bg.core.pixelmatrix.MatrixUtils;
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
        return MatrixUtils.fromString(
                "  ######  ",
                "  ######  ",
                "    ##    ",
                "    ##    ",
                "    ##    "
        );
    }

    private int tick = 0;
    private PixelMatrix[] prevFrames = new PixelMatrix[]{
            MatrixUtils.fromString(
                    "##########",
                    "#        #",
                    "#        #",
                    "#  ####  #",
                    "#  ####  #",
                    "#        #",
                    "#        #",
                    "##########"
            ),
            MatrixUtils.fromString(
                    "##########",
                    "#        #",
                    "#    #   #",
                    "#   ###  #",
                    "#  ###   #",
                    "#   #    #",
                    "#        #",
                    "##########"
            ),
            MatrixUtils.fromString(
                    "##########",
                    "#        #",
                    "#   ##   #",
                    "#   ##   #",
                    "#   ##   #",
                    "#   ##   #",
                    "#        #",
                    "##########"
            ),
            MatrixUtils.fromString(
                    "##########",
                    "#        #",
                    "#   #    #",
                    "#  ###   #",
                    "#   ###  #",
                    "#    #   #",
                    "#        #",
                    "##########"
            ),
    };

    @Override
    public PixelMatrix getPreview() {
        return prevFrames[tick++ / 10 % prevFrames.length];
    }

    @Override
    public int getBufferWidth() {
        return 10;
    }

    @Override
    public int getBufferHeight() {
        return 20;
    }

    @Override
    public Class<? extends Game> getGameClass() {
        return TestGame.class;
    }
}
