import edu.princeton.cs.algs4.StdOut;
import java.util.NoSuchElementException;
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private int head;
    private int tail;
    private int len;
    private Item[] deque;

    // construct an empty deque
    public Deque() {
        deque = (Item[]) new Object[1];
        head = tail = len = 0;
    }

    // is the deque empty?
    public boolean isEmpty() { return len == 0; }

    // return the number of items on the deque
    public int size() { return len; }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) { throw new IllegalArgumentException(); }
        ++len;
        if (head == 0) {
            resize(3 * len);
        }
//        StdOut.printf("addFirst: head = %d, tail = %d\n", head, tail);
        deque[head--] = item;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) { throw new IllegalArgumentException(); }
        ++len;
        if (tail == head + len) {
            resize(3 * len);
        }
        deque[tail++] = item;
//        StdOut.printf("addLast: head = %d, tail = %d\n", head, tail);
//        for (int i = 0; i < deque.length; i++) {
//            StdOut.printf("addLast: deque[%d] = %s\n", i, deque[i]);
//        }
//        for (int i = 0; i < deque.length; i++) {
//            StdOut.printf("addLast: deque[%d] = %s\n", i, deque[i]);
//        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (len <= 0) { throw new NoSuchElementException(); }
        --len;
        Item item = deque[++head];
        deque[head] = null;
//        StdOut.printf("removeFirst -> deque[%d] = %s\n",  head, item);
        if (len > 0 && len == tail/4) {
            resize(tail/2);
        }
//        for (int i = 0; i < deque.length; i++) {
//            StdOut.printf("addLast: deque[%d] = %s\n", i, deque[i]);
//        }
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (len <= 0) { throw new NoSuchElementException(); }
        --len;
        Item item = deque[--tail];
        deque[tail] = null;
//        StdOut.printf("removeLast -> deque[%d] = %s\n", tail, item);
        if (len > 0 && len == (deque.length - head)/4) {
            resize((deque.length - head)/2);
        }
//        for (int i = 0; i < deque.length; i++) {
//            StdOut.printf("addLast: deque[%d] = %s\n", i, deque[i]);
//        }
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

    private void resize(int capacity)
    {
        int oldHead = head;
        head = tail = (capacity - len)/2;
        Item[] copy = (Item[]) new Object[capacity];

        for (int i = 0; i < len; i++, tail++) {
            copy[tail] = deque[oldHead + i];
        }
        deque = copy;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() { return new ArrayIterator(); }

    private class ArrayIterator implements Iterator<Item>
    {
        private int i = head;
        public boolean hasNext() { return i < head + len; }
        public void remove() { throw new UnsupportedOperationException(); }
        public Item next() {
            if (!hasNext()) { throw new NoSuchElementException(); }
            return deque[++i];
        }
    }
}