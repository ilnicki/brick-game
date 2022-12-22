package me.ilnicki.bg.snake;

import static me.ilnicki.bg.snake.SnakeHead.Direction.DIR_DOWN;
import static me.ilnicki.bg.snake.SnakeHead.Direction.DIR_LEFT;
import static me.ilnicki.bg.snake.SnakeHead.Direction.DIR_RIGHT;
import static me.ilnicki.bg.snake.SnakeHead.Direction.DIR_UP;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

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
import me.ilnicki.bg.core.system.processors.gamemanager.GameArgument;
import me.ilnicki.bg.core.system.processors.gamemanager.GameManager;
import me.ilnicki.bg.core.system.processors.gamemanager.StateContainer;
import me.ilnicki.container.Container;
import me.ilnicki.container.Inject;
import me.ilnicki.container.Type;

public class SnakeGame extends AbstractGame {
  private static final byte buttonHandleFreq = 4;

  @Inject
  private Random rnd;

  @Inject
  private Container container;

  @Inject({"assets.sprites.snake.levels"})
  private PixelMatrixLoader levelLoader;

  @Inject
  private GameManager gameManager;

  private final MutablePixelMatrix helper;
  private final MutablePixelMatrix field;

  @Inject
  private GameArgument argument;

  @Inject
  private ButtonsState<GameButton> buttons;

  @Inject("score")
  private IntParameter score;

  @Inject("level")
  private IntParameter level;

  @Inject("speed")
  private IntParameter speed;

  private GameMode gameMode;

  private Set<Entity> entities;
  private SnakeHead snake;
  private byte livesCount = 4;

  private long tick = 0;

  private boolean isGameStarted;

  @Inject
  public SnakeGame(Field field, Helper helper) {
    field.getLayers().add(new Layer<>(
        this.field = new ArrayPixelMatrix(10, 20))
    );
    helper.getLayers().add(new Layer<>(
        this.helper = new ArrayPixelMatrix(4, 4))
    );
  }

  @Override
  public void load() {
    GameMode[] gameModes = GameMode.values();
    int arg = argument.get();

    if (arg <= gameModes.length) {
      gameMode = gameModes[arg - 1];
    } else {
      gameMode = gameModes[0];
    }

    initGame();

    super.load();
  }

  @Override
  public void update(int delta) {
    if (livesCount > 0) {
      if (isGameStarted || checkGameStarted()) {
        processMove(tick);
      }

      drawEntities();
      drawLivesCount();
    } else {
      quit();
    }

    tick++;
  }

  private void processMove(long tick) {
    if (buttons.getValue(GameButton.UP) % buttonHandleFreq == 0) {
      moveSnake(DIR_UP);
    } else if (buttons.getValue(GameButton.DOWN) % buttonHandleFreq == 0) {
      moveSnake(DIR_DOWN);
    } else if (buttons.getValue(GameButton.LEFT) % buttonHandleFreq == 0) {
      moveSnake(DIR_LEFT);
    } else if (buttons.getValue(GameButton.RIGHT) % buttonHandleFreq == 0) {
      moveSnake(DIR_RIGHT);
    } else if (buttons.getValue(GameButton.ROTATE) % buttonHandleFreq == 0) {
      moveSnake(snake.getDirection());
    } else if (tick % getSnakeSpeed() == 0) {
      moveSnake(snake.getDirection());
    }
  }

  private void moveSnake(SnakeHead.Direction direction) {
    Vector pos = snake.getPos();
    processHeadPosition(pos.add(direction.getVector()), direction);
  }

  private void processHeadPosition(Vector pos, SnakeHead.Direction direction) {
    switch (gameMode) {
      case CLASSIC:
        if (pos.getX() < 0
            || pos.getX() >= field.getWidth()
            || pos.getY() < 0
            || pos.getY() >= field.getHeight()) {
          die();
        } else {
          snake.setPos(pos);
          snake.setDirection(direction);
        }
        break;
      case PORTAL_WALLS:
        if (pos.getX() < 0) {
          pos = pos.withX(field.getWidth() - 1);
        } else if (pos.getX() >= field.getWidth()) {
          pos = pos.withX(0);
        } else if (pos.getY() < 0) {
          pos = pos.withY(field.getHeight() - 1);
        } else if (pos.getY() >= field.getHeight()) {
          pos = pos.withY(0);
        }

        snake.setPos(pos);
        snake.setDirection(direction);
        break;
    }

    Entity entity = getHeadCollision();

    if (entity != null) {
      if (entity instanceof Food) {
        consumeFood((Food) entity);

        score.set(score.get() + snake.size() - 3);

        if (score.get() > level.get() * speed.get() * 100) {
          level.inc();
        }

        if (score.get() > level.get() * 1000) {
          speed.inc();
        }

        generateFood();
      } else if (entity instanceof SnakePart || entity instanceof Wall) {
        die();
      }
    }
  }

