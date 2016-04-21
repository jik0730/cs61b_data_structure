package lab8;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private Node root;
    private int size = 0;

    private class Node {
        private K key;
        private V value;
        private Node left, right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public BSTMap() {
    }
    /** Removes all of the mappings from this map. */
    public void clear() {
        root = null;
        size = 0;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        if (key == null) {
            throw new NullPointerException("argument to contains() is null");
        }
        return get(key) != null;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key. 
     */
    public V get(K key) {
        return get(root, key);
    }

    private V get(Node x, K key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) {
            return get(x.left, key);
        } else if (cmp > 0) {
            return get(x.right, key);
        } else {
            return x.value;
        }
    }

    /* Returns the number of key-value mappings in this map. */
    public int size() {
        return size;
    }

    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
        root = put(root, key, value);
        size += 1;
    }

    private Node put(Node x, K key, V value) {
        if (x == null) {
            return new Node(key, value);
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = put(x.left, key, value);
        } else if (cmp > 0) {
            x.right = put(x.right, key, value);
        } else {
            x.value = value;
            x.key = key;
        }
        return x;
    }

    /* Returns a Set view of the keys contained in this map. */
    public Set<K> keySet() {
        Set<K> set = new LinkedHashSet();
        makeKeySet(root, set);
        return set;
    }

    private void makeKeySet(Node x, Set<K> set) {
        if (x == null) {
            return;
        } else {
            makeKeySet(x.left, set);
            boolean temp = set.add(x.key);
            makeKeySet(x.right, set);
        }
    }

    public void printInOrder() {
        printInOrder(root);
    }

    private void printInOrder(Node x) {
        if (root == null) {
            return;
        } else {
            printInOrder(root.left);
            System.out.println(root.key);
            printInOrder(root.right);
        }
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an 
     * UnsupportedOperationException. */
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    public Iterator iterator() {
        throw new UnsupportedOperationException();
    }
}
