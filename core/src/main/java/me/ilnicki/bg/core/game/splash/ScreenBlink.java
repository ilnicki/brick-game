package me.ilnicki.bg.core.game.splash;

import me.ilnicki.bg.core.math.Vector;
import me.ilnicki.bg.core.pixelmatrix.ArrayPixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Matrices;
import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.animation.Animation;

public class ScreenBlink implements Animation {
  private final int width;
  private final int height;

  private final PixelMatrix on;
  private final PixelMatrix off;

  private PixelMatrix current;

  private final int timesToBlink = 5;
  private final int onTime = 30;
  private final int offTime = 30;

  private int renders;

  public ScreenBlink(int width, int height, Pixel on, Pixel off) {
    this.width = width;
    this.height = height;

    this.on = Matrices.fill(new ArrayPixelMatrix(width, height), on);
    this.off = Matrices.fill(new ArrayPixelMatrix(width, height), off);

    reset();
  }

  public ScreenBlink(int width, int height, Pixel on) {
    this(width, height, on, null);
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public Pixel getPixel(Vector point) {
    return current.getPixel(point);
  }

  @Override
  public Animation next() {
    if (renders % (onTime + offTime) <= offTime) {
      current = off;
    } else {
      current = on;
    }

    renders--;
    return this;
  }

  @Override
  public boolean hasNext() {
    return renders > 0;
  }

  @Override
  public void reset() {
    current = off;
    renders = (onTime + offTime) * timesToBlink;
  }
}
