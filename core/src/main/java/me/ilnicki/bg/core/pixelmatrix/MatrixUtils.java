package me.ilnicki.bg.core.pixelmatrix;

public class MatrixUtils {

    public enum ReflectType {
        HORIZONTALLY,
        VERTICALLY,
        ON_MAJOR_DIAGONAL,
        ON_MINOR_DIAGONAL
    }

    public static PixelMatrix fromArray(Pixel[][] pixelArray) {
        int width = 0;
        for (Pixel[] row : pixelArray)
            if (row.length > width)
                width = row.length;

        PixelMatrix pm = new ArrayPixelMatrix(width, pixelArray.length);

        for (int i = 0; i < pixelArray.length; i++) {
            for (int j = 0; j < pixelArray[i].length; j++) {
                pm.setPixel(j, pm.getHeight() - 1 - i, pixelArray[i][j]);
            }
        }

        return pm;
    }

    public static PixelMatrix fromString(String ...data) {
        int width = 0;

        for (String row : data) {
            if (row.length() > width) {
                width = row.length();
            }
        }

        PixelMatrix pm = new ArrayPixelMatrix(width, data.length);

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length(); j++) {
                pm.setPixel(j, pm.getHeight() - 1 - i, data[i].charAt(j) == ' ' ? Pixel.WHITE : Pixel.BLACK);
            }
        }

        return pm;
    }

    public static void clear(PixelMatrix pm) {
        fill(pm, null);
    }

    public static void fill(PixelMatrix pm, Pixel fillWith) {
        for (int i = 0; i < pm.getWidth(); i++) {
            for (int j = 0; j < pm.getHeight(); j++) {
                pm.setPixel(i, j, fillWith);
            }
        }
    }

    public static PixelMatrix copy(PixelMatrix from) {
        return copy(from, new ArrayPixelMatrix(from.getWidth(), from.getHeight()));
    }

    public static PixelMatrix copy(PixelMatrix from, PixelMatrix to) {
        for (int i = 0; i < from.getWidth(); i++) {
            for (int j = 0; j < from.getHeight(); j++) {
                to.setPixel(i, j, from.getPixel(i, j));
            }
        }

        return to;
    }

    public static PixelMatrix getReflected(PixelMatrix pm, ReflectType type) {
        PixelMatrix newPm;

        if ((type == ReflectType.ON_MAJOR_DIAGONAL || type == ReflectType.ON_MINOR_DIAGONAL)
                && pm.getWidth() != pm.getHeight()) {
            newPm = new ArrayPixelMatrix(pm.getHeight(), pm.getWidth());
        } else {
            newPm = new ArrayPixelMatrix(pm.getWidth(), pm.getHeight());
        }

        for (int i = 0; i < pm.getWidth(); i++) {
            for (int j = 0; j < pm.getHeight(); j++) {
                switch (type) {
                    case HORIZONTALLY:
                        newPm.setPixel(i, j, pm.getPixel(i, pm.getHeight() - 1 - j));
                        break;
                    case VERTICALLY:
                        newPm.setPixel(i, j, pm.getPixel(pm.getWidth() - 1 - i, j));
                        break;
                    case ON_MAJOR_DIAGONAL:
                        throw new UnsupportedOperationException("Not supported yet.");
                    case ON_MINOR_DIAGONAL:
                        throw new UnsupportedOperationException("Not supported yet.");
                }
            }
        }

        return newPm;
    }

    public static PixelMatrix getRotated(PixelMatrix pm, int angle) {
        PixelMatrix newMatrix = null;

        angle = normalizeAngle(angle);

        if ((angle > 315 && angle < 360)
                || (angle >= 0 && angle <= 45)) {
            newMatrix = copy(pm);
        } else if (angle > 45 && angle <= 135) {
            newMatrix = new ArrayPixelMatrix(pm.getHeight(), pm.getWidth());

            for (int i = 0; i < newMatrix.getWidth(); i++) {
                for (int j = 0; j < newMatrix.getHeight(); j++) {
                    newMatrix.setPixel(i, j,
                            pm.getPixel(newMatrix.getHeight() - j - 1, i));
                }
            }
        } else if (angle > 135 && angle <= 195) {
            newMatrix = new ArrayPixelMatrix(pm.getWidth(), pm.getHeight());

            for (int i = 0; i < newMatrix.getWidth(); i++) {
                for (int j = 0; j < newMatrix.getHeight(); j++) {
                    newMatrix.setPixel(i, j,
                            pm.getPixel(newMatrix.getWidth() - i - 1,
                                    newMatrix.getHeight() - j - 1));
                }
            }
        } else if (angle > 195 && angle <= 315) {
            newMatrix = new ArrayPixelMatrix(pm.getHeight(), pm.getWidth());

            for (int i = 0; i < newMatrix.getWidth(); i++) {
                for (int j = 0; j < newMatrix.getHeight(); j++) {
                    newMatrix.setPixel(i, j,
                            pm.getPixel(j, newMatrix.getWidth() - i - 1));
                }
            }
        }

        return newMatrix;
    }

    private static int normalizeAngle(int angle) {
        return (angle %= 360) >= 0 ? angle : (angle + 360);
    }
}
