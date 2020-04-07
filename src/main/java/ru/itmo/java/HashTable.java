package ru.itmo.java;

public class HashTable {

    private double hashTableLoadFactor;
    private int hashTableCapacity;
    private int hashTableSize;
    private Entry[] data;

    public HashTable(int initialCapacity, double loadFactor) {
        hashTableCapacity = initialCapacity;
        data = new Entry[hashTableCapacity];
        hashTableLoadFactor = loadFactor;
    }

    public HashTable(int initialCapacity) {
        this(initialCapacity, 0.5);
    }

    public Object put(Object key, Object value) {
        int pos = conditionAnd(key);
        if (pos == -1) {
            pos = conditionOr(key);
        }

        Object prevValue = null;
        if (data[pos] != null) {
            prevValue = data[pos].isValue();
        }

        data[pos] = new Entry(key, value);
        if (prevValue == null) {
            hashTableSize++;
        }

        if (hashTableSize >= hashTableCapacity * hashTableLoadFactor) {
            hashTableCapacity = hashTableCapacity * 2;
            Entry[] oldPairs = data;
            data = new Entry[hashTableCapacity];
            int prevSize = hashTableSize;

            for (Entry oldPair : oldPairs) {
                if (oldPair != null && !oldPair.isNextPlace()) {
                    data[conditionOr(oldPair.isKey())] = new Entry(oldPair.isKey(), oldPair.isValue());
                }
            }

            hashTableSize = prevSize;
        }

        return prevValue;
    }

    public Object get(Object key) {
        int pos = conditionAnd(key);
        if (pos == -1) {
            return null;
        }

        Entry pair = data[conditionAnd(key)];

        return pair.isValue();
    }

    public Object remove(Object key) {
        int pos = conditionAnd(key);
        if (pos == -1) {
            return null;
        }

        Object result = data[pos].isValue();
        data[pos] = new Entry();
        hashTableSize--;
        return result;
    }

    public int size() {
        return hashTableSize;
    }

    private int conditionOr(Object key) {
        for (int i = Math.abs(key.hashCode() % data.length); i < data.length; i++) {
            if (data[i] == null || data[i].isNextPlace()) {
                return i;
            }
        }
        for (int i = 0; i < Math.abs(key.hashCode() % data.length); i++) {
            if (data[i] == null || data[i].isNextPlace()) {
                return i;
            }
        }
        return -1;
    }

    private int conditionAnd(Object key) {
        for (int i = Math.abs(key.hashCode() % data.length); i < data.length; i++) {
            if (data[i] == null) {
                return -1;
            }
            if (!data[i].isNextPlace() && data[i].key.equals(key)) {
                return i;
            }
        }
        for (int i = 0; i < Math.abs(key.hashCode() % data.length); i++) {
            if (data[i] == null) {
                return -1;
            }
            if (!data[i].isNextPlace() && data[i].key.equals(key)) {
                return i;
            }
        }
        return -1;
    }

    private static class Entry {

        private Object key;
        private Object value;
        private boolean nextPlace;

        private Entry() {
            key = null;
            value = null;
            nextPlace = true;
        }

        private Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        public Object isKey() {
            return key;
        }

        public Object isValue() {
            return value;
        }

        public boolean isNextPlace() {
            return nextPlace;
        }
    }
}