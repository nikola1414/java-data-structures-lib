package io.github.nikola1414.datastructures.tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class BinarySearchTree<E> {
    private final Comparator<? super E> comparator;
    private Node<E> root;
    private int size;

    public BinarySearchTree(Comparator<? super E> comparator) {
        this.comparator = Objects.requireNonNull(comparator, "comparator must not be null");
    }

    public static <E extends Comparable<? super E>> BinarySearchTree<E> naturalOrder() {
        return new BinarySearchTree<>(Comparator.naturalOrder());
    }

    public boolean insert(E value) {
        requireValue(value);
        if (root == null) {
            root = new Node<>(value);
            size = 1;
            return true;
        }

        Node<E> current = root;
        while (true) {
            int comparison = comparator.compare(value, current.value);
            if (comparison == 0) {
                return false;
            }
            if (comparison < 0) {
                if (current.left == null) {
                    current.left = new Node<>(value);
                    size++;
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new Node<>(value);
                    size++;
                    return true;
                }
                current = current.right;
            }
        }
    }

    public boolean delete(E value) {
        requireValue(value);
        Node<E> parent = null;
        Node<E> current = root;

        while (current != null) {
            int comparison = comparator.compare(value, current.value);
            if (comparison == 0) {
                deleteNode(parent, current);
                size--;
                return true;
            }
            parent = current;
            current = comparison < 0 ? current.left : current.right;
        }
        return false;
    }

    public boolean contains(E value) {
        requireValue(value);
        Node<E> current = root;
        while (current != null) {
            int comparison = comparator.compare(value, current.value);
            if (comparison == 0) {
                return true;
            }
            current = comparison < 0 ? current.left : current.right;
        }
        return false;
    }

    public List<E> inOrder() {
        List<E> values = new ArrayList<>(size);
        traverseInOrder(root, values);
        return List.copyOf(values);
    }

    public List<E> preOrder() {
        List<E> values = new ArrayList<>(size);
        traversePreOrder(root, values);
        return List.copyOf(values);
    }

    public List<E> postOrder() {
        List<E> values = new ArrayList<>(size);
        traversePostOrder(root, values);
        return List.copyOf(values);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    private void deleteNode(Node<E> parent, Node<E> node) {
        if (node.left != null && node.right != null) {
            Node<E> successorParent = node;
            Node<E> successor = node.right;
            while (successor.left != null) {
                successorParent = successor;
                successor = successor.left;
            }
            node.value = successor.value;
            replaceChild(successorParent, successor, successor.right);
            return;
        }
        Node<E> replacement = node.left != null ? node.left : node.right;
        replaceChild(parent, node, replacement);
    }

    private void replaceChild(Node<E> parent, Node<E> node, Node<E> replacement) {
        if (parent == null) {
            root = replacement;
        } else if (parent.left == node) {
            parent.left = replacement;
        } else {
            parent.right = replacement;
        }
    }

    private void traverseInOrder(Node<E> node, List<E> values) {
        if (node == null) {
            return;
        }
        traverseInOrder(node.left, values);
        values.add(node.value);
        traverseInOrder(node.right, values);
    }

    private void traversePreOrder(Node<E> node, List<E> values) {
        if (node == null) {
            return;
        }
        values.add(node.value);
        traversePreOrder(node.left, values);
        traversePreOrder(node.right, values);
    }

    private void traversePostOrder(Node<E> node, List<E> values) {
        if (node == null) {
            return;
        }
        traversePostOrder(node.left, values);
        traversePostOrder(node.right, values);
        values.add(node.value);
    }

    private void requireValue(E value) {
        Objects.requireNonNull(value, "value must not be null");
    }

    private static final class Node<E> {
        private E value;
        private Node<E> left;
        private Node<E> right;

        private Node(E value) {
            this.value = value;
        }
    }
}
