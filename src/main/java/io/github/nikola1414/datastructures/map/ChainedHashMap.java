package io.github.nikola1414.datastructures.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ChainedHashMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int MINIMUM_CAPACITY = 4;
    private static final float LOAD_FACTOR = 0.75f;

    private Entry<K, V>[] buckets;
    private int resizeThreshold;
    private int size;

    public ChainedHashMap() {
        this(DEFAULT_CAPACITY);
    }

    public ChainedHashMap(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("initial capacity must be positive");
        }
        int capacity = normalizedCapacity(initialCapacity);
        buckets = createBuckets(capacity);
        resizeThreshold = threshold(capacity);
    }

    public V put(K key, V value) {
        requireKey(key);
        requireValue(value);
        int index = bucketIndex(key, buckets.length);
        Entry<K, V> current = buckets[index];
        while (current != null) {
            if (current.key.equals(key)) {
                V previous = current.value;
                current.value = value;
                return previous;
            }
            current = current.next;
        }

        buckets[index] = new Entry<>(key, value, buckets[index]);
        size++;
        if (size > resizeThreshold) {
            resize();
        }
        return null;
    }

    public V get(K key) {
        Entry<K, V> entry = findEntry(requireKey(key));
        return entry == null ? null : entry.value;
    }

    public V getOrDefault(K key, V defaultValue) {
        Entry<K, V> entry = findEntry(requireKey(key));
        return entry == null ? defaultValue : entry.value;
    }

    public boolean containsKey(K key) {
        return findEntry(requireKey(key)) != null;
    }

    public V remove(K key) {
        requireKey(key);
        int index = bucketIndex(key, buckets.length);
        Entry<K, V> previous = null;
        Entry<K, V> current = buckets[index];

        while (current != null) {
            if (current.key.equals(key)) {
                if (previous == null) {
                    buckets[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return current.value;
            }
            previous = current;
            current = current.next;
        }
        return null;
    }

    public List<K> keys() {
        List<K> keys = new ArrayList<>(size);
        for (Entry<K, V> bucket : buckets) {
            Entry<K, V> current = bucket;
            while (current != null) {
                keys.add(current.key);
                current = current.next;
            }
        }
        return List.copyOf(keys);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        buckets = createBuckets(DEFAULT_CAPACITY);
        resizeThreshold = threshold(DEFAULT_CAPACITY);
        size = 0;
    }

    private Entry<K, V> findEntry(K key) {
        Entry<K, V> current = buckets[bucketIndex(key, buckets.length)];
        while (current != null) {
            if (current.key.equals(key)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    private void resize() {
        Entry<K, V>[] oldBuckets = buckets;
        buckets = createBuckets(oldBuckets.length << 1);
        resizeThreshold = threshold(buckets.length);

        for (Entry<K, V> bucket : oldBuckets) {
            Entry<K, V> current = bucket;
            while (current != null) {
                Entry<K, V> next = current.next;
                int index = bucketIndex(current.key, buckets.length);
                current.next = buckets[index];
                buckets[index] = current;
                current = next;
            }
        }
    }

    private static int bucketIndex(Object key, int capacity) {
        int hash = key.hashCode();
        return (hash ^ (hash >>> 16)) & (capacity - 1);
    }

    private static int normalizedCapacity(int requestedCapacity) {
        int maximum = 1 << 30;
        if (requestedCapacity >= maximum) {
            return maximum;
        }
        int capacity = MINIMUM_CAPACITY;
        while (capacity < requestedCapacity) {
            capacity <<= 1;
        }
        return capacity;
    }

    private static int threshold(int capacity) {
        return Math.max(1, (int) (capacity * LOAD_FACTOR));
    }

    private K requireKey(K key) {
        return Objects.requireNonNull(key, "key must not be null");
    }

    private void requireValue(V value) {
        Objects.requireNonNull(value, "value must not be null");
    }

    @SuppressWarnings("unchecked")
    private static <K, V> Entry<K, V>[] createBuckets(int capacity) {
        return (Entry<K, V>[]) new Entry<?, ?>[capacity];
    }

    private static final class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        private Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
