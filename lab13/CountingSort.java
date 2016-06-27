/**
 * Class with 2 ways of doing Counting sort, one naive way and one "better" way
 * 
 * @author 	Akhil Batra
 * @version	1.1 - April 16, 2016
 * 
**/
public class CountingSort {
    
    /**
     * Counting sort on the given int array. Returns a sorted version of the array.
     *  does not touch original array (non-destructive method)
     * DISCLAIMER: this method does not always work, find a case where it fails
     *
     * @param arr int array that will be sorted
     * @return the sorted array
    **/
    public static int[] naiveCountingSort(int[] arr) {
        // find max
        int max = Integer.MIN_VALUE;
        for (int i : arr) {
            if (i > max) {
                max = i;
            }
        }

        // gather all the counts for each value
        int[] counts = new int[max + 1];
        for (int i : arr) {
            counts[i] += 1;
        }

        // put the value count times into a new array
        int[] sorted = new int[arr.length];
        int k = 0;
        for (int i = 0; i < counts.length; i += 1) {
            for (int j = 0; j < counts[i]; j += 1, k += 1) {
                sorted[k] = i;
            }
        }

        // return the sorted array
        return sorted;
    }

    /**
     * Counting sort on the given int array, must work even with negative numbers.
     * Note, this code does not need to work for ranges of numbers greater
     * than 2 billion.
     *  does not touch original array (non-destructive method)
     * 
     * @param toSort int array that will be sorted
    **/
    public static int[] betterCountingSort(int[] toSort) {
        //TODO make it work with arrays containing negative numbers.
        // find max & min
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int i : toSort) {
            if (i > max) {
                max = i;
            }
            if (i < min) {
                min = i;
            }
        }

        // gather all the counts for each value
        int[] counts = new int[max + Math.abs(min) + 1];
        for (int i : toSort) {
            if (i < 0) {
                counts[max - i] += 1;
            } else {
                counts[i] += 1;
            }
        }

        // put the value count times into a new array
        int[] sorted = new int[toSort.length];
        int k = 0;
        int l = toSort.length - 1;
        for (int i = counts.length - 1; i >= 0; i -= 1) {
            for (int j = 0; j < counts[i]; j += 1, k += 1) {
                if (i > max) {
                    sorted[k] = - i + max;
                } else {
                    sorted[k] = max - i;
                }
            }
        }

        return sorted;
    }
}
