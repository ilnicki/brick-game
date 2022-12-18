package me.ilnicki.bg.core.pixelmatrix.loaders;

import me.ilnicki.bg.core.data.resource.ResourceProvider;
import me.ilnicki.bg.core.math.Vector;
import me.ilnicki.bg.core.pixelmatrix.ConstantPixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ImagePixelMatrixReader {
  private final InputStream inputStream;

  public ImagePixelMatrixReader(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  public PixelMatrix read() throws IOException {
    BufferedImage image = ImageIO.read(inputStream);
    ConstantPixelMatrix.Builder sprite =
        new ConstantPixelMatrix.Builder(image.getWidth(), image.getHeight());

    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        sprite.setPixel(new Vector(x, image.getHeight() - y - 1), rgbToPixel(image.getRGB(x, y)));
      }
    }

    return sprite.build();
  }

  private Pixel rgbToPixel(int color) {
    switch (color) {
      case 0xFF000000:
        return Pixel.BLACK;
      case 0xFFFFFFFF:
        return Pixel.WHITE;
      default:
        return null;
    }
  }

  public static ImagePixelMatrixReader fromResourceProvider(ResourceProvider provider, String url) {
    InputStream in = provider.getResourceAsStream(url);
    return new ImagePixelMatrixReader(in);
  }
}
