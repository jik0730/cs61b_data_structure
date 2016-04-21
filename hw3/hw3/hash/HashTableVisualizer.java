package hw3.hash;

import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

public class HashTableVisualizer {

    public static void main(String[] args) {
        /* scale: StdDraw scale
           N:     number of items
           M:     number of buckets */

        double scale = 1.0;
        int N = 50;
        int M = 10;

        HashTableDrawingUtility.setScale(scale);
        Set<Oomage> oomies = new HashSet<Oomage>();
        for (int i = 0; i < N; i += 1) {
            oomies.add(ComplexOomage.randomComplexOomage());
        }
        visualize(oomies, M, scale);
    }

    public static void visualize(Set<Oomage> set, int M, double scale) {
        HashTableDrawingUtility.drawLabels(M);

        /* TODO: Create a visualization of the given hash table. Use
           du.xCoord and du.yCoord to figure out where to draw
           Oomages.
         */
        Object[] elements = set.toArray();
        int[] bucket = new int[M];

        for (int i = 0; i < set.size(); i++) {
            Oomage temp = (Oomage) elements[i];
            int hashcode = (temp.hashCode() & 0x7FFFFFFF) % M;
            temp.draw(HashTableDrawingUtility.xCoord(bucket[hashcode])
                    , HashTableDrawingUtility.yCoord(hashcode, M), scale);
            bucket[hashcode] += 1;
        }

//        for (Iterator<Oomage> itr = set.iterator(); itr.hasNext();) {
//            int hashcode = (itr.next().hashCode() & 0x7FFFFFFF) % M;
//            itr.next().draw(HashTableDrawingUtility.xCoord(bucket[hashcode])
//                    , HashTableDrawingUtility.yCoord(hashcode, M), scale);
//            bucket[hashcode] += 1;
//        }

        /* When done with visualizer, be sure to try 
           scale = 0.5, N = 2000, M = 100. */           
    }
} 
