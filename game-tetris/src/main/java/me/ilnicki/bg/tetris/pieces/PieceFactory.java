package me.ilnicki.bg.tetris.pieces;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import me.ilnicki.bg.core.pixelmatrix.Matrices;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;

public class PieceFactory {
  private final PixelMatrixLoader loader;
  private final List<Class<? extends Piece>> pieceTypes = new ArrayList<>();
  private final Map<Class<? extends Piece>, Map<Piece.Angle, PixelMatrix>> spriteMap =
      new HashMap<>();
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
      Piece piece = pieceType.getDeclaredConstructor().newInstance();
      piece.sprites = spriteMap.get(pieceType);

      Piece.Angle[] values = Piece.Angle.values();
      piece.setAngle(values[random.nextInt(values.length)]);

      return piece;
    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      return null;
    }
  }

  private void addPiece(Class<? extends Piece> pieceClass) {
    pieceTypes.add(pieceClass);

    HashMap<Piece.Angle, PixelMatrix> sprites = new HashMap<>();

    PixelMatrix sprite = loader.get(pieceClass.getSimpleName());
    sprites.put(Piece.Angle.DEG0, sprite);
    sprites.put(Piece.Angle.DEG90, Matrices.rotate(sprite, 90));
    sprites.put(Piece.Angle.DEG180, Matrices.rotate(sprite, 180));
    sprites.put(Piece.Angle.DEG270, Matrices.rotate(sprite, 270));

    spriteMap.put(pieceClass, Collections.unmodifiableMap(sprites));
  }
}
