package me.ilnicki.bg.engine.pixelmatrix.loaders;

import me.ilnicki.bg.engine.pixelmatrix.PixelMatrix;

public class InternalBasePixelMatrixLoader extends BasePixelMatrixLoader {
    private final ClassLoader classLoader;
    private final String packageName;

    protected InternalBasePixelMatrixLoader(String packageName, Class clazz) {
        this.packageName = packageName;
        this.classLoader = clazz.getClassLoader();
    }

    @Override
    protected PixelMatrix read(String spriteName) {
        String fullName = String.format("sprites/%s/%s.pmt", this.packageName, spriteName);
        return read(classLoader.getResourceAsStream(fullName));
    }
}