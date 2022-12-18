package me.ilnicki.bg.core.pixelmatrix.loaders;

import me.ilnicki.bg.core.data.resource.ClassResourceProvider;
import me.ilnicki.bg.core.data.resource.ResourceProvider;
import me.ilnicki.container.ProvisionException;
import me.ilnicki.container.provider.Factory;

public class PixelMatrixLoaderFactory implements Factory<PixelMatrixLoader> {
  private final ResourceProvider resourceProvider = new ClassResourceProvider();

  @Override
  public PixelMatrixLoader produce(String[] args) throws ProvisionException {
    if (args.length > 0) {
      return ImagePixelMatrixLoader.forDirectory(args[0], resourceProvider);
    }

    return null;
  }
}
