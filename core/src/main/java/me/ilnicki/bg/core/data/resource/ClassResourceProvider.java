package me.ilnicki.bg.core.data.resource;

import java.io.InputStream;
import java.net.URL;

public class ClassResourceProvider implements ResourceProvider {
  private final Class<?> clazz;

  public ClassResourceProvider(Class<?> clazz) {
    this.clazz = clazz;
  }

  public ClassResourceProvider() {
    this(ClassResourceProvider.class);
  }

  @Override
  public InputStream getResourceAsStream(String name) {
    return clazz.getResourceAsStream(name);
  }

  @Override
  public URL getResource(String name) {
    return clazz.getResource(name);
  }
}
