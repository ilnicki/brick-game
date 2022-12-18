package me.ilnicki.bg.core.data.resource;

import java.util.concurrent.ConcurrentMap;

public interface ResourceIndex extends ConcurrentMap<String, String> {
  void load();
}
