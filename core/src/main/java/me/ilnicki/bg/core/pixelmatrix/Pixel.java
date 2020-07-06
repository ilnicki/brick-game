package me.ilnicki.bg.core.pixelmatrix;

public enum Pixel {
  BLACK,
  WHITE;

  @Override
  public String toString() {
    return "[" + (this == BLACK ? 'X' : ' ') + "]";
  }

  public static Pixel merge(Pixel upper, Pixel lower) {
    if (upper == null) {
      return lower;
    }

    return upper;
  }

  public static Pixel invert(Pixel pixel) {
    if (pixel == BLACK) {
      return WHITE;
    }

    return BLACK;
  }
}
