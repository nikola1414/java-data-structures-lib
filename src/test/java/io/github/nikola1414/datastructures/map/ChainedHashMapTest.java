package io.github.nikola1414.datastructures.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.Test;

class ChainedHashMapTest {
    @Test
    void storesAndReplacesValues() {
        ChainedHashMap<String, Integer> map = new ChainedHashMap<>();

        assertNull(map.put("quality", 1));
        assertEquals(1, map.put("quality", 2));
        assertEquals(2, map.get("quality"));
        assertEquals(1, map.size());
    }

    @Test
    void resolvesCollisionsUsingSeparateChaining() {
        ChainedHashMap<CollisionKey, String> map = new ChainedHashMap<>(4);
        CollisionKey first = new CollisionKey("first");
        CollisionKey second = new CollisionKey("second");
        CollisionKey third = new CollisionKey("third");

        map.put(first, "A");
        map.put(second, "B");
        map.put(third, "C");

        assertEquals("A", map.get(first));
        assertEquals("B", map.get(second));
        assertEquals("C", map.get(third));
    }

    @Test
    void removesEntryFromCollisionChain() {
        ChainedHashMap<CollisionKey, String> map = new ChainedHashMap<>(4);
        CollisionKey first = new CollisionKey("first");
        CollisionKey middle = new CollisionKey("middle");
        CollisionKey last = new CollisionKey("last");
        map.put(first, "A");
        map.put(middle, "B");
        map.put(last, "C");

        assertEquals("B", map.remove(middle));
        assertNull(map.get(middle));
        assertEquals("A", map.get(first));
        assertEquals("C", map.get(last));
        assertEquals(2, map.size());
    }

    @Test
    void preservesEntriesWhenResizing() {
        ChainedHashMap<Integer, String> map = new ChainedHashMap<>(4);

        for (int index = 0; index < 1_000; index++) {
            map.put(index, "value-" + index);
        }

        assertEquals(1_000, map.size());
        for (int index = 0; index < 1_000; index++) {
            assertEquals("value-" + index, map.get(index));
        }
    }

    @Test
    void exposesKeysAndDefaultLookup() {
        ChainedHashMap<String, Integer> map = new ChainedHashMap<>();
        map.put("heap", 1);
        map.put("tree", 2);

        assertEquals(Set.of("heap", "tree"), Set.copyOf(map.keys()));
        assertEquals(99, map.getOrDefault("missing", 99));
        assertThrows(UnsupportedOperationException.class, () -> map.keys().add("cache"));
    }

    @Test
    void rejectsInvalidInputs() {
        assertThrows(IllegalArgumentException.class, () -> new ChainedHashMap<>(0));
        ChainedHashMap<String, Integer> map = new ChainedHashMap<>();
        assertThrows(NullPointerException.class, () -> map.put(null, 1));
        assertThrows(NullPointerException.class, () -> map.put("key", null));
        assertThrows(NullPointerException.class, () -> map.get(null));
    }

    @Test
    void clearsAllEntries() {
        ChainedHashMap<String, Integer> map = new ChainedHashMap<>();
        map.put("one", 1);
        map.put("two", 2);

        map.clear();

        assertTrue(map.isEmpty());
        assertFalse(map.containsKey("one"));
    }

    private record CollisionKey(String value) {
        @Override
        public int hashCode() {
            return 42;
        }
    }
}
