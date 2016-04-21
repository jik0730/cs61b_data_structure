import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;

/**
 *  @author Josh Hug
 */

public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private Maze maze;
    private boolean targetFound = false;
    private Queue<Integer> queue;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        queue = new LinkedList<>();
    }

    /** Conducts a breadth first search of the maze starting at vertex x. */
    private void bfs(int s) {
        if (!marked[s]) {
            marked[s] = true;
            announce();
        }

        if (s == t) {
            targetFound = true;
        }

        if (targetFound) {
            return ;
        }

        for (int adj : maze.adj(s)) {
            if (!marked[adj]) {
                queue.add(adj);
                marked[adj] = true;
                edgeTo[adj] = s;
                distTo[adj] = distTo[s] + 1;
                announce();
            }
        }

        bfs(queue.remove());

    }


    @Override
    public void solve() {
        bfs(s);
    }
}

