package me.ilnicki.bg.tanks;

import static me.ilnicki.bg.tanks.Direction.DOWN;
import static me.ilnicki.bg.tanks.Direction.LEFT;
import static me.ilnicki.bg.tanks.Direction.RIGHT;
import static me.ilnicki.bg.tanks.Direction.UP;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import me.ilnicki.bg.core.game.AbstractGame;
import me.ilnicki.bg.core.math.Vector;
import me.ilnicki.bg.core.pixelmatrix.ArrayPixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Matrices;
import me.ilnicki.bg.core.pixelmatrix.MutablePixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.layering.Layer;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.core.state.Field;
import me.ilnicki.bg.core.state.Helper;
import me.ilnicki.bg.core.state.buttons.ButtonsState;
import me.ilnicki.bg.core.state.buttons.GameButton;
import me.ilnicki.bg.core.state.parameters.IntParameter;
import me.ilnicki.bg.core.system.processors.gamemanager.GameManager;
import me.ilnicki.bg.tanks.units.Bullet;
import me.ilnicki.bg.tanks.units.Tank;
import me.ilnicki.bg.tanks.units.TankFactory;
import me.ilnicki.bg.tanks.units.Wall;
import me.ilnicki.container.Inject;

public class TanksGame extends AbstractGame {
  @Inject
  private GameManager gameManager;

  private final MutablePixelMatrix field;

  @Inject
  private Helper helper;

  @Inject
  private ButtonsState<GameButton> buttons;

  @Inject("score")
  private IntParameter score;

  @Inject({"internal", "assets.sprites.tanks.units"})
  private PixelMatrixLoader unitsLoader;

  private int blink = 0;
  private final int blinkOn = 5;

  private final int maxWallCount = 15;

  private int weaponCooldown = 0;
  private final int minWeaponCooldown = 9;

  private TankFactory tankFactory;

  private Tank player;

  private final List<Tank> tanks = new ArrayList<>();
  private final List<Wall> walls = new ArrayList<>();
  private final List<Bullet> bullets = new ArrayList<>();

  private final int moveSpeed = 4;
  private final Random random = new Random();

  private long tick = 0;

  @Inject
  public TanksGame(Field field) {
    field.getLayers().add(new Layer<>(this.field = new ArrayPixelMatrix(10, 20)));
  }

  @Override
  public void load() {
    tankFactory = new TankFactory(unitsLoader);

    player = tankFactory.make(new Vector(0, 0), UP);
    tanks.add(player);

    super.load();
  }

  @Override
  public void update(int delta) {
    if (!tanks.contains(player)) {
      quit();
    }

    spawnEnemies();
    handleControls();
    processBullets(tick);

    Matrices.clear(field);
    drawTanks();
    drawBullets();
    drawWalls();

    tick++;
  }

  private void handleControls() {
    Vector pos = player.getPos();

    if (buttons.getValue(GameButton.UP) % moveSpeed == 0) {
      if (player.getDirection() == UP) {
        int newPosY = pos.getY() + 1;

        if (newPosY + player.getSprite().getHeight() - 1 < field.getHeight()) {
          player.setPos(pos.withY(newPosY));
        }
      } else {
        player.setDirection(UP);
      }
    } else if (buttons.getValue(GameButton.DOWN) % moveSpeed == 0) {
      if (player.getDirection() == DOWN) {
        int newPosY = pos.getY() - 1;

        if (newPosY >= 0) {
          player.setPos(pos.withY(newPosY));
        }
      } else {
        player.setDirection(DOWN);
      }
    } else if (buttons.getValue(GameButton.LEFT) % moveSpeed == 0) {
      if (player.getDirection() == LEFT) {
        int newPosX = pos.getX() - 1;

        if (newPosX >= 0) {
          player.setPos(pos.withX(newPosX));
        }
      } else {
        player.setDirection(LEFT);
      }
    } else if (buttons.getValue(GameButton.RIGHT) % moveSpeed == 0) {
      if (player.getDirection() == RIGHT) {
        int newPosX = pos.getX() + 1;

        if (newPosX + player.getSprite().getWidth() - 1 < field.getWidth()) {
          player.setPos(pos.withX(newPosX));
        }
      } else {
        player.setDirection(RIGHT);
      }
    }

    if (buttons.isPressed(GameButton.ROTATE)) {
      shoot();
    }
  }

  private void drawTanks() {
    tanks.forEach(
        (tank) -> {
          PixelMatrix sprite = tank.getSprite();

          for (int y = 0; y < sprite.getHeight(); y++) {
            for (int x = 0; x < sprite.getWidth(); x++) {
              final Vector pos = new Vector(x, y);
              try {
                if (sprite.getPixel(pos) != null) {
                  field.setPixel(tank.getPos().add(pos), sprite.getPixel(pos));
                }
              } catch (Exception ignored) {
              }
            }
          }
        });
  }

  private void drawBullets() {
    bullets.forEach((bullet) -> field.setPixel(bullet.getPos(), Pixel.BLACK));
  }

  private void drawWalls() {
    if (blink % blinkOn != 0) {
      walls.forEach((mine) -> field.setPixel(mine.getPos(), Pixel.BLACK));
    }

    blink++;
  }

  private void spawnEnemies() {
    if (tanks.size() < 3 && random.nextInt(4) == 0) {
      int pos = random.nextInt(6);

      switch (pos) {
        case 0:
          tanks.add(tankFactory.make(new Vector(0, 17), RIGHT));
          break;
        case 1:
          tanks.add(tankFactory.make(new Vector(7, 17), DOWN));
          break;
        case 2:
          tanks.add(tankFactory.make(new Vector(7, 10), LEFT));
          break;
        case 3:
          tanks.add(tankFactory.make(new Vector(7, 0), LEFT));
          break;
        case 4:
          tanks.add(tankFactory.make(new Vector(0, 0), UP));
          break;
        case 5:
          tanks.add(tankFactory.make(new Vector(0, 10), RIGHT));
          break;
      }
    }
  }

  private void shoot() {
    if (weaponCooldown > 0) {
      bullets.add(player.shoot());
      weaponCooldown = -minWeaponCooldown;
    }
  }

  private void processBullets(long tick) {
    for (Iterator<Bullet> i = bullets.iterator(); i.hasNext(); ) {
      Bullet bullet = i.next();

      if (tick % bullet.getSpeed() == 0) {
        bullet.setPos(bullet.getPos().add(bullet.getDirection().getVector()));
      }

      if (walls.removeIf(wall -> wall.getPos().equals(bullet.getPos()))) {
        i.remove();
      }

      if (tanks.removeIf(tank -> tank.isCollide(bullet.getPos()) && tank != bullet.getOwner())) {
        score.inc();
        i.remove();
      }

      Vector pos = bullet.getPos();

      if (pos.getY() < 0
          || pos.getX() < 0
          || pos.getY() >= field.getHeight()
          || pos.getX() >= field.getWidth()) {
        i.remove();
      }
    }
    weaponCooldown++;
  }
}
