package me.ilnicki.bg.core.pixelmatrix;

import java.util.Arrays;
import java.util.Comparator;

import me.ilnicki.bg.core.math.Rectangle;
import me.ilnicki.bg.core.math.Vector;

public class Matrices {
  public static final PixelMatrix EMPTY = (
      new ConstantPixelMatrix.Builder(0, 0)
  ).build();

  private static Pixel charToPixel(char value) {
    switch (value) {
      case ' ':
        return Pixel.WHITE;
      case '#':
        return Pixel.BLACK;
      default:
        return null;
    }
  }

  public static PixelMatrix fromString(String... data) {
    final int width =
        Arrays.stream(data)
            .map(String::length)
            .max(Comparator.naturalOrder())
            .orElse(0);

    final int height = data.length;

    MutablePixelMatrix pm = new ArrayPixelMatrix(width, height);

    for (int y = 0; y < data.length; y++) {
      for (int x = 0; x < data[y].length(); x++) {
        pm.setPixel(
            new Vector(x, pm.getHeight() - 1 - y),
            Matrices.charToPixel(data[y].charAt(x))
        );
      }
    }

    return pm;
  }

  public static <M extends MutablePixelMatrix> M clear(M matrix) {
    return fill(matrix, null);
  }

  public static <M extends MutablePixelMatrix> M fill(
      M matrix,
      Pixel fillWith
  ) {
    for (int y = 0; y < matrix.getHeight(); y++) {
      for (int x = 0; x < matrix.getWidth(); x++) {
        matrix.setPixel(new Vector(x, y), fillWith);
      }
    }

    return matrix;
  }

  public static <F extends PixelMatrix, T extends MutablePixelMatrix> T copy(
      F from,
      T to
  ) {
    for (int y = 0; y < from.getHeight(); y++) {
      for (int x = 0; x < from.getWidth(); x++) {
        Vector point = new Vector(x, y);
        to.setPixel(point, from.getPixel(point));
      }
    }

    return to;
  }

  public static PixelMatrix reflect(PixelMatrix pm, ReflectType type) {
    MutablePixelMatrix newPm;

    if (
        (
            type == ReflectType.ON_MAJOR_DIAGONAL
                || type == ReflectType.ON_MINOR_DIAGONAL
        ) && pm.getWidth() != pm.getHeight()
    ) {
      newPm = new ArrayPixelMatrix(pm.getHeight(), pm.getWidth());
    } else {
      newPm = new ArrayPixelMatrix(pm.getWidth(), pm.getHeight());
    }

    for (int y = 0; y < pm.getHeight(); y++) {
      for (int x = 0; x < pm.getWidth(); x++) {
        switch (type) {
          case HORIZONTALLY:
            newPm.setPixel(
                new Vector(x, y),
                pm.getPixel(new Vector(x, pm.getHeight() - 1 - y))
            );
            break;
          case VERTICALLY:
            newPm.setPixel(
                new Vector(x, y),
                pm.getPixel(new Vector(pm.getWidth() - 1 - x, y))
            );
            break;
          case ON_MAJOR_DIAGONAL:
          case ON_MINOR_DIAGONAL:
            throw new UnsupportedOperationException("Not supported yet.");
        }
      }
    }

    return newPm;
  }

  public static PixelMatrix rotate(PixelMatrix pm, int angle) {
    MutablePixelMatrix newMatrix = null;

    angle = normalizeAngle(angle);

    if ((angle > 315 && angle < 360) || (angle >= 0 && angle <= 45)) {
      newMatrix = copy(pm, new ArrayPixelMatrix(pm.getWidth(), pm.getHeight()));
    } else if (angle > 45 && angle <= 135) {
      newMatrix = new ArrayPixelMatrix(pm.getHeight(), pm.getWidth());

      for (int i = 0; i < newMatrix.getWidth(); i++) {
        for (int j = 0; j < newMatrix.getHeight(); j++) {
          newMatrix.setPixel(
              new Vector(i, j),
              pm.getPixel(new Vector(newMatrix.getHeight() - j - 1, i))
          );
        }
      }
    } else if (angle > 135 && angle <= 195) {
      newMatrix = new ArrayPixelMatrix(pm.getWidth(), pm.getHeight());

      for (int y = 0; y < newMatrix.getHeight(); y++) {
        for (int x = 0; x < newMatrix.getWidth(); x++) {
          newMatrix.setPixel(
              new Vector(x, y),
              pm.getPixel(
                  new Vector(newMatrix.getWidth() - x - 1,
                      newMatrix.getHeight() - y - 1)
              )
          );
        }
      }
    } else if (angle > 195 && angle <= 315) {
      newMatrix = new ArrayPixelMatrix(pm.getHeight(), pm.getWidth());

      for (int i = 0; i < newMatrix.getWidth(); i++) {
        for (int j = 0; j < newMatrix.getHeight(); j++) {
          newMatrix.setPixel(
              new Vector(i, j),
              pm.getPixel(new Vector(j, newMatrix.getWidth() - i - 1)));
        }
      }
    }

    return newMatrix;
  }

  public static PixelMatrix crop(PixelMatrix source, Rectangle boundaries) {
    ConstantPixelMatrix.Builder builder =
        new ConstantPixelMatrix.Builder(
            boundaries.getWidth(),
            boundaries.getHeight()
        );

    for (int i = 0; i < boundaries.getWidth(); i++) {
      for (int j = 0; j < boundaries.getHeight(); j++) {
        builder.setPixel(
            new Vector(j, i),
            source.getPixel(boundaries.getPos().add(new Vector(j, i))));
      }
    }

    return builder.build();
  }

  private static int normalizeAngle(int angle) {
    return (angle %= 360) >= 0 ? angle : (angle + 360);
  }

  public enum ReflectType {
    HORIZONTALLY,
    VERTICALLY,
    ON_MAJOR_DIAGONAL,
    ON_MINOR_DIAGONAL
  }
}
