package me.ilnicki.bg.core.pixelmatrix.loaders;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.ilnicki.bg.core.data.resource.ResourceIndex;
import me.ilnicki.bg.core.data.resource.ResourceProvider;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ImagesResourceIndex
    extends ConcurrentHashMap<String, String>
    implements ResourceIndex {
  private static final Type TYPE = (new TypeToken<Map<String, String>>() {
  }).getType();
  private static final Gson GSON = new Gson();

  private final String path;
  private final ResourceProvider provider;

  public ImagesResourceIndex(String path, ResourceProvider provider) {
    this.path = preparePath(path);
    this.provider = provider;
  }

  public void load() {
    InputStream in = provider.getResourceAsStream(this.path + "/index.json");

    if (in != null) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      Map<String, String> raw = GSON.fromJson(reader.lines().collect(Collectors.joining()), TYPE);
      raw.forEach((key, value) -> this.put(key, this.path + "/" + value));
    }
  }

  private String preparePath(String path) {
    return '/' + path.replace('.', '/');
  }
}
