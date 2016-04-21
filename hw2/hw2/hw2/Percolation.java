package hw2;                       

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // create class of each grid
    public class EachGrid {
        private boolean isOpen;
        private boolean isFull;
        private int index;

        public EachGrid() {
            isOpen = false;
            isFull = false;
        }
    }

    private EachGrid[][] grid;
    private EachGrid top;
    private EachGrid bottom;
    private int numberOfOpenSites;

    private WeightedQuickUnionUF union1;
    private WeightedQuickUnionUF union2;

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }

        union1 = new WeightedQuickUnionUF(N * N + 2);
        union2 = new WeightedQuickUnionUF(N * N + 1);
        grid = new EachGrid[N][];
        for (int i = 0; i < N; i++) {
            grid[i] = new EachGrid[N];
            for (int j = 0; j < N; j++) {
                grid[i][j] = new EachGrid();
                grid[i][j].index = (N * i) + j;
            }
        }

        top = new EachGrid();
        top.index = N * N;
        for (int i = 0; i < N; i++) {
            top.isFull = true;
            union1.union(top.index, grid[0][i].index);
            union2.union(top.index, grid[0][i].index);
        }

        bottom = new EachGrid();
        bottom.index = N * N + 1;
        for (int i = 0; i < N; i++) {
            union1.union(grid[N - 1][i].index, bottom.index);
        }

        numberOfOpenSites = 0;
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 0 || col < 0) {
            throw new IndexOutOfBoundsException();
        } else if (row >= grid.length || col >= grid[0].length) {
            throw new IndexOutOfBoundsException();
        } else if (!isOpen(row, col)) {
            grid[row][col].isOpen = true;
            numberOfOpenSites += 1;

            // make connection between neighbors
            // maybe occur exception -> does it cause operation forced to be terminated?  maybe not
            makeConnection(row, col);

            // full check
//            for (int i = 0; i < grid.length; i++) {
//                for (int j = 0; j < grid[0].length; j++) {
//                    boolean temp = this.isFull(i, j);
//                }
//            }

        } else {
            return;
        }
    }

    private void makeConnection(int row, int col) {
        if (row - 1 >= 0 && isOpen(row - 1, col) 
            && !union2.connected(grid[row][col].index, grid[row - 1][col].index)) {
            union1.union(grid[row][col].index, grid[row - 1][col].index);
            union2.union(grid[row][col].index, grid[row - 1][col].index);
        }
        if (row + 1 <= grid.length - 1 && isOpen(row + 1, col) 
            && !union2.connected(grid[row][col].index, grid[row + 1][col].index)) {
            union1.union(grid[row][col].index, grid[row + 1][col].index);
            union2.union(grid[row][col].index, grid[row + 1][col].index);
        }
        if (col - 1 >= 0 && isOpen(row, col - 1) 
            && !union2.connected(grid[row][col].index, grid[row][col - 1].index)) {
            union1.union(grid[row][col].index, grid[row][col - 1].index);
            union2.union(grid[row][col].index, grid[row][col - 1].index);
        }
        if (col + 1 <= grid[0].length - 1 && isOpen(row, col + 1) 
            && !union2.connected(grid[row][col].index, grid[row][col + 1].index)) {
            union1.union(grid[row][col].index, grid[row][col + 1].index);
            union2.union(grid[row][col].index, grid[row][col + 1].index);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 0 || col < 0) {
            throw new IndexOutOfBoundsException();
        } else if (row >= grid.length || col >= grid[0].length) {
            throw new IndexOutOfBoundsException();
        } else {
            return grid[row][col].isOpen;
        }
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 0 || col < 0) {
            throw new IndexOutOfBoundsException();
        } else if (row >= grid.length || col >= grid[0].length) {
            throw new IndexOutOfBoundsException();
        } else {
            if (union2.connected(top.index, grid[row][col].index) && grid[row][col].isOpen) {
                grid[row][col].isFull = true;
                return grid[row][col].isFull;
            }
            return grid[row][col].isFull;
        }
    }

    // check whether there is fulled neighbor around one
    // we can filter which is real fulled one or not
//    private boolean checkFulled(int row, int col) {
//        if (row - 1 >= 0) {
//            if (grid[row - 1][col].isFull) {
//                return true;
//            }
//        }
//        if (row + 1 <= grid.length - 1) {
//            if (grid[row + 1][col].isFull) {
//                return true;
//            }
//        }
//        if (col - 1 >= 0) {
//            if (grid[row][col - 1].isFull) {
//                return true;
//            }
//        }
//        if (col + 1 <= grid[0].length - 1) {
//            if (grid[row][col + 1].isFull) {
//                return true;
//            }
//        }
//        return false;
//    }

    // number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        if (grid.length == 1) {
            if (grid[0][0].isOpen) {
                return true;
            }
            return false;
        }

        if (union1.connected(top.index, bottom.index)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {

    }
}
