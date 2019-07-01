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

    @Inject
    public TanksGame(Field field) {
        field.getLayers().add(new Layer(this.field = new ArrayPixelMatrix(10, 20)));
    }

    @Override
    public void load() {
        tankFactory = new TankFactory(unitsLoader);

        player = tankFactory.make(new Point(0, 0), UP);
        tankList.add(player);
    }

    @Override
    public void update(long tick) {
        if (!this.tankList.contains(this.player)) {
            this.gameManager.exitGame();
        }

        spawnEnemies();
        handleControlls();
        processBullets(tick);

        MatrixUtils.clear(this.field);
        drawTanks();
        drawBullets();
        drawWalls();
    }


    private void handleControlls() {
        if (keyMap.getState(CtrlKey.UP) % moveSpeed == 0) {
            if (player.getDirection() == UP) {
                int newPosY = player.getPosY() + 1;

                if (newPosY + player.getSprite().getHeight() - 1 < this.field.getHeight())
                    player.setPosY(newPosY);
            } else {
                player.setDirection(UP);
            }
        } else if (keyMap.getState(CtrlKey.DOWN) % moveSpeed == 0) {
            if (player.getDirection() == DOWN) {
                int newPosY = player.getPosY() - 1;

                if (newPosY >= 0)
                    player.setPosY(newPosY);
            } else {
                player.setDirection(DOWN);
            }
        } else if (keyMap.getState(CtrlKey.LEFT) % moveSpeed == 0) {
            if (player.getDirection() == LEFT) {
                int newPosX = player.getPosX() - 1;

                if (newPosX >= 0)
                    player.setPosX(newPosX);
            } else {
                player.setDirection(LEFT);
            }
        } else if (keyMap.getState(CtrlKey.RIGHT) % moveSpeed == 0) {
            if (player.getDirection() == RIGHT) {
                int newPosX = player.getPosX() + 1;

                if (newPosX + player.getSprite().getWidth() - 1 < this.field.getWidth())
                    player.setPosX(newPosX);
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

            for (int i = 0; i < sprite.getHeight(); i++) {

                for (int j = 0; j < sprite.getWidth(); j++) {
                    try {
                        if (sprite.getPixel(j, i) != null)
                            this.field.setPixel(tank.getPosX() + j, tank.getPosY() + i, sprite.getPixel(j, i));
                    } catch (Exception e) {

                    }
                }
            }
        });
    }

    private void drawBullets() {
        bulletList.forEach((bullet) ->
                this.field.setPixel(bullet.getPosX(), bullet.getPosY(), Pixel.BLACK));
    }

    private void drawWalls() {
        if (!(blink % blinkOn == 0)) {
            wallList.forEach((mine) ->
                    this.field.setPixel(mine.getPosX(), mine.getPosY(), Pixel.BLACK));
        }

        blink++;
    }

    private void spawnEnemies() {
        Random rnd = new Random();
        if (this.tankList.size() < 3
                && rnd.nextInt(4) == 0) {
            int pos = rnd.nextInt(6);
            Tank tank = null;

            switch (pos) {
                case 0:
                    tank = tankFactory.make(new Point(0, 17), RIGHT);
                    break;
                case 1:
                    tank = tankFactory.make(new Point(7, 17), DOWN);
                    break;
                case 2:
                    tank = tankFactory.make(new Point(7, 10), LEFT);
                    break;
                case 3:
                    tank = tankFactory.make(new Point(7, 0), LEFT);
                    break;
                case 4:
                    tank = tankFactory.make(new Point(0, 0), UP);
                    break;
                case 5:
                    tank = tankFactory.make(new Point(0, 10), RIGHT);
                    break;
            }

            if (tank != null)
                this.tankList.add(tank);
        }
    }

    private void shoot() {
        if (weaponCooldown > 0) {
            final Bullet bullet = player.shoot();
            bulletList.add(bullet);

            weaponCooldown = -minWeaponCooldown;
        }
    }

    private void processBullets(long tick) {
        for (Iterator<Bullet> i = bulletList.iterator(); i.hasNext(); ) {
            Bullet bullet = i.next();

            if (tick % bullet.getSpeed() == 0) {
                switch (bullet.getDirection()) {
                    case UP:
                        bullet.setPosY(bullet.getPosY() + 1);
                        break;
                    case DOWN:
                        bullet.setPosY(bullet.getPosY() - 1);
                        break;
                    case RIGHT:
                        bullet.setPosX(bullet.getPosX() + 1);
                        break;
                    case LEFT:
                        bullet.setPosX(bullet.getPosX() - 1);
                        break;
                }
            }

            if (this.wallList.removeIf(wall -> (wall.getPosY() == bullet.getPosY()
                    && wall.getPosX() == bullet.getPosX()))) {
                i.remove();
            }

            if (this.tankList.removeIf(tank -> tank.isCollide(bullet.getPosX(), bullet.getPosY())
                    && tank != bullet.getOwner())) {
                this.parameters.score.inc();
                i.remove();
            }

            if (bullet.getPosY() < 0 || bullet.getPosX() < 0
                    || bullet.getPosY() >= this.field.getHeight()
                    || bullet.getPosX() >= this.field.getWidth()) {
                i.remove();
            }
        }
        weaponCooldown++;
    }

}
