package hw3.hash;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

import edu.princeton.cs.algs4.StdRandom;


public class TestComplexOomage {

    @Test
    public void testHashCodeDeterministic() {
        ComplexOomage so = ComplexOomage.randomComplexOomage();
        int hashCode = so.hashCode();
        for (int i = 0; i < 100; i += 1) {
            assertEquals(hashCode, so.hashCode());
        }
    }

    public boolean haveNiceHashCodeSpread(Set<ComplexOomage> oomages) {
        /* TODO: Write a utility function that ensures that the oomages have
         * hashCodes that would distribute them fairly evenly across
         * buckets To do this, mod each's hashCode by M = 10,
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */

        Object[] elements = oomages.toArray();
        int[] bucket = new int[10];

        for (int i = 0; i < oomages.size(); i++) {
            Oomage temp = (Oomage) elements[i];
            int hashcode = (temp.hashCode() & 0x7FFFFFFF) % 10;
            bucket[hashcode] += 1;
        }

        for (int i = 0; i < bucket.length; i++) {
            if ((double) bucket[i] < (double) oomages.size() / 50.0
                     || (double) bucket[i] > (double) oomages.size() / 2.5) {
                return false;
            }
        }

        return true;
    }


    @Test
    public void testRandomItemsHashCodeSpread() {
        HashSet<ComplexOomage> oomages = new HashSet<ComplexOomage>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            oomages.add(ComplexOomage.randomComplexOomage());
        }

        assertTrue(haveNiceHashCodeSpread(oomages));
    }

    @Test
    public void testWithDeadlyParams() {
        /* TODO: Create a Set that shows the flaw in the hashCode function.
         */
        HashSet<ComplexOomage> oomages = new HashSet<ComplexOomage>();

        for (int i = 0; i < 100; i++) {
            LinkedList<Integer> temp = new LinkedList<>();
            for (int j = 0; j < 5; j++) {
                temp.addLast(10);
            }
            for (int j = 0; j < i; j++) {
                temp.addLast(0);
            }
            temp.addLast(20);
            oomages.add(new ComplexOomage(temp));
        }

        assertTrue(haveNiceHashCodeSpread(oomages));
    }

    /** Calls tests for SimpleOomage. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestComplexOomage.class);
    }
}
