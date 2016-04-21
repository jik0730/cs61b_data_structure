package hw4.puzzle;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private class Node implements Comparable<Node> {
        private Board tile;
        private int moves;
        private int priority;
        private Node prev;

        Node(Board tiles, Node prev2) {
            tile = tiles;
            if (prev2 == null) {
                moves = 0;
            } else {
                moves = prev2.moves + 1;
                prev = prev2;
            }
            priority = moves + tile.manhattan();
        }

        public int compareTo(Node o) {
            if (this.priority > o.priority) {
                return 1;
            } else if (this.priority < o.priority) {
                return -1;
            }
            return 0;
        }
    }

    MinPQ<Node> searchNodes = new MinPQ<>();
    public Solver(Board initial) {
        searchNodes.insert(new Node(initial, null));

        while (!searchNodes.min().tile.isGoal()) {
            makeChildren(searchNodes);
        }
    }

    private void makeChildren(MinPQ<Node> k) {
        Node temp = k.delMin();
        for (Board neighbor : BoardUtils.neighbors(temp.tile)) {
            if (temp.prev == null) {
                k.insert(new Node(neighbor, temp));
            } else if (!temp.prev.tile.equals(neighbor)) {
                k.insert(new Node(neighbor, temp));
            }
        }
    }

    public int moves() {
        return searchNodes.min().moves;
    }

    public Iterable<Board> solution() {
        Stack<Board> toReturn = new Stack<>();
        Node temp = searchNodes.min();
        while (temp.prev != null) {
            toReturn.push(temp.tile);
            temp = temp.prev;
        }
        toReturn.push(temp.tile);
        return toReturn;
    }

    /** Main method. */
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);
        Solver solver = new Solver(initial);
        StdOut.println("Minimum number of moves = " + solver.moves());
        for (Board board : solver.solution()) {
            StdOut.println(board);
        }
    }
}
