import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        if (args.length == 1) {
            RandomizedQueue<String> queue = new RandomizedQueue<String>();
            while (!StdIn.isEmpty()) {
                queue.enqueue(StdIn.readString());
            }
            int kLength = Integer.parseInt(args[0]);
            for (int i = 0; i < kLength; i++) {
                StdOut.println(queue.dequeue());
            }
        }
    }
}