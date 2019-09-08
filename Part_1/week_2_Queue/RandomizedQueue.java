import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.NoSuchElementException;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int len;
    private Node firstNode;
    private Node lastNode;

    private class Node {
        Node nextNode;
        Item item;
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        len = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() { return len == 0; }

    // return the number of items on the randomized queue
    public int size() { return len; }

    // add the item
    public void enqueue(Item item) {
        if (item == null) { throw new IllegalArgumentException(); }
        if (lastNode != null) {
            Node oldLast = lastNode;
            lastNode = new Node();
            lastNode.item = item;
            oldLast.nextNode = lastNode;
        } else {
            lastNode = new Node();
            lastNode.item = item;
            firstNode = lastNode;
        }
        ++len;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) { throw new NoSuchElementException(); }
        int randNumber = StdRandom.uniform(len) - 1;
        Node start = firstNode;
        Node prevNode = start;
//        StdOut.println("++++++++++++++++++++++ START ++++++++++++++++++++++++++");
//        StdOut.printf("len = %d\n", len);
//        StdOut.printf("rnd = %d\n", randNumber);
        for (int i = 0; i < randNumber; i++) {
            prevNode = start;
            start = start.nextNode;
//            StdOut.printf("itr = %d\n", i);
        }
//        StdOut.println("++++++++++++++++++++++ END ++++++++++++++++++++++++++++");
        if (firstNode != start) {
            prevNode.nextNode = start.nextNode;
        } else if (firstNode == start) {
            firstNode = start.nextNode;
            if (firstNode == null) {
                lastNode = null;
            }
        }
        --len;
        return start.item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) { throw new NoSuchElementException(); }
        int randNumber = StdRandom.uniform(len);
        Node start = firstNode;
        for (int i = 0; i < randNumber; i++) {
            start = start.nextNode;
        }
        return start.item;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() { return new ArrayIterator(); }

    private class ArrayIterator implements Iterator<Item>
    {
        private Node current = firstNode;
        public boolean hasNext() { return current != null; }
        public void remove() { throw new UnsupportedOperationException(); }
        public Item next() {
            if (!hasNext()) { throw new NoSuchElementException(); }
            Node prevCurrent = current;
            current = current.nextNode;
            return prevCurrent.item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
//        RandomizedQueue<String> queue = new RandomizedQueue<String>();
//        queue.enqueue("Roman");
//        queue.enqueue("Bohdan");
//        queue.enqueue("Hanna");
//        StdOut.println("After enqueue");
//        Iterator<String> iterator1 = queue.iterator();
//        while (iterator1.hasNext()) {
//            StdOut.printf("iterator1.next() => %s\n", iterator1.next());
//        }
//        StdOut.printf("queue.dequeue() => %s\n", queue.dequeue());
//        StdOut.printf("queue.dequeue() => %s\n", queue.dequeue());
//        StdOut.println("After dequeue");
//        Iterator<String> iterator2 = queue.iterator();
//        while (iterator2.hasNext()) {
//            StdOut.printf("iterator2.next() => %s\n", iterator2.next());
//        }
//        StdOut.printf("queue.sample() => %s\n", queue.sample());
//        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
//        rq.isEmpty();
//        rq.isEmpty();
//        rq.enqueue(5);
//        StdOut.println(rq.dequeue());
//        rq.enqueue(0);
//        StdOut.println(rq.dequeue());
//        Iterator<Integer> iter = rq.iterator();
//        while (iter.hasNext()) {
//            StdOut.printf("iter.next() => %s\n", iter.next());
//        }
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        rq.isEmpty();
        rq.enqueue(1);
        rq.dequeue();
        rq.isEmpty();
        rq.enqueue(42);
        rq.size();
        rq.enqueue(2);
        rq.dequeue();
        rq.enqueue(3);
        rq.enqueue(4);
        Iterator<Integer> iter = rq.iterator();
        while (iter.hasNext()) {
            StdOut.printf("iter.next() => %s\n", iter.next());
        }
        rq.dequeue();
        rq.dequeue();
    }
}

