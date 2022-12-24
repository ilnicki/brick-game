package me.ilnicki.bg.core.game.splash;

import me.ilnicki.bg.core.math.Vector;
import me.ilnicki.bg.core.pixelmatrix.ArrayPixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Matrices;
import me.ilnicki.bg.core.pixelmatrix.MutablePixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.animation.Animation;

public class Spiral implements Animation {
  private final int width;
  private final int height;
  private MutablePixelMatrix canvas;
  private SpiralGenerator spiralGenerator;

  public Spiral(int width, int height) {
    this.width = width;
    this.height = height;

    setup();
  }

  @Override
  public int getWidth() {
    return canvas.getWidth();
  }

  @Override
  public int getHeight() {
    return canvas.getHeight();
  }

  @Override
  public Pixel getPixel(Vector point) {
    return canvas.getPixel(point);
  }

  @Override
  public Animation next() {
    final Vector pos = spiralGenerator.next();

    if (pos != null) {
      canvas.setPixel(pos, Pixel.invert(canvas.getPixel(pos)));
    }

    return this;
  }

  @Override
  public boolean hasNext() {
    return spiralGenerator.hasNext();
  }

  @Override
  public void reset() {
    setup();
  }

  private void setup() {
    canvas = Matrices.fill(new ArrayPixelMatrix(
        width,
        height
    ), Pixel.WHITE);

    spiralGenerator = new SpiralGenerator(
        width,
        height
    );
  }
}
