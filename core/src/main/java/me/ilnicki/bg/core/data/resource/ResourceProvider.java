package me.ilnicki.bg.core.data.resource;

import java.io.InputStream;
import java.net.URL;

public interface ResourceProvider {
    InputStream getResourceAsStream(String name);
    URL getResource(String name);
}
