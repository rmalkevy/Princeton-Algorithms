import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Board {

    private class Coordinate {
        public int x = 0;
        public int y = 0;

        public Coordinate(int y, int x) {
            this.y = y;
            this.x = x;
        }
    }

    final private short[][] grid;
    final private int size;
    final private int hammingDistance;
    final private int manhattanDistance;
    private boolean isBoardGoal = true;
    private Coordinate zeroCoordinate;
  
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException();
        }

        size = tiles.length;
        grid = new short [size][size];
        int sequenceNumber = 0;
        int sequenceLength = size * size;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int value = tiles[y][x];
                grid[y][x] = (short)value;
                sequenceNumber++;
                if (isBoardGoal && sequenceNumber != value && sequenceNumber != sequenceLength) {
                    isBoardGoal = false;
                }
                if (value == 0) {
                    zeroCoordinate = new Coordinate(y, x);
                }
            }
        }

        hammingDistance = computeHammingDistance();
        manhattanDistance = cumputeManhattenDistance();
    }
                                           
    // string representation of this board
    public String toString() {
        StringBuilder gridString = new StringBuilder();
        gridString.append(size).append("\n");

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                gridString.append(grid[y][x]);
                if (x < size) {
                    gridString.append(" ");
                }
            }
            gridString.append("\n");
        }
        return gridString.toString();
    }

    // board dimension n
    public int dimension() {
        return size;
    }

    // number of tiles out of place
    public int hamming() {
        return hammingDistance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return isBoardGoal;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (this == y) return true;
        if (y.getClass() != this.getClass()) return false;

        final Board that = (Board) y;
        if (that.size != this.size) return false;
        if (that.hammingDistance != this.hammingDistance) return false;
        if (that.manhattanDistance != this.manhattanDistance) return false;
        return Arrays.deepEquals(that.grid, this.grid);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> ownNeighbors = new ArrayList<Board>();

        // LEFT
        if (zeroCoordinate.x > 0) {
            int[][] neighbor = copyGrid();
            int tempValue = neighbor[zeroCoordinate.y][zeroCoordinate.x - 1];
            neighbor[zeroCoordinate.y][zeroCoordinate.x - 1] = 0;
            neighbor[zeroCoordinate.y][zeroCoordinate.x] = tempValue;
            ownNeighbors.add(new Board(neighbor));
        }
        // RIGHT
        if (zeroCoordinate.x < size - 1) {
            int[][] neighbor = copyGrid();
            int tempValue = neighbor[zeroCoordinate.y][zeroCoordinate.x + 1];
            neighbor[zeroCoordinate.y][zeroCoordinate.x + 1] = 0;
            neighbor[zeroCoordinate.y][zeroCoordinate.x] = tempValue;
            ownNeighbors.add(new Board(neighbor));
        }
        // TOP
        if (zeroCoordinate.y > 0) {
            int[][] neighbor = copyGrid();
            int tempValue = neighbor[zeroCoordinate.y - 1][zeroCoordinate.x];
            neighbor[zeroCoordinate.y - 1][zeroCoordinate.x] = 0;
            neighbor[zeroCoordinate.y][zeroCoordinate.x] = tempValue;
            ownNeighbors.add(new Board(neighbor));
        }
        // BOTTOM
        if (zeroCoordinate.y < size - 1) {
            int[][] neighbor = copyGrid();
            int tempValue = neighbor[zeroCoordinate.y + 1][zeroCoordinate.x];
            neighbor[zeroCoordinate.y + 1][zeroCoordinate.x] = 0;
            neighbor[zeroCoordinate.y][zeroCoordinate.x] = tempValue;
            ownNeighbors.add(new Board(neighbor));
        }
        return ownNeighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twinBoard = copyGrid();

        int x = 0, y = 0, x2 = 1, y2 = 1;
        if (twinBoard[y][x] == 0) x++;
        if (twinBoard[y2][x2] == 0) x2--;
        short tempValue = (short) twinBoard[y][x];
        twinBoard[y][x] = twinBoard[y2][x2];
        twinBoard[y2][x2] = tempValue;

        return new Board(twinBoard);
    }

    private int[] findCoordinate(short tileNumber) {
        int[] coordinates = new int[2];
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (grid[y][x] == tileNumber) {
                    coordinates[0] = y;
                    coordinates[1] = x;
                    return coordinates;
                }
            }
        }
        return coordinates;
    }

    private int computeHammingDistance() {
        short distance = 0;
        short goalValue = 0;
        int sequenceLength = size * size;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                goalValue++;
                if (goalValue == sequenceLength) goalValue = 0;
                if (grid[y][x] == 0 || grid[y][x] == goalValue) continue;
                if (grid[y][x] != goalValue) distance += 1;
            }
        }
        return distance;
    }

    private int cumputeManhattenDistance() {
        int distance = 0;
        short goalValue = 0;
        int sequenceLength = size * size;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                goalValue++;
                if (goalValue == sequenceLength) continue;
                int[] coordinate = findCoordinate(goalValue);
                distance += Math.abs(y - coordinate[0]) + Math.abs(x - coordinate[1]);
            }
        }
        return distance;
    }

    private int[][] copyGrid() {
        int[][] copiedGrid = new int[size][size];

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                copiedGrid[y][x] = grid[y][x];
            }
        }
        return copiedGrid;
    }

    // unit testing (not graded)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
    
        // solve the puzzle
        Solver solver = new Solver(initial);
    
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
