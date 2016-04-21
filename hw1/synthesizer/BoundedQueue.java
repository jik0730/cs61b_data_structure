// declare as part of a synthesizer package
package synthesizer;

// import Iterator package
import java.util.Iterator;

public interface BoundedQueue<T> extends Iterable<T> {

    // return size of the buffer
    int capacity();

    // return number of items curruntly in the buffer
    int fillCount();

    // add item x to the end
    void enqueue(T x);

    // delete item and return the item from the front
    T dequeue();

    // return item from the front
    T peek();

    Iterator<T> iterator();

    // is the buffer empty?
    default boolean isEmpty() {
        if (this.fillCount() == 0) {
            return true;
        }
        return false;
    }

    // is the buffer full?
    default boolean isFull() {
        if (this.fillCount() == this.capacity()) {
            return true;
        }
        return false;
    }
}
