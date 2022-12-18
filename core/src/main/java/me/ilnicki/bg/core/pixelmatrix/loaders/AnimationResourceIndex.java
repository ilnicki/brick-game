package me.ilnicki.bg.core.pixelmatrix.loaders;

import com.google.gson.Gson;
import me.ilnicki.bg.core.data.resource.ResourceIndex;
import me.ilnicki.bg.core.data.resource.ResourceProvider;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AnimationResourceIndex
    extends ConcurrentHashMap<String, String>
    implements ResourceIndex {
  private static final Gson GSON = new Gson();

  private final String path;
  private final ResourceProvider provider;

  private boolean looped = false;
  private Frame[] frames;

  public AnimationResourceIndex(String path, ResourceProvider provider) {
    this.path = preparePath(path);
    this.provider = provider;
  }

  public void load() {
    InputStream in = provider.getResourceAsStream(this.path + ".json");

    if (in != null) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      Animation raw = GSON.fromJson(
          reader.lines().collect(Collectors.joining()),
          Animation.class
      );
      this.looped = raw.looped;

      String path = this.path.substring(0, this.path.lastIndexOf("/"));

      this.frames = Arrays.stream(raw.frames).map(
          frame -> new Frame(path + "/" + frame.data, frame.length)
      ).toArray(Frame[]::new);

      Arrays.stream(this.frames)
          .forEach(frame -> this.put(frame.data, frame.data));
    }
  }

  public Frame[] getFrames() {
    return frames;
  }

  public boolean isLooped() {
    return looped;
  }

  private String preparePath(String path) {
    return '/' + path.replace('.', '/');
  }

  public static class Animation {
    Frame[] frames;
    boolean looped;
  }

  public static class Frame {
    String data;
    int length;

    public Frame(String data, int length) {
      this.data = data;
      this.length = length;
    }
  }
}
