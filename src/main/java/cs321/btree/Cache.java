package cs321.btree;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cache<K,V> extends LinkedHashMap<K,V> {

    private int maxSize;

    public Cache(int maxSize) {
        super(maxSize, 0.75f, true);
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return (size() > maxSize);
    }
}
