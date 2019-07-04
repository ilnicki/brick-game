package me.ilnicki.bg.tanks;

import me.ilnicki.bg.core.game.Game;
import me.ilnicki.bg.core.machine.Field;
import me.ilnicki.bg.core.machine.Layer;
import me.ilnicki.bg.core.machine.Machine;
import me.ilnicki.bg.core.machine.keyboard.Keyboard;
import me.ilnicki.bg.core.machine.keyboard.Keyboard.CtrlKey;
import me.ilnicki.bg.core.pixelmatrix.*;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.core.system.container.Args;
import me.ilnicki.bg.core.system.container.Inject;
import me.ilnicki.bg.core.system.processors.GameManager;
import me.ilnicki.bg.tanks.units.Tank;
import me.ilnicki.bg.tanks.units.TankFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static me.ilnicki.bg.tanks.Direction.*;

public class TanksGame implements Game {
    @Inject
    private GameManager gameManager;

    private PixelMatrix field;

    @Inject
    private Machine.Helper helper;

    @Inject
    private Keyboard.CtrlKeyMap keyMap;

    @Inject
    private Machine.Parameters parameters;

    @Inject
    @Args({"internal", "assets.sprites.tanks.units"})
    private PixelMatrixLoader unitsLoader;

    private int blink = 0;
    private final int blinkOn = 5;

    private final int maxWallCount = 15;

    private int weaponCooldown = 0;
    private final int minWeaponCooldown = 9;

    private TankFactory tankFactory;

    private Tank player;
    private final List<Tank> tankList = new ArrayList<>();
    private final List<Wall> wallList = new ArrayList<>();
    private final List<Bullet> bulletList = new ArrayList<>();

    private final int moveSpeed = 4;
    private final Random random = new Random();


    @Inject
    public TanksGame(Field field) {
        field.getLayers().add(new Layer(this.field = new ArrayPixelMatrix(10, 20)));
    }

    @Override
    public void load() {
        tankFactory = new TankFactory(unitsLoader);

        player = tankFactory.make(new Vector(0, 0), UP);
        tankList.add(player);
    }

    @Override
    public void update(long tick) {
        if (!tankList.contains(player)) {
            gameManager.exitGame();
        }

        spawnEnemies();
        handleControls();
        processBullets(tick);

        MatrixUtils.clear(field);
        drawTanks();
        drawBullets();
        drawWalls();
    }


    private void handleControls() {
        if (keyMap.getState(CtrlKey.UP) % moveSpeed == 0) {
            if (player.getDirection() == UP) {
                int newPosY = player.getPosY() + 1;

                if (newPosY + player.getSprite().getHeight() - 1 < field.getHeight()) {
                    player.setPos(player.getPos().withY(newPosY));
                }
            } else {
                player.setDirection(UP);
            }
        } else if (keyMap.getState(CtrlKey.DOWN) % moveSpeed == 0) {
            if (player.getDirection() == DOWN) {
                int newPosY = player.getPosY() - 1;

                if (newPosY >= 0) {
                    player.setPos(player.getPos().withY(newPosY));
                }
            } else {
                player.setDirection(DOWN);
            }
        } else if (keyMap.getState(CtrlKey.LEFT) % moveSpeed == 0) {
            if (player.getDirection() == LEFT) {
                int newPosX = player.getPosX() - 1;

                if (newPosX >= 0) {
                    player.setPos(player.getPos().withX(newPosX));
                }
            } else {
                player.setDirection(LEFT);
            }
        } else if (keyMap.getState(CtrlKey.RIGHT) % moveSpeed == 0) {
            if (player.getDirection() == RIGHT) {
                int newPosX = player.getPosX() + 1;

                if (newPosX + player.getSprite().getWidth() - 1 < field.getWidth()) {
                    player.setPos(player.getPos().withX(newPosX));
                }
            } else {
                player.setDirection(RIGHT);
            }
        }

        if (keyMap.isPressed(CtrlKey.ROTATE)) {
            shoot();
        }

    }

    private void drawTanks() {
        tankList.forEach((tank) ->
        {
            PixelMatrix sprite = tank.getSprite();

            for (int y = 0; y < sprite.getHeight(); y++) {
                for (int x = 0; x < sprite.getWidth(); x++) {
                    try {
                        if (sprite.getPixel(x, y) != null) {
                            field.setPixel(tank.getPos().add(new Vector(x, y)), sprite.getPixel(x, y));
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        });
    }

    private void drawBullets() {
        bulletList.forEach((bullet) -> field.setPixel(bullet.getPos(), Pixel.BLACK));
    }

    private void drawWalls() {
        if (blink % blinkOn != 0) {
            wallList.forEach((mine) -> field.setPixel(mine.getPos(), Pixel.BLACK));
        }

        blink++;
    }

    private void spawnEnemies() {
        if (tankList.size() < 3 && random.nextInt(4) == 0) {
            int pos = random.nextInt(6);

            switch (pos) {
                case 0:
                    tankList.add(tankFactory.make(new Vector(0, 17), RIGHT));
                    break;
                case 1:
                    tankList.add(tankFactory.make(new Vector(7, 17), DOWN));
                    break;
                case 2:
                    tankList.add(tankFactory.make(new Vector(7, 10), LEFT));
                    break;
                case 3:
                    tankList.add(tankFactory.make(new Vector(7, 0), LEFT));
                    break;
                case 4:
                    tankList.add(tankFactory.make(new Vector(0, 0), UP));
                    break;
                case 5:
                    tankList.add(tankFactory.make(new Vector(0, 10), RIGHT));
                    break;
            }
        }
    }

    private void shoot() {
        if (weaponCooldown > 0) {
            bulletList.add(player.shoot());
            weaponCooldown = -minWeaponCooldown;
        }
    }

    private void processBullets(long tick) {
        for (Iterator<Bullet> i = bulletList.iterator(); i.hasNext(); ) {
            Bullet bullet = i.next();

            if (tick % bullet.getSpeed() == 0) {
                bullet.setPos(bullet.getPos().add((Vector) bullet.getDirection().getVector()));
            }

            if (wallList.removeIf(wall -> wall.getPos().equals(bullet.getPos()))) {
                i.remove();
            }

            if (tankList.removeIf(tank -> tank.isCollide(bullet.getPos()) && tank != bullet.getOwner())) {
                parameters.score.inc();
                i.remove();
            }

            if (bullet.getPosY() < 0
                    || bullet.getPosX() < 0
                    || bullet.getPosY() >= field.getHeight()
                    || bullet.getPosX() >= field.getWidth()) {
                i.remove();
            }
        }
        weaponCooldown++;
    }

}
