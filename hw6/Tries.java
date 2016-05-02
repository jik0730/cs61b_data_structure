import java.util.HashMap;
import java.util.List;

/**
 * Created by Ingyo on 2016. 4. 30..
 */
public class Tries {
    private Node root;

    /** Each element of hash map tries. */
    private class Node {
        private boolean exists;
        private HashMap<Character, Node> hm;

        private Node() {
            this.hm = new HashMap<>();
        }

        private Node(boolean exists) {
            this.exists = exists;

            if (this.hm == null) {
                this.hm = new HashMap<>();
            }
        }

        private void putChar(char c, boolean exists) {
            this.hm.put(c, new Node(exists));
        }

        private Node getValue(char c) {
            return this.hm.get(c);
        }

    }

    /** Getting string list of a dictionary as an argument,
     * and make a Tries structure. */
    public Tries(List<String> dic) {

        /** Initialize root. */
        root = new Node();

        /** Iterate all of elements in a dictionary. */
        for (String s : dic) {
            Node temp = root;
            for (int i = 0; i < s.length(); i += 1) {
                char c = s.charAt(i);

                if (temp.hm.containsKey(c)) {
                    if (i == s.length() - 1) {
                        temp.getValue(c).exists = true;
                    }
                } else {
                    temp.putChar(c, i == s.length() - 1);
                }

                temp = temp.getValue(c);
            }
        }
        String a = "a";

    }

    public boolean inspectTheWordExist(String word) {
        Node temp = root;
        for (int i = 0; i < word.length(); i += 1) {
            if (!temp.hm.containsKey(word.charAt(i))) {
                break;
            }

            temp = temp.hm.get(word.charAt(i));

            if (i == word.length() - 1 && temp.exists) {
                return true;
            }
        }

        return false;
    }

    public boolean inspectTheWordPossiblyExist(String word) {
        Node temp = root;
        for (int i = 0; i < word.length(); i += 1) {
            if (!temp.hm.containsKey(word.charAt(i))) {
                break;
            }
            if (i == word.length() - 1) {
                return true;
            }
            temp = temp.hm.get(word.charAt(i));
        }

        return false;
    }
}
