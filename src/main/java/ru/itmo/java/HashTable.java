package ru.itmo.java;

public class HashTable {

    private double HashTableLoadFactor;
    private int HashTableCapacity;
    private int HashTableSize;
    private Entry[] data;

    HashTable(int initialCapacity, double loadFactor) {
        HashTableCapacity = initialCapacity;
        data = new Entry[HashTableCapacity];
        HashTableLoadFactor = loadFactor;
    }

    HashTable(int initialCapacity) {
        HashTableCapacity = initialCapacity;
        data = new Entry[HashTableCapacity];
        HashTableLoadFactor = 0.5;
    }

    private static class Entry {

        private final Object key, value;
        private boolean TorF;

        private Entry() {
            key = null;
            value = null;
            TorF = true;
        }

        public Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        public Object getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public boolean getTorF() {
            return TorF;
        }
    }

    Object put(Object key, Object value) {
        int pos = second(key);
        if (pos == -1) {
            pos = first(key);
        }

        Object prevValue = null;
        if (data[pos] != null) {
            prevValue = data[pos].getValue();
        }

        data[pos] = new Entry(key, value);
        if (prevValue == null) {
            HashTableSize++;
        }

        if (HashTableSize >= HashTableCapacity * HashTableLoadFactor) {
            HashTableCapacity = HashTableCapacity * 2;
            Entry[] oldPairs = data;
            data = new Entry[HashTableCapacity];
            int prevSize = HashTableSize;

            for (Entry oldPair : oldPairs) {
                if (oldPair != null && !oldPair.getTorF()) {
                    data[first(oldPair.getKey())] = new Entry(oldPair.getKey(), oldPair.getValue());
                }
            }

            HashTableSize = prevSize;
        }

        return prevValue;
    }

    Object get(Object key) {
        int pos = second(key);
        if (pos == -1) {
            return null;
        }

        Entry pair = data[second(key)];

        return pair.getValue();
    }

    Object remove(Object key) {
        int pos = second(key);
        if (pos == -1) {
            return null;
        }

        Object result = data[pos].getValue();
        data[pos] = new Entry();
        HashTableSize--;
        return result;
    }

    int size() {
        return HashTableSize;
    }

    private int first(Object key) {
        for (int i = Math.abs(key.hashCode() % data.length); i < data.length; i++) {
            if (data[i] == null || data[i].getTorF()) {
                return i;
            }
        }
        for (int i = 0; i < Math.abs(key.hashCode() % data.length); i++) {
            if (data[i] == null || data[i].getTorF()) {
                return i;
            }
        }
        return -1;
    }

    private int second(Object key) {
        for (int i = Math.abs(key.hashCode() % data.length); i < data.length; i++) {
            if (data[i] == null) {
                return -1;
            }
            if (!data[i].getTorF() && data[i].key.equals(key)) {
                return i;
            }
        }
        for (int i = 0; i < Math.abs(key.hashCode() % data.length); i++) {
            if (data[i] == null) {
                return -1;
            }
            if (!data[i].getTorF() && data[i].key.equals(key)) {
                return i;
            }
        }
        return -1;
    }
}