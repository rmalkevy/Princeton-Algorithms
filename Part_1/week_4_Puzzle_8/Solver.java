import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Solver {

    private boolean isBoardSolvable = true;
    private int moves = 0;
    private Stack<Board> solution;
    
    private class Node implements Comparable<Node> {
        final private Board board;
        final private int moves;
        final private int priority;
        final private Node previous;
        
        private Node(Board board, Node previousNode) {
            this.board = board;
            previous = previousNode;
            if (previousNode != null) {
                moves = previousNode.moves + 1;
            }
            else {
                moves = 0;
            }
            priority = this.board.manhattan() + moves;
        }
        
        public int compareTo(Node that) {
            if (this.priority > that.priority) return 1;
            if (this.priority < that.priority) return -1;
            // if (this.board.manhattan() > that.board.manhattan()) return 1;
            // if (this.board.manhattan() < that.board.manhattan()) return -1;
            return 0;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
    
        MinPQ<Node> search = new MinPQ<Node>();
        Node initialNode = new Node(initial, null);
        search.insert(initialNode);
        Node initialTwinNode = new Node(initial.twin(), null);
        search.insert(initialTwinNode);
        
        while (true) {
            Node min = search.delMin();
            moves = min.moves;
            
            if (min.board.isGoal()) {
                
                solution = new Stack<Board>();
                while (min != null) {
                    solution.push(min.board);
                    
                    if (min.board == initialTwinNode.board) {
                        isBoardSolvable = false;
                    }
                    min = min.previous;
                }
                break;
            }
            
            for (Board neighborBoard : min.board.neighbors()) { 
                if (min.previous == null || !neighborBoard.equals(min.previous.board)) {
                    Node searchNode = new Node(neighborBoard, min);
                    search.insert(searchNode);   
                }
            }
            
        }
        
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isBoardSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isBoardSolvable) return -1;
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isBoardSolvable) return null;
        return solution;
    }

    // test client (see below) 
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
