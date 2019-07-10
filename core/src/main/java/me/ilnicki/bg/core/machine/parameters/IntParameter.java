package me.ilnicki.bg.core.machine.parameters;

import me.ilnicki.bg.core.system.Ref;

public class IntParameter implements Ref<Integer> {
    private int value;
    private final int min;
    private final int max;

    public IntParameter() {
        this(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public IntParameter(int max) {
        this(Integer.MIN_VALUE, max);
    }

    public IntParameter(int min, int max) {
        this(min, max, min);
    }

    public IntParameter(int min, int max, int value) {
        this.min = min;
        this.max = max;
        this.value = value;
    }

    public Integer get() {
        return value;
    }

    public Integer getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }

    public void set(Integer value) {
        if (value > max) {
            this.value = max;
        } else if (value < min) {
            this.value = min;
        } else {
            this.value = value;
        }
    }

    public void inc() {
        if (++value > max)
            value = min;
    }

    public void dec() {
        if (--value < min)
            value = max;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + value;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final IntParameter other = (IntParameter) obj;
        return value == other.value;
    }
}
