import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import java.util.ArrayList;

public class PointSET {

  final private SET<Point2D> pointSet;

  // construct an empty set of points
  public PointSET() {
    pointSet = new SET<Point2D>();
  }
  // is the set empty?
  public boolean isEmpty() {
    return pointSet.isEmpty();
  }
  // number of points in the set
  public int size() {
    return pointSet.size();
  }
  // add the point to the set (if it is not already in the set)
  public void insert(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException();
    }
    pointSet.add(p);
  }
  // does the set contain point p?
  public boolean contains(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException();
    }
    return pointSet.contains(p);
  }
  // draw all points to standard draw
  public void draw() {
    for (Point2D p: pointSet) {
      p.draw();
    }
  }
  // all points that are inside the rectangle (or on the boundary)
  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null) {
      throw new IllegalArgumentException();
    }
    ArrayList<Point2D> iterator = new ArrayList<Point2D>();
    for (Point2D p: pointSet) {
      if (rect.contains(p)) {
        iterator.add(p);
      }
    }
    return iterator;
  }
  // a nearest neighbor in the set to point p; null if the set is empty
  public Point2D nearest(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException();
    }
    if (pointSet.isEmpty()) {
      return null;
    }
    Point2D nearestPoint = pointSet.max();
    double smallestDistance = nearestPoint.distanceSquaredTo(p);
    for (Point2D thisPoint: pointSet) {
      double thisDistance = thisPoint.distanceSquaredTo(p);
      if (thisDistance < smallestDistance) {
        nearestPoint = thisPoint;
        smallestDistance = thisDistance;
      }
    }
    return nearestPoint;
  }
  // unit testing of the methods (optional)
  // public static void main(String[] args) {

  // }
}