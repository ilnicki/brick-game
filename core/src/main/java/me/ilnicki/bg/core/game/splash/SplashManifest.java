package me.ilnicki.bg.core.game.splash;

import me.ilnicki.bg.core.game.Game;
import me.ilnicki.bg.core.game.Manifest;

public class SplashManifest implements Manifest {
    @Override
    public Class<? extends Game> getGameClass() {
        return Splash.class;
    }
}
