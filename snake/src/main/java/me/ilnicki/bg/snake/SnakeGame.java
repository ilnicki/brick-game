package me.ilnicki.bg.snake;

import me.ilnicki.bg.core.game.Game;
import me.ilnicki.bg.core.machine.Field;
import me.ilnicki.bg.core.machine.Layer;
import me.ilnicki.bg.core.machine.Machine;
import me.ilnicki.bg.core.machine.keyboard.Keyboard;
import me.ilnicki.bg.core.machine.keyboard.Keyboard.CtrlKey;
import me.ilnicki.bg.core.pixelmatrix.ArrayPixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.MatrixUtils;
import me.ilnicki.bg.core.pixelmatrix.Pixel;
import me.ilnicki.bg.core.pixelmatrix.PixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.loaders.PixelMatrixLoader;
import me.ilnicki.bg.core.system.container.Args;
import me.ilnicki.bg.core.system.container.Inject;
import me.ilnicki.bg.core.system.processors.GameArgument;
import me.ilnicki.bg.core.system.processors.GameManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static me.ilnicki.bg.snake.SnakeHead.Direction.*;

public class SnakeGame implements Game {
    private static final byte keyHandleFreq = 4;

    @Inject
    private Machine.Helper helper;

    private PixelMatrix field;

    @Inject
    private GameArgument argument;

    @Inject
    private Keyboard.CtrlKeyMap keyMap;

    @Inject
    private Machine.Parameters params;

    @Inject
    private GameManager gameManager;

    @Inject
    @Args({"internal", "assets.sprites.levels"})
    private PixelMatrixLoader levelLoader;

    private GameMode gameMode;

    private ArrayList<Entity> entities;
    private SnakeHead snake;
    private int snakeLength;
    private byte livesCount = 4;

    private boolean isGameStarted;

    @Inject
    public SnakeGame(Field field) {
        field.getLayers().add(new Layer(this.field = new ArrayPixelMatrix(10, 20)));
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
    }

    @Override
    public void update(long tick) {
        if (livesCount > 0) {
            if (isGameStarted || checkGameStarted()) {
                processMove(tick);
            }

            drawEntities();
            drawLivesCount();
        } else {
            gameManager.exitGame();
        }
    }

    private void processMove(long tick) {
        if (keyMap.getState(CtrlKey.UP) % keyHandleFreq == 0) {
            moveSnake(DIR_UP);
        } else if (keyMap.getState(CtrlKey.DOWN) % keyHandleFreq == 0) {
            moveSnake(DIR_DOWN);
        } else if (keyMap.getState(CtrlKey.LEFT) % keyHandleFreq == 0) {
            moveSnake(DIR_LEFT);
        } else if (keyMap.getState(CtrlKey.RIGHT) % keyHandleFreq == 0) {
            moveSnake(DIR_RIGHT);
        } else if (keyMap.getState(CtrlKey.ROTATE) % keyHandleFreq == 0) {
            moveSnake(snake.getDirection());
        } else if (tick % getSnakeSpeed() == 0) {
            moveSnake(snake.getDirection());
        }
    }

    private void moveSnake(SnakeHead.Direction direction) {
        int x = snake.getPosX();
        int y = snake.getPosY();

        switch (direction) {
            case DIR_UP:
                y++;
                break;
            case DIR_RIGHT:
                x++;
                break;
            case DIR_DOWN:
                y--;
                break;
            case DIR_LEFT:
                x--;
                break;
        }

        processHeadPosition(x, y, direction);
    }

