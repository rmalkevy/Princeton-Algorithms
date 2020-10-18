import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {

  private int size = 0;
  final private static boolean CHIRALITY_LEFT = true;
  final private static boolean CHIRALITY_RIGHT = false;
  final private static boolean SPLITS_V = true;
  final private static boolean SPLITS_H = false;

  private static class Node {
    private Point2D p;      // the point
    private RectHV rect;    // the axis-aligned rectangle corresponding to this node
    private Node lb;        // the left/bottom subtree
    private Node rt;        // the right/top subtree
 }

  // construct an empty set of points
  final private Node rootKdTree;
  private Point2D nearestPoint;
  private double smallestDistance;

  public KdTree() {
    rootKdTree = new KdTree.Node();
    rootKdTree.p = null;
    rootKdTree.rect = new RectHV(0.0, 0.0, 1.0, 1.0);
    rootKdTree.lb = new KdTree.Node();
    rootKdTree.rt = new KdTree.Node();
  }
  // is the set empty?
  public boolean isEmpty() {
    return size == 0;
  }
  // number of points in the set
  public int size() {
    return size;
  }
  // add the point to the set (if it is not already in the set)
  public void insert(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException();
    }
    if (contains(p)) {
      return;
    }
    addByX(rootKdTree, p, null, null);
  }

  private void addByX(Node node, Point2D p, Node prevNode, Boolean chirality) {
    if (node.p == null) {
      addPointToNode(node, p, prevNode, SPLITS_V, chirality);
      return;
    }
    if (p.x() < node.p.x()) {
      addByY(node.lb, p, node, CHIRALITY_LEFT);
    } else {
      addByY(node.rt, p, node, CHIRALITY_RIGHT);
    }
  }

  private void addByY(Node node, Point2D p, Node prevNode, Boolean chirality) {
    if (node.p == null) {
      addPointToNode(node, p, prevNode, SPLITS_H, chirality);
      return;
    }
    if (p.y() < node.p.y()) {
      addByX(node.lb, p, node, CHIRALITY_LEFT);
    } else {
      addByX(node.rt, p, node, CHIRALITY_RIGHT);
    }
  }

  private void addPointToNode(Node node, Point2D p, Node prevNode, Boolean splits, Boolean chirality) {
    size++;
    node.p = p;
    node.lb = new KdTree.Node();
    node.rt = new KdTree.Node();
    if (prevNode == null) {
      return;
    }

    double xmin = 0;
    double ymin = 0;
    double xmax = 0;
    double ymax = 0;
    if (splits == SPLITS_H && chirality == CHIRALITY_LEFT) {
      xmin = prevNode.rect.xmin();
      ymin = prevNode.rect.ymin();
      xmax = prevNode.p.x();
      ymax = prevNode.rect.ymax();
    } else if (splits == SPLITS_H && chirality == CHIRALITY_RIGHT) {
      xmin = prevNode.p.x();
      ymin = prevNode.rect.ymin();
      xmax = prevNode.rect.xmax();
      ymax = prevNode.rect.ymax();
    } else if (splits == SPLITS_V && chirality == CHIRALITY_LEFT) {
      xmin = prevNode.rect.xmin();
      ymin = prevNode.rect.ymin();
      xmax = prevNode.rect.xmax();
      ymax = prevNode.p.y();
    } else if (splits == SPLITS_V && chirality == CHIRALITY_RIGHT) {
      xmin = prevNode.rect.xmin();
      ymin = prevNode.p.y();
      xmax = prevNode.rect.xmax();
      ymax = prevNode.rect.ymax();
    }
    node.rect = new RectHV(xmin, ymin, xmax, ymax);
  }

  // does the set contain point p?
  public boolean contains(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException();
    }
    return searchByX(rootKdTree, p);
  }

  private boolean searchByY(Node node, Point2D p) {
    if (node.p == null) {
      return false;
    }
    if (p.y() == node.p.y() && p.x() == node.p.x()) {
      return true;
    }
    if (p.y() < node.p.y()) {
      return searchByX(node.lb, p);
    } else {
      return searchByX(node.rt, p);
    }
  }

  private boolean searchByX(Node node, Point2D p) {
    if (node.p == null) {
      return false;
    }
    if (p.y() == node.p.y() && p.x() == node.p.x()) {
      return true;
    }
    if (p.x() < node.p.x()) {
      return searchByY(node.lb, p);
    } else {
      return searchByY(node.rt, p);
    }
  }

  // draw all points to standard draw
  public void draw() {
    drawNode(rootKdTree, null, SPLITS_V, null);
  }

  private void drawNode(Node node, Node prevNode, boolean splits, Boolean chirality) {
    if (node.p == null) {
      return;
    }
    drawPointAndLine(node, prevNode, splits, chirality);
    drawNode(node.lb, node, !splits, CHIRALITY_LEFT);
    drawNode(node.rt, node, !splits, CHIRALITY_RIGHT);
  }

  private void drawPointAndLine(Node node, Node prevNode, boolean splits, Boolean chirality) {
    StdDraw.setPenRadius();
    if (splits == SPLITS_V && prevNode == null) {
      double x0 = node.p.x(), x1 = x0;
      double y0 = 0;
      double y1 = 1;
      StdDraw.setPenColor(StdDraw.RED);
      StdDraw.line(x0, y0, x1, y1);
    } else if (splits == SPLITS_V && chirality == CHIRALITY_LEFT) {
      double x0 = node.p.x(), x1 = x0;
      double y0 = prevNode.p.y();
      double y1 = prevNode.rect.ymin();
      StdDraw.setPenColor(StdDraw.RED);
      StdDraw.line(x0, y0, x1, y1);
    } else if (splits == SPLITS_V && chirality == CHIRALITY_RIGHT) {
      double x0 = node.p.x(), x1 = x0;
      double y0 = prevNode.p.y();
      double y1 = prevNode.rect.ymax();
      StdDraw.setPenColor(StdDraw.RED);
      StdDraw.line(x0, y0, x1, y1);
    } else if (splits == SPLITS_H && chirality == CHIRALITY_LEFT) {
      double y0 = node.p.y(), y1 = y0;
      double x0 = prevNode.p.x();
      double x1 = prevNode.rect.xmin();
      StdDraw.setPenColor(StdDraw.BLUE);
      StdDraw.line(x0, y0, x1, y1);
    } else if (splits == SPLITS_H && chirality == CHIRALITY_RIGHT) {
      double y0 = node.p.y(), y1 = y0;
      double x0 = prevNode.p.x();
      double x1 = prevNode.rect.xmax();
      StdDraw.setPenColor(StdDraw.BLUE);
      StdDraw.line(x0, y0, x1, y1);
    }
    StdDraw.setPenColor(StdDraw.BLACK);
    StdDraw.setPenRadius(0.01);
    node.p.draw();
  }

  // all points that are inside the rectangle (or on the boundary)
  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null) {
      throw new IllegalArgumentException();
    }
    ArrayList<Point2D> iterator = new ArrayList<Point2D>();

    addPointToRange(iterator, rootKdTree, rect);
    return iterator;
  }

  private void addPointToRange(ArrayList<Point2D> iterator, Node node, RectHV rect) {
    if (node.p == null) {
      return;
    }
    if (!node.rect.intersects(rect)) {
      return;
    }
    if (rect.contains(node.p)) {
      iterator.add(node.p);
    }
    addPointToRange(iterator, node.lb, rect);
    addPointToRange(iterator, node.rt, rect);
  }

  // a nearest neighbor in the set to point p; null if the set is empty
  public Point2D nearest(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException();
    }
    if (isEmpty()) {
      return null;
    }
    nearestPoint = rootKdTree.p;
    smallestDistance = nearestPoint.distanceSquaredTo(p);

    searchNearest(rootKdTree, p);
    return nearestPoint;
  }

  private void searchNearest(Node node, Point2D queryPoint) {
    if (node.p == null) {
      return;
    }
    if (node.rect.distanceSquaredTo(queryPoint) > smallestDistance) {
      return;
    }

    double distance = node.p.distanceSquaredTo(queryPoint);
    if (distance < smallestDistance) {
      nearestPoint = node.p;
      smallestDistance = distance;
    }

    if (node.lb.p != null && node.lb.rect.contains(queryPoint)) {
      searchNearest(node.lb, queryPoint);
      searchNearest(node.rt, queryPoint);
    } else {
      searchNearest(node.rt, queryPoint);
      searchNearest(node.lb, queryPoint);
    }
  }

  // unit testing of the methods (optional)
  // public static void main(String[] args) {

  // }
}