package me.ilnicki.bg.engine.pixelmatrix.loaders;

import me.ilnicki.bg.engine.pixelmatrix.PixelMatrix;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ExternalBasePixelMatrixLoader extends BasePixelMatrixLoader {
    private final File texturesPackage;

    protected ExternalBasePixelMatrixLoader(String packageName, String path) {
        this.texturesPackage = new File(path + "/sprites/", packageName);
        if (!this.texturesPackage.exists())
            throw new IllegalArgumentException(String.format("Package %s is not exist.", packageName));
    }

    @Override
    protected PixelMatrix read(String spriteName) {
        File texture = new File(this.texturesPackage, spriteName + ".pmt");
        try {
            return read(new FileInputStream(texture));
        } catch (FileNotFoundException ex) {
            return null;
        }
    }
}
