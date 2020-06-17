package me.ilnicki.bg.core.data.resource;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ResourceIndex extends ConcurrentHashMap<String, String> {
    private final static Type TYPE = (new TypeToken<Map<String, String>>(){}).getType();
    private final static Gson GSON = new Gson();

    private final String path;
    private final ResourceProvider provider;

    public ResourceIndex(String path, ResourceProvider provider) {
        this.path = path;
        this.provider = provider;
    }

    public void load() {
        InputStream in = provider.getResourceAsStream(this.path + "index.json");

        if (in != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            Map<String, String> raw = GSON.fromJson(reader.lines().collect(Collectors.joining()), TYPE);
            raw.forEach((key, value) -> this.put(key, this.path + value));
        }
    }

    public String getPath() {
        return path;
    }
}
