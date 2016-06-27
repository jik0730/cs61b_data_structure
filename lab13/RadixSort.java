/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra
 * @version 1.4 - April 14, 2016
 *
 **/
public class RadixSort
{

    /**
     * Does Radix sort on the passed in array with the following restrictions:
     *  The array can only have ASCII Strings (sequence of 1 byte characters)
     *  The sorting is stable and non-destructive
     *  The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     **/
    public static String[] sort(String[] asciis)
    {
        String[] toReturn = new String[asciis.length];
        int longestStringLength = 1;

        for (int i = 0; i < asciis.length; i += 1) {
            toReturn[i] = asciis[i];
            if (longestStringLength < asciis[i].length()) {
                longestStringLength = asciis[i].length();
            }
        }


        int index = 0;

        while (longestStringLength != 0) {
            String[] temp = new String[toReturn.length];
            int[] sortHelperArray = new int[256];

            for (String s : toReturn) {
                if (s.length() < longestStringLength) {
                    sortHelperArray[0] += 1;
                } else {
                    sortHelperArray[(int) s.charAt(longestStringLength - 1)] += 1;
                }
            }

            for (int i = 1; i < sortHelperArray.length; i += 1) {
                sortHelperArray[i] += sortHelperArray[i - 1];
            }

            for (int i = toReturn.length - 1; i >= 0; i -= 1) {
                if (toReturn[i].length() < longestStringLength) {
                    temp[sortHelperArray[0] - 1] = toReturn[i];
                    sortHelperArray[0] -= 1;
                } else {
                    temp[sortHelperArray[(int) toReturn[i].charAt(longestStringLength - 1)] - 1] = toReturn[i];
                    sortHelperArray[(int) toReturn[i].charAt(longestStringLength - 1)] -= 1;
                }
            }

            toReturn = temp;

            longestStringLength -= 1;
        }

        return toReturn;
    }

    /**
     * Radix sort helper function that recursively calls itself to achieve the sorted array
     *  destructive method that changes the passed in array, asciis
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelper(String[] asciis, int start, int end, int index)
    {
        //TODO use if you want to
    }

    public static void main(String[] args) {
        String[] s = new String[] {"zebra", "cat", "apple", "airplane", "apples", "zebraaes"};

        String[] s2 = sort(s);

        System.out.print("Original strings:");
        for (String t : s) {
            System.out.print(" " + t);
        }

        System.out.print("\nSorted strings:");
        for (String t : s2) {
            System.out.print(" " + t);
        }

    }
}
