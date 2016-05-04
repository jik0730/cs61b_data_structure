import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Ingyo on 2016. 4. 30..
 */
public class PlayBoggle {
    private Board board;
    private Tries tries;
    private HashSet<String> words;
    private boolean marked[][];

    /** Construct board and tries. */
    public PlayBoggle(List<String> boardList, List<String> triesList) {
        this.board = new Board(boardList);
        this.tries = new Tries(triesList);
        this.words = new HashSet<>();

        this.marked = new boolean[board.getyLength()][];
        for (int k = 0; k < board.getyLength(); k += 1) {
            this.marked[k] = new boolean[board.getxLength()];
        }
    }

    /** Play Boggle Game! */
    public List<String> play() {
        char[][] board = this.board.getBoard();

        for (int i = 0; i < board.length; i += 1) {
            for (int j = 0; j < board[i].length; j += 1) {
                // playForeach(board, i, j, words);
                Node cur = new Node(i, j, Character.toString(board[i][j]), this.board);
                newBoggleAlgorithm(cur, board, words);
            }
        }

        return new ArrayList<>(words);
    }

    /** New Boggle algorithm. */
    private void newBoggleAlgorithm(Node currentNode, char[][] board, HashSet<String> result) {

        for (Node n : adj(currentNode, board, result)) {
            marked[currentNode.getI()][currentNode.getJ()] = true;
            newBoggleAlgorithm(n, board, result);
            marked[currentNode.getI()][currentNode.getJ()] = false;
        }
    }

    /** New adj algorithm for new Boggle algorithm. */
    private HashSet<Node> adj(Node n, char[][] board, HashSet<String> result) {
        HashSet<Node> toReturn = new HashSet<>();
        String ori = n.getS();
        int i1 = n.getI() - 1;
        int j1 = n.getJ() - 1;
        int i2 = n.getI() + 1;
        int j2 = n.getJ() + 1;

        if (i1 == -1) {
            i1 = n.getI();
        }
        if (j1 == -1) {
            j1 = n.getJ();
        }
        if (i2 == this.board.getyLength()) {
            i2 = n.getI();
        }
        if (j2 == this.board.getxLength()) {
            j2 = n.getJ();
        }

        for (int m = i1; m <= i2; m += 1) {
            for (int n2 = j1; n2 <= j2; n2 += 1) {
                if (m == n.getI() && n2 == n.getJ()) {
                    continue;
                } else if (/*n.*/marked[m][n2]) {
                    continue;
                } else {
                    String tempS = ori + Character.toString(board[m][n2]);
                    if (tries.inspectTheWordPossiblyExist(tempS)) {
                        if (tries.inspectTheWordExist(tempS)) {
                            result.add(tempS);
                        }
                        Node temp = new Node(m, n2, tempS, n);
                        toReturn.add(temp);
                    }
                }
            }
        }

        return toReturn;
    }

    /** Boggle algorithm for each character. */
    /** Secondary Boggle Algorithm. */
    private void playForeach(char[][] board, int i, int j, HashSet<String> result) {
        HashSet<Node> search = new HashSet<>();
        search.add(new Node(i, j, Character.toString(board[i][j]), this.board));

        /** 1. Make a hash set which is to search every possible word.
         *  2. Add initial letter.
         *  3. Add all possible words into the hash set by using 'combineWithAdj' method(Filtering by if phrase).
         *  4. If we find a real word, put that into result hash set which is collection of words that we found.
         *  5. Iterate until the hash set becomes empty. */
        while (!search.isEmpty()) {
            Object[] allSet = search.toArray();
            HashSet<Node> toAdd = new HashSet<>();
            for (Node n : search) {

                /** Two different word existing tests. */
                boolean exists = tries.inspectTheWordPossiblyExist(n.getS());
                boolean wordExists = tries.inspectTheWordExist(n.getS());

                if (!exists) {

                } else if (exists && !wordExists) {
                    toAdd = combineWithAdj(search, board);
                } else if (exists && wordExists) {
                    result.add(n.getS());
                    toAdd = combineWithAdj(search, board);
                }
            }

            /** Add combined adjs. */
            for (Node a : toAdd) {
                search.add(a);
            }

            /** Remove original potential words. */
            for (Object n : allSet) {
                search.remove(n);
            }
        }
    }

    /** Combine words with each adj letters(Not remove original one). */
    private HashSet<Node> combineWithAdj(HashSet<Node> search, char[][] board) {
        if (search.isEmpty()) {
            return null;
        }

        HashSet<Node> toReturn = new HashSet<>();
        for (Node n : search) {
            String ori = n.getS();
            int i1 = n.getI() - 1;
            int j1 = n.getJ() - 1;
            int i2 = n.getI() + 1;
            int j2 = n.getJ() + 1;

            if (i1 == -1) {
                i1 = n.getI();
            }
            if (j1 == -1) {
                j1 = n.getJ();
            }
            if (i2 == this.board.getyLength()) {
                i2 = n.getI();
            }
            if (j2 == this.board.getxLength()) {
                j2 = n.getJ();
            }

            for (int m = i1; m <= i2; m += 1) {
                for (int n2 = j1; n2 <= j2; n2 += 1) {
                    if (m == n.getI() && n2 == n.getJ()) {
                        continue;
                    } else if (/*n.*/marked[m][n2]) {
                        continue;
                    } else {
                        String tempS = ori + Character.toString(board[m][n2]);
                        if (tries.inspectTheWordPossiblyExist(tempS)) {
                            Node temp = new Node(m, n2, tempS, n);
                            toReturn.add(temp);
                        }
                    }
                }
            }
        }

        return toReturn;
    }

    /** The parts annotated are for old version of boggle algorithm. */
    private class Node {
        private int i;
        private int j;
        private String s;
        //private boolean[][] marked;

        private Node(int i, int j, String s, Node prev) {
            this.i = i;
            this.j = j;
            this.s = s;
//            this.marked = new boolean[prev.marked.length][];
//            for (int k = 0; k < prev.marked.length; k += 1) {
//                this.marked[k] = new boolean[prev.marked[k].length];
//                for (int q = 0; q < this.marked[k].length; q += 1) {
//                    this.marked[k][q] = prev.marked[k][q];
//                }
//            }
//            this.marked[i][j] = true;
        }

        private Node(int i , int j, String s, Board board) {
            this.i = i;
            this.j = j;
            this.s = s;
//            this.marked = new boolean[board.getyLength()][];
//            for (int k = 0; k < board.getyLength(); k += 1) {
//                this.marked[k] = new boolean[board.getxLength()];
//            }
//            this.marked[i][j] = true;
        }

        public int getI() {
            return this.i;
        }

        public int getJ() {
            return this.j;
        }

        public String getS() {
            return this.s;
        }
    }
}
