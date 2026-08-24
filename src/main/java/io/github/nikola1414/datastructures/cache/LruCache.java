package io.github.nikola1414.datastructures.cache;

import io.github.nikola1414.datastructures.map.ChainedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class LruCache<K, V> {
    private final int capacity;
    private final ChainedHashMap<K, Node<K, V>> entries;
    private Node<K, V> mostRecent;
    private Node<K, V> leastRecent;

    public LruCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.capacity = capacity;
        this.entries = new ChainedHashMap<>(capacity);
    }

    public Optional<V> get(K key) {
        Node<K, V> node = entries.get(requireKey(key));
        if (node == null) {
            return Optional.empty();
        }
        moveToFront(node);
        return Optional.of(node.value);
    }

    public void put(K key, V value) {
        requireKey(key);
        requireValue(value);
        Node<K, V> existing = entries.get(key);
        if (existing != null) {
            existing.value = value;
            moveToFront(existing);
            return;
        }

        Node<K, V> node = new Node<>(key, value);
        entries.put(key, node);
        addFirst(node);
        if (entries.size() > capacity) {
            evictLeastRecent();
        }
    }

    public Optional<V> remove(K key) {
        Node<K, V> node = entries.remove(requireKey(key));
        if (node == null) {
            return Optional.empty();
        }
        unlink(node);
        return Optional.of(node.value);
    }

    public boolean containsKey(K key) {
        return entries.containsKey(requireKey(key));
    }

    public List<K> keysByRecency() {
        List<K> keys = new ArrayList<>(size());
        Node<K, V> current = mostRecent;
        while (current != null) {
            keys.add(current.key);
            current = current.next;
        }
        return List.copyOf(keys);
    }

    public int size() {
        return entries.size();
    }

    public int capacity() {
        return capacity;
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public void clear() {
        entries.clear();
        mostRecent = null;
        leastRecent = null;
    }

    private void moveToFront(Node<K, V> node) {
        if (node == mostRecent) {
            return;
        }
        unlink(node);
        addFirst(node);
    }

    private void addFirst(Node<K, V> node) {
        node.previous = null;
        node.next = mostRecent;
        if (mostRecent != null) {
            mostRecent.previous = node;
        }
        mostRecent = node;
        if (leastRecent == null) {
            leastRecent = node;
        }
    }

    private void unlink(Node<K, V> node) {
        if (node.previous == null) {
            mostRecent = node.next;
        } else {
            node.previous.next = node.next;
        }
        if (node.next == null) {
            leastRecent = node.previous;
        } else {
            node.next.previous = node.previous;
        }
        node.previous = null;
        node.next = null;
    }

    private void evictLeastRecent() {
        Node<K, V> evicted = leastRecent;
        if (evicted == null) {
            return;
        }
        unlink(evicted);
        entries.remove(evicted.key);
    }

    private K requireKey(K key) {
        return Objects.requireNonNull(key, "key must not be null");
    }

    private void requireValue(V value) {
        Objects.requireNonNull(value, "value must not be null");
    }

    private static final class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> previous;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
