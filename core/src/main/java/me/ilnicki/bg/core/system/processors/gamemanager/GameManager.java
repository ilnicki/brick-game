package me.ilnicki.bg.core.system.processors.gamemanager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import me.ilnicki.bg.core.game.Game;
import me.ilnicki.bg.core.game.GamesConfig;
import me.ilnicki.bg.core.game.Manifest;
import me.ilnicki.bg.core.state.GameState;
import me.ilnicki.bg.core.state.State;
import me.ilnicki.bg.core.system.CoreModule;
import me.ilnicki.bg.core.system.Kernel;
import me.ilnicki.bg.core.system.Module;
import me.ilnicki.bg.core.system.SystemConfig;
import me.ilnicki.bg.core.util.Safe;
import me.ilnicki.container.Container;
import me.ilnicki.container.Inject;
import me.ilnicki.container.Type;
import me.ilnicki.eventloop.EventLoop;

public class GameManager implements CoreModule {
  @Inject
  private State state;

  @Inject
  private Kernel kernel;
  @Inject
  private Container container;

  private List<Manifest> manifests;

  @Inject
  private SystemConfig systemConfig;
  @Inject
  private GamesConfig gamesConfig;

  @Inject
  @Type(StateContainer.class)
  private Container stateContainer;

  private final EventLoop loop = new EventLoop();
  private final Stack<GameProcess> gamesStack = new Stack<>();

  @Override
  public void load() {
    manifests =
        Arrays.stream(gamesConfig.getGameManifests())
            .map(this::loadGameManifest)
            .collect(Collectors.toList());

    GameArgument arg = new GameArgument();
    container.share(arg);
    stateContainer.share(arg);

    launchGame(
        () -> {
          try {
            return new LauncherWrapper(
                container.get(
                    Class.forName(gamesConfig.getLauncher())
                        .asSubclass(Module.class)
                )
            );
          } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
          }
        });
  }

  @Override
  public void update(int delta) {
    loop.execute();
    processStack(delta);
  }

  @Override
  public void stop() {
    while (!gamesStack.empty()) {
      gamesStack.pop().getGame().stop();
    }
  }

  public List<Manifest> getManifests() {
    return manifests;
  }

  public Future<GameState> launchGame(Class<? extends Game> gameClass) {
    return launchGame(() -> stateContainer.get(gameClass));
  }

  public Future<GameState> launchGame(Supplier<Game> creator) {
    FutureTask<GameState> futureState =
        new FutureTask<>(
            () -> {
              GameState gameState = new GameState();
              state.setGameState(gameState);

              gamesStack.push(new GameProcess(creator.get(), gameState));

              return gameState;
            });

    loop.add(futureState);
    return futureState;
  }

  private void processStack(int delta) {
    Optional<GameProcess> process = Safe.of(gamesStack::peek);

    if (process.isPresent()) {
      Game game = process.get().getGame();
      Game.Status status = game.getStatus();

      if (status == Game.Status.RUNNING) {
        game.update(delta);
      } else if (status == Game.Status.LOADING) {
        game.load();
      } else if (status == Game.Status.FINISHING) {
        gamesStack.pop().getGame().stop();
        state.setGameState(gamesStack.peek().getGameState());
      }
    }
  }

  private Manifest loadGameManifest(String manifestName) {
    try {
      return container.get(Class.forName(manifestName).asSubclass(Manifest.class));
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
