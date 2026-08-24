package io.github.nikola1414.datastructures.heap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class MinHeapTest {
    @Test
    void removesElementsInAscendingOrder() {
        MinHeap<Integer> heap = MinHeap.naturalOrder();
        List.of(7, 2, 9, 1, 5, 2).forEach(heap::add);

        assertEquals(1, heap.peek());
        assertEquals(List.of(1, 2, 2, 5, 7, 9), drain(heap));
        assertTrue(heap.isEmpty());
    }

    @Test
    void buildsHeapFromCollection() {
        MinHeap<Integer> heap = MinHeap.from(List.of(12, -3, 8, 0, 4));

        assertEquals(List.of(-3, 0, 4, 8, 12), drain(heap));
    }

    @Test
    void supportsCustomComparator() {
        MinHeap<String> heap = new MinHeap<>(Comparator.comparingInt(String::length));
        heap.add("architecture");
        heap.add("api");
        heap.add("cache");

        assertEquals("api", heap.removeMin());
        assertEquals("cache", heap.removeMin());
    }

    @Test
    void rejectsNullElements() {
        MinHeap<Integer> heap = MinHeap.naturalOrder();

        assertThrows(NullPointerException.class, () -> heap.add(null));
        assertThrows(NullPointerException.class, () -> MinHeap.<Integer>from(null));
        assertThrows(NullPointerException.class, () -> MinHeap.from(java.util.Arrays.asList(1, null)));
    }

    @Test
    void failsFastWhenEmpty() {
        MinHeap<Integer> heap = MinHeap.naturalOrder();

        assertThrows(NoSuchElementException.class, heap::peek);
        assertThrows(NoSuchElementException.class, heap::removeMin);
    }

    @Test
    void clearsAllElements() {
        MinHeap<Integer> heap = MinHeap.from(List.of(3, 1, 2));

        heap.clear();

        assertEquals(0, heap.size());
        assertTrue(heap.isEmpty());
    }

    private static <E> List<E> drain(MinHeap<E> heap) {
        java.util.ArrayList<E> values = new java.util.ArrayList<>();
        while (!heap.isEmpty()) {
            values.add(heap.removeMin());
        }
        return values;
    }
}
