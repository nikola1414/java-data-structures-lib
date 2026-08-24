package io.github.nikola1414.datastructures.tree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BinarySearchTreeTest {
    private BinarySearchTree<Integer> tree;

    @BeforeEach
    void setUp() {
        tree = BinarySearchTree.naturalOrder();
        List.of(8, 3, 10, 1, 6, 14, 4, 7, 13).forEach(tree::insert);
    }

    @Test
    void returnsAllDepthFirstTraversals() {
        assertEquals(List.of(1, 3, 4, 6, 7, 8, 10, 13, 14), tree.inOrder());
        assertEquals(List.of(8, 3, 1, 6, 4, 7, 10, 14, 13), tree.preOrder());
        assertEquals(List.of(1, 4, 7, 6, 3, 13, 14, 10, 8), tree.postOrder());
    }

    @Test
    void ignoresDuplicateValues() {
        assertFalse(tree.insert(6));
        assertEquals(9, tree.size());
    }

    @Test
    void deletesLeafNode() {
        assertTrue(tree.delete(1));

        assertFalse(tree.contains(1));
        assertEquals(List.of(3, 4, 6, 7, 8, 10, 13, 14), tree.inOrder());
    }

    @Test
    void deletesNodeWithOneChild() {
        assertTrue(tree.delete(14));

        assertTrue(tree.contains(13));
        assertEquals(List.of(1, 3, 4, 6, 7, 8, 10, 13), tree.inOrder());
    }

    @Test
    void deletesRootWithTwoChildren() {
        assertTrue(tree.delete(8));

        assertFalse(tree.contains(8));
        assertEquals(List.of(1, 3, 4, 6, 7, 10, 13, 14), tree.inOrder());
        assertEquals(8, tree.size());
    }

    @Test
    void reportsMissingDeletionWithoutMutation() {
        assertFalse(tree.delete(99));
        assertEquals(9, tree.size());
    }

    @Test
    void supportsCustomOrdering() {
        BinarySearchTree<String> byLength = new BinarySearchTree<>(Comparator.comparingInt(String::length));
        byLength.insert("modern");
        byLength.insert("api");
        byLength.insert("engineering");

        assertEquals(List.of("api", "modern", "engineering"), byLength.inOrder());
    }

    @Test
    void rejectsNullValuesAndExposesImmutableTraversals() {
        assertThrows(NullPointerException.class, () -> tree.insert(null));
        assertThrows(NullPointerException.class, () -> tree.contains(null));
        assertThrows(UnsupportedOperationException.class, () -> tree.inOrder().add(20));
    }

    @Test
    void clearsTree() {
        tree.clear();

        assertTrue(tree.isEmpty());
        assertEquals(List.of(), tree.inOrder());
    }
}
