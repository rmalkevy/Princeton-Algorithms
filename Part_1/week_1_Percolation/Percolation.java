import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdOut;

public class Percolation {
    private WeightedQuickUnionUF weightedQuickUnionUF;
    private WeightedQuickUnionUF backwashUF;
    private int length;
    private int numberOfOpenedSites;
    private int topVirtualNode;
    private int bottomVirtualNode;
    private boolean[][] grid;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        throwIllegalArgumentException(n);

        grid = new boolean[n][n];
        weightedQuickUnionUF = new WeightedQuickUnionUF(n * n + 2); // 2 virtual sites
        backwashUF = new WeightedQuickUnionUF(n * n + 1);
        length = n;
        numberOfOpenedSites = 0;
        topVirtualNode = n * n;
        bottomVirtualNode = n * n + 1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        throwIllegalArgumentException(row, col);
        int privRow = row - 1;
        int privCol = col - 1;

        if (this.isOpen(row, col)) { return; }

        grid[privRow][privCol] = true;
        numberOfOpenedSites++;

        int p = privRow * length + privCol;
        if (this.isLeftSiteOpen(row, col)) {
            int q = p - 1;
            this.connectTwoSites(p, q);
        }
        if (this.isRightSiteOpen(row, col)) {
            int q = p + 1;
            this.connectTwoSites(p, q);
        }
        if (this.isTopSiteOpen(row, col)) {
            int q = (privRow - 1) * length + privCol;
            this.connectTwoSites(p, q);
        }
        if (this.isBottomSiteOpen(row, col)) {
            int q = (privRow + 1) * length + privCol;
            this.connectTwoSites(p, q);
        }

        // connect to the virtual top node
        if (row == 1) {
            weightedQuickUnionUF.union(topVirtualNode, p);
            backwashUF.union(topVirtualNode, p);
        }
        // connect to the bottom node
        if (row == length) {
            weightedQuickUnionUF.union(bottomVirtualNode, p);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        throwIllegalArgumentException(row, col);
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        throwIllegalArgumentException(row, col);

        if (!this.isOpen(row, col)) { return false; }
        int site = (row - 1) * length + col - 1;
        return backwashUF.connected(site, topVirtualNode);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenedSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return weightedQuickUnionUF.connected(topVirtualNode, bottomVirtualNode);
    }

    // test client (optional)
    public static void main(String[] args) {
        if (args.length == 1) {
            Percolation percolation = new Percolation(Integer.parseInt(args[0]));
        }
    }

    private void connectTwoSites(int p, int q) {
        if (!weightedQuickUnionUF.connected(p, q)) {
            weightedQuickUnionUF.union(p, q);
        }
        if (!backwashUF.connected(p, q)) {
            backwashUF.union(p, q);
        }
    }

    private boolean isLeftSiteOpen(int row, int col) {
        return col - 1 - 1 >= 0 && this.isOpen(row, col - 1);
    }

    private boolean isRightSiteOpen(int row, int col) {
        return col + 1 - 1 < length && this.isOpen(row, col + 1);
    }

    private boolean isTopSiteOpen(int row, int col) {
        return row - 1 - 1 >= 0 && this.isOpen(row - 1, col);
    }

    private boolean isBottomSiteOpen(int row, int col) {
        return row + 1 - 1 < length && this.isOpen(row + 1, col);
    }

    private void throwIllegalArgumentException(int row, int col) {
        if (!(row > 0 && row <= length && col > 0 && col <= length)) {
            throw new IllegalArgumentException();
        }
    }

    private void throwIllegalArgumentException(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
    }
}
