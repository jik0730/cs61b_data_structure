package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private Percolation percolationTest;
    private double[] means;
    private int numberOfExperiments;
    private int numberOfGrids;

    //perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        
        means = new double[T];
        numberOfExperiments = T;
        numberOfGrids = N * N;
        for (int i = 0; i < T; i++) {
            percolationTest = new Percolation(N);
            means[i] = (double) percolationTest(percolationTest, N) / (double) numberOfGrids;
        }
    }

    private int percolationTest(Percolation p, int N) {
        int row, col;
        while (!p.percolates()) {
            row = StdRandom.uniform(N);
            col = StdRandom.uniform(N);

            // open one site
            // if one is already open, then try the other one
            if (!p.isOpen(row, col)) {
                p.open(row, col);
            }
        }
        return p.numberOfOpenSites();
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(means);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(means);
    }
 
    // low  endpoint of 95% confidence interval
    public double confidenceLow() {
        return this.mean() - ((1.96 * this.stddev()) / Math.sqrt(numberOfExperiments));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return this.mean() + ((1.96 * this.stddev()) / Math.sqrt(numberOfExperiments));
    }
}
