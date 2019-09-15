import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> lineSegmentArrayList;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        nullChecker(points);

        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);
        duplicationChecker(sortedPoints);

        this.lineSegmentArrayList = new ArrayList<LineSegment>();
        final int n = sortedPoints.length;

        for (int a = 0; a < n - 3; a++) {
            Point pA = sortedPoints[a];
            for (int b = a + 1; b < n - 2; b++) {
                Point pB = sortedPoints[b];
                double slopeAB = pA.slopeTo(pB);
                for (int c = b + 1; c < n - 1; c++) {
                    Point pC = sortedPoints[c];
                    double slopeAC = pA.slopeTo(pC);
                    if (slopeAB == slopeAC) {
                        for (int d = c + 1; d < n; d++) {
                            if (slopeAB == sortedPoints[a].slopeTo(sortedPoints[d])) {
                                this.lineSegmentArrayList.add(new LineSegment(pA, sortedPoints[d]));
                            }
                        }
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return this.lineSegmentArrayList.size();
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] arr = new LineSegment[lineSegmentArrayList.size()];
        arr = lineSegmentArrayList.toArray(arr);
        return arr;
    }

    private void nullChecker(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void duplicationChecker(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}