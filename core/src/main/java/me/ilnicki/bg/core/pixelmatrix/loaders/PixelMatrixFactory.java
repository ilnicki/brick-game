package me.ilnicki.bg.core.pixelmatrix.loaders;

import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.container.Container;
import me.ilnicki.container.Inject;
import me.ilnicki.container.ProvisionException;
import me.ilnicki.container.provider.Factory;

public class PixelMatrixFactory implements Factory<PixelMatrix> {
  @Inject
  private Container container;

  @Override
  public PixelMatrix produce(String[] args) throws ProvisionException {
    String loaderName = args[0].substring(0, args[0].lastIndexOf('.'));
    String spriteName = args[0].substring(args[0].lastIndexOf('.') + 1);

    return container.get(PixelMatrixLoader.class, loaderName)
        .get(spriteName);
  }
}
