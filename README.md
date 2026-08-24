# Java Data Structures

[![CI](https://github.com/nikola1414/java-data-structures-lib/actions/workflows/ci.yml/badge.svg)](https://github.com/nikola1414/java-data-structures-lib/actions/workflows/ci.yml)
[![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/21/)
[![JUnit](https://img.shields.io/badge/JUnit-5-25A162?logo=junit5&logoColor=white)](https://junit.org/junit5/)
[![Build](https://img.shields.io/badge/build-Maven-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)

A dependency-free collection of foundational data structures implemented from first principles in modern Java. The project emphasizes predictable APIs, generic types, explicit edge-case handling, asymptotic efficiency, and focused automated tests.

## Implemented structures

| Structure | Highlights | Core complexity |
| --- | --- | --- |
| `MinHeap<E>` | Natural or custom ordering, bottom-up heap construction, duplicate support | peek O(1), add/remove O(log n), build O(n) |
| `BinarySearchTree<E>` | Set semantics, insert, search, complete deletion, three depth-first traversals | average O(log n), worst O(n) |
| `ChainedHashMap<K, V>` | Separate chaining, replacement, removal, automatic resizing | average O(1), worst O(n) |
| `LruCache<K, V>` | Custom hash map + doubly linked list, immutable recency view | get/put/remove O(1) average |

## Quick start

```java
import io.github.nikola1414.datastructures.cache.LruCache;
import io.github.nikola1414.datastructures.heap.MinHeap;
import io.github.nikola1414.datastructures.map.ChainedHashMap;
import io.github.nikola1414.datastructures.tree.BinarySearchTree;

var heap = MinHeap.<Integer>naturalOrder();
heap.add(8);
heap.add(3);
heap.add(5);
int minimum = heap.removeMin();

var tree = BinarySearchTree.<Integer>naturalOrder();
tree.insert(8);
tree.insert(3);
tree.insert(10);
var sorted = tree.inOrder();

var map = new ChainedHashMap<String, Integer>();
map.put("heap", 1);
map.put("tree", 2);

var cache = new LruCache<String, String>(2);
cache.put("profile", "Nikola");
cache.put("role", "Software Engineer");
var profile = cache.get("profile");
```

## Design decisions

- Generic APIs support domain types rather than only primitives.
- Comparators make heap and tree ordering explicit and reusable.
- Null keys, values, and elements are rejected to keep contracts unambiguous.
- BST duplicates use set semantics and leave the tree unchanged.
- Traversal and recency results are immutable snapshots.
- Hash buckets resize at a 0.75 load factor and retain collision chains.
- LRU reads and updates promote entries without scanning the cache.
- Public operations preserve size and link invariants across failure paths.

## Project layout

```text
src
├── main/java/io/github/nikola1414/datastructures
│   ├── cache/LruCache.java
│   ├── heap/MinHeap.java
│   ├── map/ChainedHashMap.java
│   └── tree/BinarySearchTree.java
└── test/java/io/github/nikola1414/datastructures
    ├── cache/LruCacheTest.java
    ├── heap/MinHeapTest.java
    ├── map/ChainedHashMapTest.java
    └── tree/BinarySearchTreeTest.java
```

## Build and test

Requirements: JDK 21 and Maven 3.9 or newer.

```bash
mvn clean verify
```

The Maven compiler enables all lint checks and treats warnings as build failures. GitHub Actions runs the complete verification suite for every push and pull request to `main`.

## Test strategy

The JUnit 5 suite validates normal behavior, boundary conditions, invalid inputs, duplicate handling, every BST deletion shape, forced hash collisions, resizing under load, immutable views, and LRU link integrity through promotion and eviction scenarios.

## Roadmap

- Iterators and collection-compatible views
- Self-balancing search trees
- Property-based and randomized differential tests
- JMH benchmark suite
