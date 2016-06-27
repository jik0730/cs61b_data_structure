import org.junit.Test;
import static org.junit.Assert.*;
import edu.princeton.cs.algs4.Queue;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Ingyo on 2016. 4. 22..
 */
public class QuickSortTest {

    @Test
    public void testBasicQuickSort() {
        Queue<String> queue = new Queue<>();
        String[] toBeSorted = new String[] {"Ethan", "Alice", "Vanessa"};
        queue.enqueue("Ethan");
        queue.enqueue("Alice");
        queue.enqueue("Vanessa");

        Queue<String> actual = QuickSort.quickSort(queue);
        Arrays.sort(toBeSorted);
        System.out.print(actual);

        int temp = 0;
        for (String s : actual) {
            assertTrue(s.equals(toBeSorted[temp]));
            temp += 1;
        }

    }

    @Test
    public void testAdvancedQuickSort() {
        for (int j = 0; j < 10; j += 1) {
            Random seed = new Random();

            Queue<Integer> queue = new Queue<>();
            int[] toBeSorted = new int[100];

            for (int i = 0; i < 100; i += 1) {
                int toInsert = seed.nextInt();
                queue.enqueue(toInsert);
                toBeSorted[i] = toInsert;
            }

            Queue<Integer> actual = QuickSort.quickSort(queue);
            Arrays.sort(toBeSorted);

            int temp = 0;
            for (int s : actual) {
                assertTrue(s == toBeSorted[temp]);
                temp += 1;
            }
        }
    }


    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", QuickSortTest.class);
    }
}
