package me.ilnicki.bg.engine.pixelmatrix.loaders;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.HashMap;

import me.ilnicki.bg.engine.data.DataProvider;
import me.ilnicki.bg.engine.game.Game;
import me.ilnicki.bg.engine.game.GameInfo;
import me.ilnicki.bg.engine.pixelmatrix.ArrayPixelMatrix;
import me.ilnicki.bg.engine.pixelmatrix.Pixel;
import me.ilnicki.bg.engine.pixelmatrix.PixelMatrix;

public abstract class BasePixelMatrixLoader implements PixelMatrixLoader {
    protected final HashMap<String, PixelMatrix> cache;

    public BasePixelMatrixLoader() {
        cache = new HashMap<>();
    }

    public PixelMatrix load(String spriteName, boolean shouldBeCached) {
        if (shouldBeCached) {
            PixelMatrix pm = cache.get(spriteName);

            if (pm == null) {
                cache.put(spriteName, pm = read(spriteName));
            }

            return pm;
        } else {
            return read(spriteName);
        }
    }

    protected abstract PixelMatrix read(String spriteName);

    protected PixelMatrix read(InputStream is) {
        try (DataInputStream input = new DataInputStream(is)) {
            if (input.readByte() == 0x01) {
                short width = input.readShort();
                short height = input.readShort();

                PixelMatrix pixelMatrix = new ArrayPixelMatrix(width, height);

                for (int i = height - 1; i >= 0; i--) {
                    boolean newLine = false;

                    for (int j = 0; j < width && !newLine; j++) {
                        switch (input.readByte()) {
                            case 0x01:
                                pixelMatrix.setPixel(j, i, Pixel.BLACK);
                                break;
                            case 0x02:
                                pixelMatrix.setPixel(j, i, Pixel.WHITE);
                                break;
                            case 0x03:
                                newLine = true;
                                break;
                        }
                    }
                }

                return pixelMatrix;
            }
        } catch (Exception ignored) {
        }

        return null;
    }

    public void clearCache() {
        cache.clear();
    }

    public static PixelMatrixLoader create(String packageName, Object pathSource) {
        if (pathSource instanceof DataProvider) {
            return new ExternalBasePixelMatrixLoader(packageName, ((DataProvider) pathSource).getLocation());
        } else if (pathSource instanceof GameInfo) {
            return new InternalBasePixelMatrixLoader(packageName, pathSource.getClass());
        } else if (pathSource instanceof Game) {
            return new InternalBasePixelMatrixLoader(packageName, pathSource.getClass());
        } else if (pathSource.getClass().isInstance(Game.class)) {
            return new InternalBasePixelMatrixLoader(packageName, (Class) pathSource);
        } else {
            return null;
        }
    }

}
