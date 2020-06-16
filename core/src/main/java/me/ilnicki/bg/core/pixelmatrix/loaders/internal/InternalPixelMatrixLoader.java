package me.ilnicki.bg.core.pixelmatrix.loaders.internal;

import me.ilnicki.bg.core.pixelmatrix.ConstantPixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Vector;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;

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
        index = new ResourceIndex(preparePath(path));
        index.load();
    }

    @Override
    public PixelMatrix load(String spriteName, boolean shouldBeCached) {
        if (!index.containsKey(spriteName)) {
            throw new IllegalArgumentException(String.format("Sprite \"%s\" not found in registry.", spriteName));
        }

        if (!sprites.containsKey(spriteName)) {
            loadingQueue.remove(spriteName);
        }

        return sprites.computeIfAbsent(spriteName, this::read);
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

    private PixelMatrix read(String spriteName) {
        String path = index.get(spriteName);

        InputStream in = getClass().getResourceAsStream(path);
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
