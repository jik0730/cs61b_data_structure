import java.util.Observable;
/** 
 *  @author Josh Hug
 */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class MazeCycles extends MazeExplorer {
    /* Inherits public fields: 
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private Maze maze;
    private WeightedQuickUnionUF union;
    private boolean cycleFound = false;

    public MazeCycles(Maze m) {
        super(m);
        maze = m;
        edgeTo[0] = 0;
        union = new WeightedQuickUnionUF(maze.V());
    }

    public void mc(int c) {
        marked[c] = true;
        announce();

        if (cycleFound) {
            return ;
        }

        for (int i : maze.adj(c)) {
            if (union.connected(c, i) && i != edgeTo[c]) {
                cycleFound = true;
            }

            if (cycleFound) {
                return ;
            }

            if (!marked[i]) {
                marked[i] = true;
                union.union(i, c);
                edgeTo[i] = c;
                announce();
                mc(i);
            }
        }
    }

    @Override
    public void solve() {
        mc(0);
    }
} 

