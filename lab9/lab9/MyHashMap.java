package lab9;

import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

public class MyHashMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private int numOfPairs;
    private int sizeOfTable;
    private LinkedList<Node> table;
    
    private class Node {
        private K key;
        private V value;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public MyHashMap() {

    }

    public MyHashMap()
    

    /** Removes all of the mappings from this map. */
    public void clear() {

    }

    /* Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {

    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key. 
     */
    public V get(K key) {

    }

    /* Returns the number of key-value mappings in this map. */
    public int size() {

    }

    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {

    }

    /* Returns a Set view of the keys contained in this map. */
    public Set<K> keySet() {

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

}