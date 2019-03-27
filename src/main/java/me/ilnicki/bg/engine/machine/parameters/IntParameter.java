package me.ilnicki.bg.engine.machine.parameters;

import me.ilnicki.bg.engine.system.Holder;

public class IntParameter implements Holder<Integer> {
    private int value;
    private final int minValue;
    private final int maxValue;

    public IntParameter() {
        this(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public IntParameter(int maxValue) {
        this(Integer.MIN_VALUE, maxValue);
    }

    public IntParameter(int minValue, int maxValue) {
        this(minValue, maxValue, minValue);
    }

    public IntParameter(int minValue, int maxValue, int value) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.value = value;
    }

    public Integer get() {
        return value;
    }

    public void set(Integer value) {
        if (value > maxValue) {
            this.value = maxValue;
        } else if (value < minValue) {
            this.value = minValue;
        } else {
            this.value = value;
        }
    }

    public void inc() {
        if (++value > maxValue)
            value = minValue;
    }

    public void dec() {
        if (--value < minValue)
            value = maxValue;
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