    private void processHeadPosition(int x, int y, SnakeHead.Direction direction) {
        switch (gameMode) {
            case CLASSIC:
                if (x < 0 || x >= field.getWidth()
                        || y < 0 || y >= field.getHeight()) {
                    die();
                } else {
                    snake.setPos(x, y);
                    snake.setDirection(direction);
                }
                break;
            case PORTAL_WALLS:
                if (x < 0) {
                    x = field.getWidth() - 1;
                } else if (x >= field.getWidth()) {
                    x = 0;
                } else if (y < 0) {
                    y = field.getHeight() - 1;
                } else if (y >= field.getHeight()) {
                    y = 0;
                }

                snake.setPos(x, y);
                snake.setDirection(direction);
                break;
        }

        Entity entity = getHeadCollision();

        if (entity != null) {
            if (entity instanceof Food) {
                consumeFood((Food) entity);

                params.score.set(params.score.get() + snakeLength - 3);

                if (params.score.get() > params.level.get() * params.speed.get() * 100) {
                    params.level.inc();
                }

                if (params.score.get() > params.level.get() * 1000) {
                    params.speed.inc();
                }

                generateFood();
            } else if (entity instanceof SnakePart || entity instanceof Wall) {
                die();
            }
        }
    }

    private boolean checkGameStarted() {
        return isGameStarted = Arrays.stream(CtrlKey.values()).anyMatch(key -> keyMap.isPressed(key));
    }

    private void drawEntities() {
        MatrixUtils.clear(field);

        entities.forEach((entity) ->
        {
            PixelMatrix sprite = entity.getSprite();

            for (int i = 0; i < sprite.getWidth(); i++) {
                for (int j = 0; j < sprite.getHeight(); j++) {
                    try {
                        field.setPixel(
                                entity.getPosX() + i,
                                entity.getPosY() + j,
                                sprite.getPixel(i, j)
                        );
                    } catch (Exception ignored) {
                    }
                }
            }
        });
    }

    private void drawLivesCount() {
        MatrixUtils.clear(helper);
        int j = helper.getHeight() - 1;

        for (int i = 0; i < livesCount; i++) {
            try {
                helper.setPixel(i, j, Pixel.BLACK);
            } catch (ArrayIndexOutOfBoundsException e) {
                j--;

                try {
                    helper.setPixel(i, j, Pixel.BLACK);
                } catch (Exception ignored) {
                }
            }
        }
    }

    private void generateFood() {
        boolean isEmptyPlace;

        do {
            isEmptyPlace = true;

            Random rnd = new Random();
            int foodX = rnd.nextInt(field.getWidth());
            int foodY = rnd.nextInt(field.getHeight());

            for (Entity entity : entities) {
                if (foodX == entity.getPosX() || foodY == entity.getPosY()) {
                    isEmptyPlace = false;
                }
            }

            if (isEmptyPlace) {
                Food food = new Food(foodX, foodY);
                entities.add(food);

            }
        } while (!isEmptyPlace);
    }

    public void consumeFood(Food food) {
        entities.remove(food);
        SnakePart tail = snake.getChildPart();

        SnakePart newPart = new SnakePart(food.getPosX(),
                food.getPosY(), snake);
        entities.add(newPart);

        moveSnake(snake.getDirection());

        newPart.setChildPart(tail);

        snakeLength++;
    }

    private Entity getHeadCollision() {
        for (Entity entity : entities) {
            if (entity.getPosX() == snake.getPosX()
                    && entity.getPosY() == snake.getPosY()
                    && entity != snake) {
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
        entities = new ArrayList<>();
        isGameStarted = false;

        snake = new SnakeHead(3, 0, SnakeHead.Direction.DIR_UP);
        entities.add(snake);

        SnakePart part1 = new SnakePart(4, 0, snake);
        entities.add(part1);

        SnakePart part2 = new SnakePart(5, 0, part1);
        entities.add(part2);

        snakeLength = 3;

        loadWalls();
        generateFood();
    }

    private void loadWalls() {
        try {
            PixelMatrix walls = levelLoader.load(gameMode.getLevelPrefix() + params.level, false);

            if (walls != null) {
                for (int i = 0; i < walls.getWidth(); i++) {
                    for (int j = 0; j < walls.getHeight(); j++) {
                        try {
                            if (walls.getPixel(i, j) == Pixel.BLACK) {
                                entities.add(new Wall(i, j));
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }

    }

    private int getSnakeSpeed() {
        return 16 - params.speed.get();
    }

    @Override
    public void save() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void recover() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private enum GameMode {

        CLASSIC, PORTAL_WALLS;

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
