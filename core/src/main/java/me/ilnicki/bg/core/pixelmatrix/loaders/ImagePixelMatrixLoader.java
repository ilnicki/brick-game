package me.ilnicki.bg.core.pixelmatrix.loaders;

import me.ilnicki.bg.core.data.resource.ResourceIndex;
import me.ilnicki.bg.core.pixelmatrix.ConstantPixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Vector;
import me.ilnicki.bg.core.data.resource.ResourceProvider;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class ImagePixelMatrixLoader implements PixelMatrixLoader {
    private final Map<String, Future<PixelMatrix>> cache = new ConcurrentHashMap<>();
    private final ThreadPoolExecutor pool = new ThreadPoolExecutor(
            2,
            8,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>()
    );

    private final ResourceIndex index;
    private final ResourceProvider resourceProvider;

    public ImagePixelMatrixLoader(String path, ResourceProvider provider) {
        resourceProvider = provider;
        index = new ResourceIndex(preparePath(path), resourceProvider);
        index.load();
    }

    @Override
    public void load(String... spriteNames) {
        Stream.of(spriteNames)
                .forEach(spriteName -> cache.computeIfAbsent(spriteName, this::scheduleReading));
    }

    @Override
    public PixelMatrix get(String spriteName) {
        if (!index.containsKey(spriteName)) {
            throw new IllegalArgumentException(String.format("Sprite \"%s\" not found in registry.", spriteName));
        }

        try {
            return cache.computeIfAbsent(spriteName, this::scheduleReading).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
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

    private Future<PixelMatrix> scheduleReading(String spriteName) {
        return pool.submit(() -> read(spriteName));
    }

    private PixelMatrix read(String spriteName) {
        String path = index.get(spriteName);

        InputStream in = resourceProvider.getResourceAsStream(path);
        try {
            BufferedImage image = ImageIO.read(in);
            ConstantPixelMatrix.Builder sprite = new ConstantPixelMatrix.Builder(image.getWidth(), image.getHeight());

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    sprite.setPixel(
                            new Vector(x, image.getHeight() - y - 1),
                            rgbToPixel(image.getRGB(x, y))
                    );
                }
            }

            return sprite.build();
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("Sprite %s can not be read.", spriteName));
        }
    }

    private String preparePath(String path) {
        return '/' + path.replace('.', '/') + '/';
    }
}
