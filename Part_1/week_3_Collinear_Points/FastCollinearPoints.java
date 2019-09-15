import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private ArrayList<LineSegment> lineSegmentArrayList;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        nullChecker(points);

        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);
        duplicationChecker(sortedPoints);

        this.lineSegmentArrayList = new ArrayList<LineSegment>();
        final int N = sortedPoints.length;

        for (int i = 0; i < N; i++) {
            Point pOrigin = sortedPoints[i];
            Point[] psBySlope = sortedPoints.clone();
            Arrays.sort(psBySlope, pOrigin.slopeOrder());

            int x = 1;
            while (x < N) {
                LinkedList<Point> candidates = new LinkedList<>();
                final double SLOPE_ORIGIN_TO_X = pOrigin.slopeTo(psBySlope[x]);
                while (x < N && pOrigin.slopeTo(psBySlope[x]) == SLOPE_ORIGIN_TO_X) {
                    candidates.add(psBySlope[x++]);
                }

                if (candidates.size() >= 3 && pOrigin.compareTo(candidates.peek()) < 0) {
                    Point max = candidates.removeLast();
                    lineSegmentArrayList.add(new LineSegment(pOrigin, max));
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}