package com.github.k24.prefsoven.store;

import android.support.v4.util.Pair;

import com.github.k24.prefsoven.field.AbstractOvenPrefField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by k24 on 2015/12/30.
 */
public final class Pid {
    public static final String SEP = ":";
    private final long timestamp;
    private final Map<Element<?>, Key<?>> keys = new HashMap<>();
    private volatile boolean removed;

    private Pid(long timestamp) {
        this.timestamp = timestamp;
    }

    //region For Model
    static Pid create() {
        return new Pid(System.nanoTime());
    }

    static Pair<Pid, ? extends Element<?>> create(Map<Object, Pid> pidMap, String key, Map<String, Element<?>> elementMap) {
        // TODO More efficient
        String[] segments = key.split(SEP);
        if (segments.length < 2) return null;
        long timestamp = Long.parseLong(segments[0]);
        Pid pid = pidMap.get(timestamp);
        if (pid == null) {
            pid = new Pid(timestamp);
            pidMap.put(timestamp, pid);
        }
        return Pair.create(pid, elementMap.get(segments[1]));
    }

    void addKey(Key<?> key) {
        keys.put(key.element(), key);
    }

    static Map<Object, Pid> createKeyPidMap() {
        return new LinkedHashMap<>();
    }

    String getKeyAsString(Element<?> element) {
        return timestamp + ":" + element.name();
    }
    //endregion

    public boolean isBoundWithKeys() {
        return !keys.isEmpty();
    }

    public List<Element<?>> elements() {
        if (!isBoundWithKeys()) throw new IllegalStateException("Unbound");
        return new ArrayList<>(keys.keySet());
    }

    @SuppressWarnings("unchecked")
    public <T> AbstractOvenPrefField<T> value(Element<T> element) {
        if (!isBoundWithKeys()) throw new IllegalStateException("Unbound");
        return (AbstractOvenPrefField<T>) keys.get(element).value();
    }

    public Map<String, Object> valuesAsMapWithName() {
        HashMap<String, Object> map = new HashMap<>(keys.size());
        for (Map.Entry<Element<?>, Key<?>> entry : keys.entrySet()) {
            map.put(entry.getKey().name(), entry.getValue().value().get());
        }
        return map;
    }

    public long createdAtMillis() {
        return timestamp / 1000_000; // Nanos to millis
    }

    public static Comparator<Pid> comparator() {
        return new Comparator<Pid>() {
            @Override
            public int compare(Pid lhs, Pid rhs) {
                // Long.compare is supported since API-19...
                long gap = lhs.timestamp - rhs.timestamp;
                return gap < 0 ? -1 : (gap == 0 ? 0 : 1);
            }
        };
    }

    public Index index() {
        return new Index(timestamp);
    }

    void remove() {
        removed = true;
        for (Key<?> key : keys.values()) {
            key.clear();
        }
        keys.clear();
    }

    public static class Index implements Serializable {
        private final long timestamp;

        Index(long timestamp){
            this.timestamp = timestamp;
        }

        //region Generated
        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;

            Index index = (Index) object;

            return timestamp == index.timestamp;

        }

        @Override
        public int hashCode() {
            return (int) (timestamp ^ (timestamp >>> 32));
        }
        //endregion
    }
}
