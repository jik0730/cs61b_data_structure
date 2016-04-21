// declare as part of a synthesizer package
package synthesizer;

public abstract class AbstractBoundedQueue<T> implements BoundedQueue<T> {
    protected int fillCount;
    protected int capacity;

    // return the size of buffer    
    public int capacity() {
        return capacity;
    }

    // return the number of items in the buffer
    public int fillCount() {
        return fillCount;
    }
}
