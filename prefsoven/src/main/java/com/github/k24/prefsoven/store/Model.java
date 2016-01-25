package com.github.k24.prefsoven.store;

import android.content.SharedPreferences;
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by k24 on 2015/12/30.
 */
public final class Model {
    private final PrefFieldFactory prefFieldFactory;
    private final Map<String, Element<?>> elementMap;
    private final List<Key<?>> keys = new ArrayList<>();
    private final Map<Pid.Index, Pid> pids = new LinkedHashMap<>();

    private final Object lock = new Object();

    public static Model create(PrefFieldFactory prefFieldFactory, Map<String, Element<?>> elementMap) {
        return new Model(prefFieldFactory, elementMap);
    }

    Model(PrefFieldFactory prefFieldFactory, Map<String, Element<?>> elementMap) {
        this.prefFieldFactory = prefFieldFactory;
        this.elementMap = elementMap;
        reset();
    }

    public void reset() {
        synchronized (lock) {
            // TODO Async
            SharedPreferences sharedPreferences = prefFieldFactory.getPrefs();
            Map<Object, Pid> pidMap = Pid.createKeyPidMap();

            for (String keyString : sharedPreferences.getAll().keySet()) {
                Pair<Pid, ? extends Element<?>> pair = Pid.create(pidMap, keyString, elementMap);
                if (pair == null) continue;
                Pid pid = pair.first;
                Element<?> element = pair.second;
                if (pid == null || element == null) continue;
                pids.put(pid.index(), pid);

                Key<?> key = new Key<>(this, keyString, pid, element);
                pid.addKey(key);
                element.addKey(key);
                keys.add(key);
            }
        }
    }

    public PrefFieldFactory getPrefFieldFactory() {
        return prefFieldFactory;
    }

    public Pid addElements(Collection<Element<?>> elements) {
        synchronized (lock) {
            Pid pid = Pid.create();
            for (Element<?> element : elements) {
                Key<?> key = new Key<>(this, pid.getKeyAsString(element), pid, element);
                pid.addKey(key);
                element.addKey(key);
                keys.add(key);
            }
            pids.put(pid.index(), pid);
            return pid;
        }
    }

    public List<Key<?>> keys() {
        synchronized (lock) {
            return Collections.unmodifiableList(keys);
        }
    }

    public void clear() {
        synchronized (lock) {
            for (Pid pid : pids.values()) {
                pid.remove();
            }
            pids.clear();
            keys.clear();
        }
    }

    public List<Pid> pids() {
        synchronized (lock) {
            return Collections.unmodifiableList(new ArrayList<>(pids.values()));
        }
    }

    public List<Element<?>> elements() {
        return new ArrayList<>(elementMap.values());
    }

    public List<String> elementNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Element<?> element : elementMap.values()) {
            names.add(element.name());
        }
        return names;
    }

    public Pid pid(Pid.Index index) {
        synchronized (lock) {
            return pids.get(index);
        }
    }
}