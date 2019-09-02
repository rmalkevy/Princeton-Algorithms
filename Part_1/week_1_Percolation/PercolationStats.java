import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private int length;
    private int trials;
    private int numberOfSites;

    private static final double CONFIDENCE_95 = 1.96;
    private double[] fractions;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int inTrials) {
        throwIllegalArgumentException(n, inTrials);

        length = n;
        trials = inTrials;
        numberOfSites = length * length;
        this.fractions = new double[trials];

        for (int i = 0; i < trials; i++) {
            this.calculateFraction(i);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(this.fractions);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(this.fractions);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double meanRes = StdStats.mean(this.fractions);
        double stddevRes = StdStats.stddev(this.fractions);
        return meanRes - (CONFIDENCE_95 * stddevRes) / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double meanRes = StdStats.mean(this.fractions);
        double stddevRes = StdStats.stddev(this.fractions);
        return meanRes + (CONFIDENCE_95 * stddevRes) / Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        if (args.length == 2) {
            int nLength = Integer.parseInt(args[0]);
            int trials = Integer.parseInt(args[1]);
            PercolationStats percolationStats = new PercolationStats(nLength, trials);
            StdOut.printf("mean                    = %f\n", percolationStats.mean());
            StdOut.printf("stddev                  = %f\n", percolationStats.stddev());
            StdOut.printf("95%% confidence interval = [%f, %f]\n", percolationStats.confidenceLo(), percolationStats.confidenceHi());
        }
    }

    private void calculateFraction(int trialIndex) {
        Percolation percolation = new Percolation(length);
        int randomNumber = 0;
        int row = 0;
        int col = 0;

        while (!percolation.percolates()) {
            randomNumber = StdRandom.uniform(0, numberOfSites);
            row = randomNumber / length + 1;
            col = randomNumber + length - row * length + 1;
            percolation.open(row, col);
        }
        if (percolation.percolates()) {
            this.fractions[trialIndex] = percolation.numberOfOpenSites() / (double)numberOfSites;
        }
    }

    private void throwIllegalArgumentException(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
    }
}
