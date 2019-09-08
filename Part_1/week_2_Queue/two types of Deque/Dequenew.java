import edu.princeton.cs.algs4.StdOut;
import java.util.NoSuchElementException;
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private int len;
    private Node firstNode;
    private Node lastNode;

    private class Node {
        Node nextNode;
        Node prevNode;
        Item item;
    }

    // construct an empty deque
    public Deque() { len = 0; }

    // is the deque empty?
    public boolean isEmpty() { return len == 0; }

    // return the number of items on the deque
    public int size() { return len; }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) { throw new IllegalArgumentException(); }
        ++len;
        if (firstNode != null) {
            newNode = new Node();
            newNode.item = item;
            newNode.nextNode = firstNode;
            firstNode.prevNode = newNode;
            firstNode = newNode;
        } else {
            firstNode = new Node();
            firstNode.item = item;
        }
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) { throw new IllegalArgumentException(); }
        ++len;
        if (lastNode != null) {
            newNode = new Node();
            newNode.item = item;
            lastNode.nextNode = newNode;
            newNode.prevNode = lastNode;
            lastNode = newNode;
        } else {
            lastNode = new Node();
            lastNode.item = item;
            firstNode.nextNode = lastNode;
            lastNode.prevNode = firstNode;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (len <= 0) { throw new NoSuchElementException(); }
        --len;
        Item item = firstNode.item;
        firstNode = firstNode.nextNode;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (len <= 0) { throw new NoSuchElementException(); }
        --len;
        Item item = lastNode.item;
        lastNode = lastNode.prevNode;
        lastNode.nextNode = null;
        return item;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> itemDeque = new Deque<String>();
        itemDeque.addFirst("_11_");
        itemDeque.addLast("_21_");
        itemDeque.addFirst("_12_");
        itemDeque.addLast("_22_");
        itemDeque.removeFirst();
        itemDeque.removeLast();
        itemDeque.addFirst("_31_");
        itemDeque.addLast("_41_");
        itemDeque.addFirst("_32_");
        itemDeque.addLast("_42_");
        itemDeque.addFirst("_51_");
        itemDeque.addLast("_61_");
        itemDeque.addFirst("_52_");
        itemDeque.addLast("_62_");
        StdOut.printf("isEmpty = %s\n", itemDeque.isEmpty() ? "true" : "false");
        StdOut.printf("size    = %d\n", itemDeque.size());
        Iterator<String> iter = itemDeque.iterator();
        while (iter.hasNext()) {
            StdOut.printf("str     = %s\n", iter.next());
        }
    }

    // return an iterator over items in order from front to back
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
}