package me.ilnicki.bg.engine.pixelmatrix.loaders.internal;

import me.ilnicki.bg.engine.pixelmatrix.ArrayPixelMatrix;
import me.ilnicki.bg.engine.pixelmatrix.Pixel;
import me.ilnicki.bg.engine.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.engine.pixelmatrix.loaders.PixelMatrixLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class InternalPixelMatrixLoader implements PixelMatrixLoader {
    private final Map<String, PixelMatrix> sprites = new ConcurrentHashMap<>();
    private final Deque<String> loadingQueue = new ConcurrentLinkedDeque<>();
    private final ResourceIndex index;


    public InternalPixelMatrixLoader(String path) {
        path = this.preparePath(path);

        index = new ResourceIndex(path);
        index.load();
    }

    @Override
    public PixelMatrix load(String spriteName, boolean shouldBeCached) {
        if (!index.containsKey(spriteName)) {
            throw new IllegalArgumentException(String.format("Sprite %s not found in registry.", spriteName));
        }

        if (!sprites.containsKey(spriteName)) {
            loadingQueue.remove(spriteName);
        }

        return sprites.computeIfAbsent(spriteName, this::read);
    }

    private PixelMatrix read(String spriteName) {
        String path = index.get(spriteName);

        InputStream in = getClass().getResourceAsStream(path);
        try {
            BufferedImage image = ImageIO.read(in);
            PixelMatrix sprite = new ArrayPixelMatrix(image.getWidth(), image.getHeight());

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    sprite.setPixel(
                            x,
                            sprite.getHeight() - y - 1,
                            image.getRGB(x, y) == 0xFF000000 ? Pixel.BLACK : Pixel.WHITE
                    );
                }
            }

            return sprite;
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("Sprite %s can not be read.", spriteName));
        }
    }

    private String preparePath(String path) {
        return '/' + path.replace('.', '/') + '/';
    }
}
