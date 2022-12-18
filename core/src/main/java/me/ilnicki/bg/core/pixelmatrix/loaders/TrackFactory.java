package me.ilnicki.bg.core.pixelmatrix.loaders;

import me.ilnicki.bg.core.data.resource.ClassResourceProvider;
import me.ilnicki.bg.core.data.resource.ResourceProvider;
import me.ilnicki.bg.core.pixelmatrix.ArrayPixelMatrix;
import me.ilnicki.bg.core.pixelmatrix.animation.Animation;
import me.ilnicki.bg.core.pixelmatrix.animation.Frame;
import me.ilnicki.bg.core.pixelmatrix.animation.Track;
import me.ilnicki.container.ProvisionException;
import me.ilnicki.container.provider.Factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TrackFactory implements Factory<Track> {
  private final ResourceProvider resourceProvider = new ClassResourceProvider();

  @Override
  public Track produce(String... args) throws ProvisionException {
    AnimationResourceIndex ari = new AnimationResourceIndex(
        args[0],
        resourceProvider
    );
    ari.load();

    final ImagePixelMatrixLoader loader = new ImagePixelMatrixLoader(
        ari,
        resourceProvider
    );

    final List<Frame> frames = Arrays.stream(ari.getFrames()).map(frame -> new Frame(
        loader.get(frame.data),
        frame.length
    )).collect(Collectors.toList());

    return new Track(
        frames,
        ari.isLooped()
    );
  }
}
