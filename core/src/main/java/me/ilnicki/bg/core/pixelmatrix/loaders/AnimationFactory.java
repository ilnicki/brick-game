package me.ilnicki.bg.core.pixelmatrix.loaders;

import me.ilnicki.bg.core.pixelmatrix.animation.Animation;
import me.ilnicki.bg.core.pixelmatrix.animation.TrackAnimation;
import me.ilnicki.container.Inject;
import me.ilnicki.container.ProvisionException;
import me.ilnicki.container.provider.Factory;

public class AnimationFactory implements Factory<Animation> {
  private final TrackFactory trackFactory;

  @Inject
  public AnimationFactory(TrackFactory trackFactory) {
    this.trackFactory = trackFactory;
  }

  @Override
  public Animation produce(String... args) throws ProvisionException {
    return new TrackAnimation(trackFactory.produce(args));
  }
}
