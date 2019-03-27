package me.ilnicki.bg.engine.system;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class ModuleSet implements Set<Module>, Module {
    private final Set<Module> modules = new LinkedHashSet<>();

    @Override
    public int size() {
        return modules.size();
    }

    @Override
    public boolean isEmpty() {
        return modules.isEmpty();
    }

    @Override
    public boolean contains(Object module) {
        return modules.contains(module);
    }

    @Override
    public Iterator<Module> iterator() {
        return modules.iterator();
    }

    @Override
    public Object[] toArray() {
        return modules.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return modules.toArray(ts);
    }

    @Override
    public boolean add(Module module) {
        return modules.add(module);
    }

    @Override
    public boolean remove(Object module) {
        return modules.remove(module);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return modules.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends Module> collection) {
        return modules.addAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return modules.retainAll(collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return modules.removeAll(collection);
    }

    @Override
    public void clear() {
        modules.clear();
    }

    @Override
    public void load() {
        modules.forEach(Module::load);
    }

    @Override
    public void update(long tick) {
        modules.forEach(module -> module.update(tick));
    }

    @Override
    public void stop() {
        modules.forEach(Module::stop);
    }
}
