package me.ilnicki.bg.tetris;

import me.ilnicki.bg.core.game.AbstractGame;
import me.ilnicki.bg.core.game.Game;
import me.ilnicki.bg.core.pixelmatrix.ArrayPixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.MutablePixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.layering.Layer;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.core.state.Field;
import me.ilnicki.bg.core.state.Helper;
import me.ilnicki.bg.core.state.buttons.ButtonsState;
import me.ilnicki.bg.core.system.processors.gamemanager.GameManager;
import me.ilnicki.bg.tetris.pieces.Piece;
import me.ilnicki.bg.tetris.pieces.PieceFactory;
import me.ilnicki.container.Args;
import me.ilnicki.container.Inject;

public class TetrisGame extends AbstractGame {
    @Inject
    private GameManager gameManager;

    private final MutablePixelMatrix field;

    @Inject
    private Helper helper;

    @Inject
    private ButtonsState keyboard;

    @Inject
    @Args({"internal", "assets.sprites.tetris.pieces"})
    private PixelMatrixLoader unitsLoader;


    private PieceFactory factory;

    private Piece currentPiece;

    private Piece nextPiece;

    @Inject
    public TetrisGame(Field field) {
        field.getLayers().add(new Layer<>(this.field = new ArrayPixelMatrix(10, 20)));
    }

    @Override
    public void load() {
        this.currentPiece = this.factory.make();
        this.nextPiece = this.factory.make();

        super.load();
    }

    @Override
    public void update(int delta) {

    }
}
