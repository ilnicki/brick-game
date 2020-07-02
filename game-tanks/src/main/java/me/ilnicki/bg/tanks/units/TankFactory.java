package me.ilnicki.bg.tanks.units;

import me.ilnicki.bg.core.pixelmatrix.Matrices;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.math.Vector;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.tanks.Direction;

import java.util.HashMap;

public class TankFactory {
    private final HashMap<Direction, PixelMatrix> sprites;
    private final HashMap<Direction, Vector> shotPoints;

    public TankFactory(PixelMatrixLoader unitsLoader) {
        PixelMatrix tank = unitsLoader.get("tank");

        sprites = new HashMap<>(4);

        sprites.put(Direction.UP, tank);
        sprites.put(Direction.RIGHT, Matrices.rotate(tank, 90));
        sprites.put(Direction.DOWN, Matrices.rotate(tank, 180));
        sprites.put(Direction.LEFT, Matrices.rotate(tank, 270));

        shotPoints = new HashMap<>(4);

        shotPoints.put(Direction.UP, new Vector(1, 2));
        shotPoints.put(Direction.RIGHT, new Vector(2, 1));
        shotPoints.put(Direction.DOWN, new Vector(1, 0));
        shotPoints.put(Direction.LEFT, new Vector(0, 1));
    }

    public Tank make(Vector point, Direction direction) {
        final Tank tank = new Tank(point, direction);
        tank.sprites = sprites;
        tank.shotPoints = shotPoints;

        return tank;
    }
}
