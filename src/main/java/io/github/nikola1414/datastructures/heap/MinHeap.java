package io.github.nikola1414.datastructures.heap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public final class MinHeap<E> {
    private final List<E> elements;
    private final Comparator<? super E> comparator;

    public MinHeap(Comparator<? super E> comparator) {
        this.elements = new ArrayList<>();
        this.comparator = Objects.requireNonNull(comparator, "comparator must not be null");
    }

    public MinHeap(Collection<? extends E> values, Comparator<? super E> comparator) {
        Objects.requireNonNull(values, "values must not be null");
        this.comparator = Objects.requireNonNull(comparator, "comparator must not be null");
        this.elements = new ArrayList<>(values.size());
        values.forEach(this::requireElement);
        this.elements.addAll(values);
        heapify();
    }

    public static <E extends Comparable<? super E>> MinHeap<E> naturalOrder() {
        return new MinHeap<>(Comparator.naturalOrder());
    }

    public static <E extends Comparable<? super E>> MinHeap<E> from(Collection<? extends E> values) {
        return new MinHeap<>(values, Comparator.naturalOrder());
    }

    public void add(E element) {
        requireElement(element);
        elements.add(element);
        siftUp(elements.size() - 1);
    }

    public E peek() {
        ensureNotEmpty();
        return elements.getFirst();
    }

    public E removeMin() {
        ensureNotEmpty();
        E minimum = elements.getFirst();
        E last = elements.removeLast();
        if (!elements.isEmpty()) {
            elements.set(0, last);
            siftDown(0);
        }
        return minimum;
    }

    public int size() {
        return elements.size();
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public void clear() {
        elements.clear();
    }

    private void heapify() {
        for (int index = parentIndex(elements.size() - 1); index >= 0; index--) {
            siftDown(index);
        }
    }

    private void siftUp(int index) {
        int current = index;
        while (current > 0) {
            int parent = parentIndex(current);
            if (compare(current, parent) >= 0) {
                return;
            }
            swap(current, parent);
            current = parent;
        }
    }

    private void siftDown(int index) {
        int current = index;
        while (leftChildIndex(current) < elements.size()) {
            int smallest = leftChildIndex(current);
            int right = rightChildIndex(current);
            if (right < elements.size() && compare(right, smallest) < 0) {
                smallest = right;
            }
            if (compare(current, smallest) <= 0) {
                return;
            }
            swap(current, smallest);
            current = smallest;
        }
    }

    private int compare(int first, int second) {
        return comparator.compare(elements.get(first), elements.get(second));
    }

    private void swap(int first, int second) {
        E value = elements.get(first);
        elements.set(first, elements.get(second));
        elements.set(second, value);
    }

    private static int parentIndex(int index) {
        return (index - 1) / 2;
    }

    private static int leftChildIndex(int index) {
        return index * 2 + 1;
    }

    private static int rightChildIndex(int index) {
        return index * 2 + 2;
    }

    private void requireElement(E element) {
        Objects.requireNonNull(element, "element must not be null");
    }

    private void ensureNotEmpty() {
        if (elements.isEmpty()) {
            throw new NoSuchElementException("heap is empty");
        }
    }
}
