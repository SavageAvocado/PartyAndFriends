package net.savagedev.paf.utils;

public class KeyValueSet<A> {
    private String key;
    private A value;

    public KeyValueSet(String key, A value) {
        this.value = value;
        this.key = key;
    }

    String getKey() {
        return this.key;
    }

    A getValue() {
        return this.value;
    }
}
