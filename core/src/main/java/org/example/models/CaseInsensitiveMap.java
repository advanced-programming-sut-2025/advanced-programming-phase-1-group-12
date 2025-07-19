package org.example.models;

import java.util.HashMap;

public class CaseInsensitiveMap<V> extends HashMap<String, V> {
    @Override
    public V get(Object key) {
        if (key instanceof String strKey) {
            for (Entry<String, V> entry : this.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(strKey)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key instanceof String strKey) {
            for (String existingKey : this.keySet()) {
                if (existingKey.equalsIgnoreCase(strKey)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public V remove(Object key) {
        if (key instanceof String strKey) {
            for (String existingKey : this.keySet()) {
                if (existingKey.equalsIgnoreCase(strKey)) {
                    return super.remove(existingKey);
                }
            }
        }
        return null;
    }
}
