package me.ilnicki.bg.core.pixelmatrix.loaders;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import me.ilnicki.bg.core.data.resource.ResourceIndex;
import me.ilnicki.bg.core.data.resource.ResourceProvider;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;

public class ImagePixelMatrixLoader implements PixelMatrixLoader {
  private final Map<String, Future<PixelMatrix>> cache = new ConcurrentHashMap<>();
  private final ThreadPoolExecutor pool = new ThreadPoolExecutor(
      2,
      8,
      0L,
      TimeUnit.MILLISECONDS,
      new LinkedBlockingQueue<>(),
      new DaemonThreadFactory()
  );

  private final ResourceIndex index;
  private final ResourceProvider resourceProvider;

  public ImagePixelMatrixLoader(
      ResourceIndex index,
      ResourceProvider resourceProvider
  ) {
    this.index = index;
    this.resourceProvider = resourceProvider;
  }

  @Override
  public void load(String... spriteNames) {
    Stream.of(spriteNames)
        .forEach(spriteName -> cache.computeIfAbsent(spriteName, this::scheduleReading));
  }

  @Override
  public PixelMatrix get(String spriteName) {
    if (!index.containsKey(spriteName)) {
      throw new IllegalArgumentException(
          String.format("Sprite \"%s\" not found in registry.", spriteName));
    }

    try {
      return cache.computeIfAbsent(spriteName, this::scheduleReading).get();
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  private Future<PixelMatrix> scheduleReading(String spriteName) {
    return pool.submit(() -> read(spriteName));
  }

  private PixelMatrix read(String spriteName) {
    ImagePixelMatrixReader reader = ImagePixelMatrixReader.fromResourceProvider(
        resourceProvider,
        index.get(spriteName)
    );

    try {
      return reader.read();
    } catch (IOException e) {
      throw new IllegalArgumentException(String.format("Sprite %s can not be read.", spriteName));
    }
  }

  private static class DaemonThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    private final ThreadGroup group;
    private final String namePrefix;

    DaemonThreadFactory() {
      SecurityManager s = System.getSecurityManager();
      group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
      namePrefix = String.format("loader-pool-%d-thread-", poolNumber.getAndIncrement());
    }

    public Thread newThread(Runnable r) {
      Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
      t.setDaemon(true);
      return t;
    }
  }

  public static ImagePixelMatrixLoader forDirectory(
      String path,
      ResourceProvider resourceProvider
  ) {
    ResourceIndex index = new ImagesResourceIndex(path, resourceProvider);
    index.load();

    return new ImagePixelMatrixLoader(index, resourceProvider);
  }
}