  private boolean checkGameStarted() {
    return isGameStarted = Arrays.stream(GameButton.values())
        .anyMatch(key -> buttons.isPressed(key));
  }

  private void drawEntities() {
    Matrices.clear(field);

    entities.forEach(
        (entity) -> {
          PixelMatrix sprite = entity.getSprite();

          for (int y = 0; y < sprite.getHeight(); y++) {
            for (int x = 0; x < sprite.getWidth(); x++) {
              final Vector pos = new Vector(x, y);
              Vector newPos = entity.getPos().sub(pos);
              try {
                field.setPixel(newPos, sprite.getPixel(pos));
              } catch (Exception ignored) {
              }
            }
          }
        });
  }

  private void drawLivesCount() {
    Matrices.clear(helper);
    int y = helper.getHeight() - 1;

    for (int x = 0; x < livesCount; x++) {
      Vector point = new Vector(x, y);
      try {
        helper.setPixel(point, Pixel.BLACK);
      } catch (ArrayIndexOutOfBoundsException e) {
        y--;

        try {
          helper.setPixel(point, Pixel.BLACK);
        } catch (Exception ignored) {
        }
      }
    }
  }

  private void generateFood() {
    boolean isEmptyPlace = true;

    do {
      Vector foodPos = new Vector(
          rnd.nextInt(field.getWidth()),
          rnd.nextInt(field.getHeight())
      );

      for (Entity entity : entities) {
        if (foodPos.equals(entity.getPos())) {
          isEmptyPlace = false;
          break;
        }
      }

      if (isEmptyPlace) {
        Food food = new Food(foodPos);
        container.injectTo(food);
        entities.add(food);
      }
    } while (!isEmptyPlace);
  }

  private void consumeFood(Food food) {
    entities.remove(food);
    SnakePart tail = snake.tail();

    SnakePart newPart = new SnakePart(food.getPos());
    tail.append(newPart);

    entities.add(newPart);

    moveSnake(snake.getDirection());
  }

  private Entity getHeadCollision() {
    for (Entity entity : entities) {
      if (entity.getPos().equals(snake.getPos()) && entity != snake) {
        return entity;
      }
    }

    return null;
  }

  private void die() {
    livesCount--;
    initGame();
  }

  private void initGame() {
    entities = new LinkedHashSet<>();
    isGameStarted = false;

    snake = new SnakeHead(new Vector(3, 0), SnakeHead.Direction.DIR_UP);
    container.injectTo(snake);
    entities.add(snake);

    SnakePart part1 = new SnakePart(new Vector(4, 0));
    snake.append(part1);
    entities.add(part1);

    SnakePart part2 = new SnakePart(new Vector(5, 0));
    part1.append(part2);
    entities.add(part2);

    loadWalls();
    generateFood();
  }

  private void loadWalls() {
    try {
      PixelMatrix walls = levelLoader.get(gameMode.getLevelPrefix() + level);

      if (walls != null) {
        for (int y = 0; y < walls.getHeight(); y++) {
          for (int x = 0; x < walls.getWidth(); x++) {
            Vector pos = new Vector(x, y);
            try {
              if (walls.getPixel(pos) == Pixel.BLACK) {
                entities.add(new Wall(pos));
              }
            } catch (Exception ignored) {
            }
          }
        }
      }
    } catch (Exception ignored) {
    }
  }

  private int getSnakeSpeed() {
    return 16 - speed.get();
  }

  private enum GameMode {
    CLASSIC,
    PORTAL_WALLS;

    private final String levelPrefix;

    GameMode() {
      levelPrefix = toString();
    }

    GameMode(String levelPrefix) {
      this.levelPrefix = levelPrefix;
    }

    public String getLevelPrefix() {
      return levelPrefix;
    }
  }
}
