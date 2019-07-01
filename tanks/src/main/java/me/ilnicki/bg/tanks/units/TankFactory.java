package me.ilnicki.bg.tanks.units;

import me.ilnicki.bg.core.pixelmatrix.MatrixUtils;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Point;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.tanks.Direction;

import java.util.HashMap;

public class TankFactory {
    private final HashMap<Direction, PixelMatrix> sprites;
    private final HashMap<Direction, Point> shotPoints;

    public TankFactory(PixelMatrixLoader unitsLoader) {
        PixelMatrix tank = unitsLoader.load("tank", false);

        sprites = new HashMap<>(4);

        sprites.put(Direction.UP, tank);
        sprites.put(Direction.RIGHT, MatrixUtils.getRotated(tank, 90));
        sprites.put(Direction.DOWN, MatrixUtils.getRotated(tank, 180));
        sprites.put(Direction.LEFT, MatrixUtils.getRotated(tank, 270));

        shotPoints = new HashMap<>(4);

        shotPoints.put(Direction.UP, new Point(1, 2));
        shotPoints.put(Direction.RIGHT, new Point(2, 1));
        shotPoints.put(Direction.DOWN, new Point(1, 0));
        shotPoints.put(Direction.LEFT, new Point(0, 1));
    }

    public Tank make(Point point, Direction direction) {
        final Tank tank = new Tank(point, direction);
        tank.sprites = sprites;
        tank.shotPoints = shotPoints;

        return tank;
    }
}
