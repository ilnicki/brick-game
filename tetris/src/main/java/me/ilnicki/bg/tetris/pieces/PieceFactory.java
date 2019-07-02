package me.ilnicki.bg.tetris.pieces;

import me.ilnicki.bg.core.pixelmatrix.MatrixUtils;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;

import java.util.*;

public class PieceFactory {
    private final PixelMatrixLoader loader;
    private final List<Class<? extends Piece>> pieceTypes = new ArrayList<>();
    private final Map<Class<? extends Piece>, Map<Piece.Angle, PixelMatrix>> spriteMap = new HashMap<>();
    private final Random random = new Random();

    public PieceFactory(PixelMatrixLoader loader) {
        this.loader = loader;

        addPiece(IPiece.class);
        addPiece(JPiece.class);
        addPiece(LPiece.class);
        addPiece(OPiece.class);
        addPiece(SPiece.class);
        addPiece(TPiece.class);
        addPiece(ZPiece.class);
    }

    public Piece make() {
        Class<? extends Piece> pieceType = pieceTypes.get(random.nextInt(pieceTypes.size()));
        try {
            Piece piece = pieceType.newInstance();
            piece.sprites = spriteMap.get(pieceType);

            Piece.Angle[] values = Piece.Angle.values();
            piece.setAngle(values[random.nextInt(values.length)]);

            return piece;
        } catch (InstantiationException | IllegalAccessException ex) {
            return null;
        }
    }

    private void addPiece(Class<? extends Piece> pieceClass) {
        pieceTypes.add(pieceClass);

        HashMap<Piece.Angle, PixelMatrix> sprites = new HashMap<>();

        PixelMatrix sprite = loader.load(pieceClass.getSimpleName(), true);
        sprites.put(Piece.Angle.DEG0, sprite);
        sprites.put(Piece.Angle.DEG90, MatrixUtils.getRotated(sprite, 90));
        sprites.put(Piece.Angle.DEG180, MatrixUtils.getRotated(sprite, 180));
        sprites.put(Piece.Angle.DEG270, MatrixUtils.getRotated(sprite, 270));

        spriteMap.put(pieceClass, Collections.unmodifiableMap(sprites));
    }
}
