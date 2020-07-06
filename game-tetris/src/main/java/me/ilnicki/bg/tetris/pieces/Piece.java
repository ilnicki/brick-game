package me.ilnicki.bg.tetris.pieces;

import java.util.Map;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;

public abstract class Piece {
  Map<Angle, PixelMatrix> sprites;
  private Angle angle;

  void setAngle(Angle angle) {
    this.angle = angle;
  }

  public PixelMatrix getSprite() {
    return sprites.get(this.angle);
  }

  public void rotate() {
    this.angle = this.angle.next();
  }

  protected enum Angle {
    DEG0 {
      @Override
      public Angle next() {
        return DEG90;
      }
    },
    DEG90 {
      @Override
      public Angle next() {
        return DEG180;
      }
    },
    DEG180 {
      @Override
      public Angle next() {
        return DEG270;
      }
    },
    DEG270 {
      @Override
      public Angle next() {
        return DEG0;
      }
    };

    public abstract Angle next();
  }
}
