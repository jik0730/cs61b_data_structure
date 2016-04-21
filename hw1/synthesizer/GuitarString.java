//package <package name>;
package synthesizer;

import java.util.Iterator;

//Make sure this class is public
public class GuitarString extends Object {
    /** Constants. Do not change. In case you're curious, the keyword final means
     * the values cannot be changed at runtime. We'll discuss this and other topics
     * in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        buffer = new ArrayRingBuffer((int) Math.round(SR / frequency));
        Iterator<Double> bu = buffer.iterator();
        while (bu.hasNext()) {
            buffer.enqueue((double) 0);
            double temp = bu.next();
        }
    }

    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        Iterator<Double> bu = buffer.iterator();
        while (bu.hasNext()) {
            double temp = buffer.dequeue();
            double r = Math.random() - 0.5;
            buffer.enqueue(r);
            temp = bu.next();
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm. 
     */
    public void tic() {
        double temp1 = buffer.dequeue();
        double temp2 = buffer.peek();
        buffer.enqueue(DECAY * (temp1 + temp2) / 2);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.peek();
    }
}
