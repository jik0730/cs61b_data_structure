// package <package name>;
package synthesizer;

import java.util.Iterator;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        rb = (T[]) new Object[capacity];
        first = 0;
        last = 0;
        this.fillCount = 0;
        this.capacity = capacity;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
        if (this.isFull()) {
            throw new RuntimeException("Ring Buffer Overflow");
        }
        rb[last] = x;
        fillCount += 1;
        last = (last + 1) % this.capacity;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        if (this.isEmpty()) {
            throw new RuntimeException("Ring Buffer Underflow");
        }
        T temp = rb[first];
        rb[first] = null;
        fillCount -= 1;
        first = (first + 1) % this.capacity;
        return temp;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        if (this.isEmpty()) {
            throw new RuntimeException("Ring Buffer Underflow");
        }
        return rb[first];
    }

    private class ArrayIterator implements Iterator<T> {
        private int ptr;

        public ArrayIterator() {
            ptr = 0;
        }

        public boolean hasNext() {
            return (ptr != rb.length);
        }

        public T next() {
            T returnitem = rb[ptr];
            ptr = ptr + 1;
            return returnitem;
        }
    }

    public Iterator<T> iterator() {
        return new ArrayIterator();
    }
}
