package me.ilnicki.bg.core.system;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class ModuleSet implements Module {
  private final Set<Module> modules = new LinkedHashSet<>();

  public boolean add(Module module) {
    return modules.add(module);
  }

  public boolean addAll(Collection<? extends Module> collection) {
    return modules.addAll(collection);
  }

  @Override
  public void load() {
    modules.forEach(Module::load);
  }

  @Override
  public void update(int delta) {
    modules.forEach(module -> module.update(delta));
  }

  @Override
  public void stop() {
    modules.forEach(Module::stop);
  }
}
