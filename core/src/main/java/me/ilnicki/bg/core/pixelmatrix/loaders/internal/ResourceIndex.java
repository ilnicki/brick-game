package me.ilnicki.bg.core.pixelmatrix.loaders.internal;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ResourceIndex extends HashMap<String, String> {
    private final String path;

    public ResourceIndex(String path) {
        this.path = path;
    }

    public void load() {
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Gson gson = new Gson();

        InputStream in = getClass().getResourceAsStream(this.path + "index.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        Map<String, String> raw = gson.fromJson(reader.lines().collect(Collectors.joining()), type);

        raw.forEach((key, value) -> this.put(key, this.path + value));
    }
}
