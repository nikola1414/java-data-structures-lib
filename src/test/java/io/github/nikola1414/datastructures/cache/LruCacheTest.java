package io.github.nikola1414.datastructures.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class LruCacheTest {
    @Test
    void evictsLeastRecentlyUsedEntry() {
        LruCache<String, Integer> cache = new LruCache<>(3);
        cache.put("heap", 1);
        cache.put("tree", 2);
        cache.put("map", 3);

        cache.get("heap");
        cache.put("cache", 4);

        assertFalse(cache.containsKey("tree"));
        assertEquals(List.of("cache", "heap", "map"), cache.keysByRecency());
    }

    @Test
    void promotesEntryWhenRead() {
        LruCache<Integer, String> cache = new LruCache<>(3);
        cache.put(1, "one");
        cache.put(2, "two");
        cache.put(3, "three");

        assertEquals(Optional.of("one"), cache.get(1));
        assertEquals(List.of(1, 3, 2), cache.keysByRecency());
    }

    @Test
    void updatesValueAndRecencyWithoutGrowing() {
        LruCache<String, Integer> cache = new LruCache<>(2);
        cache.put("first", 1);
        cache.put("second", 2);

        cache.put("first", 10);

        assertEquals(Optional.of(10), cache.get("first"));
        assertEquals(2, cache.size());
        assertEquals(List.of("first", "second"), cache.keysByRecency());
    }

    @Test
    void handlesSingleEntryCapacity() {
        LruCache<String, Integer> cache = new LruCache<>(1);
        cache.put("old", 1);
        cache.put("new", 2);

        assertEquals(Optional.empty(), cache.get("old"));
        assertEquals(Optional.of(2), cache.get("new"));
        assertEquals(List.of("new"), cache.keysByRecency());
    }

    @Test
    void removesEntriesAndMaintainsLinks() {
        LruCache<Integer, String> cache = new LruCache<>(3);
        cache.put(1, "one");
        cache.put(2, "two");
        cache.put(3, "three");

        assertEquals(Optional.of("two"), cache.remove(2));
        assertEquals(Optional.empty(), cache.remove(99));
        assertEquals(List.of(3, 1), cache.keysByRecency());
    }

    @Test
    void rejectsInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> new LruCache<>(0));
        LruCache<String, Integer> cache = new LruCache<>(2);
        assertThrows(NullPointerException.class, () -> cache.put(null, 1));
        assertThrows(NullPointerException.class, () -> cache.put("key", null));
        assertThrows(NullPointerException.class, () -> cache.get(null));
    }

    @Test
    void clearsCache() {
        LruCache<String, Integer> cache = new LruCache<>(2);
        cache.put("first", 1);
        cache.put("second", 2);

        cache.clear();

        assertTrue(cache.isEmpty());
        assertEquals(List.of(), cache.keysByRecency());
    }
}
