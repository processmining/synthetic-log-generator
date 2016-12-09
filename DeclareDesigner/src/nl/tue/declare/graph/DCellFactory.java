package nl.tue.declare.graph;

/** <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

import java.util.*;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

import org.jgraph.graph.*;

public class DCellFactory {

  private static final int[] ARROW = {
      DGraphConstants.ARROW_NONE, DGraphConstants.ARROW_CLASSIC,
      DGraphConstants.ARROW_TECHNICAL,
      DGraphConstants.ARROW_SIMPLE, DGraphConstants.ARROW_CIRCLE,
      DGraphConstants.ARROW_LINE, DGraphConstants.ARROW_DOUBLELINE,
      DGraphConstants.ARROW_DIAMOND, DGraphConstants.ARROW_TECHNICAL_CIRCLE};

  private static final float[][] DASH = {
      DGraphConstants.DASH_NONE, DGraphConstants.DASH_1, DGraphConstants.DASH_2,
      DGraphConstants.DASH_3};

  private static final int[] NUMBERS = {
      DGraphConstants.NUMBER_ONE, DGraphConstants.NUMBER_TWO,
      DGraphConstants.NUMBER_THREE};

  /**
   * createEdge
   *
   * @param style float
   * @param left Point
   * @param right Point
   * @return DefaultEdge
   */
  public static DEdge createEdge(LineStyle style, Point left,
                                 Point right) {
    DEdge edge = new DEdge(style);

    // Set start and end point for edge
    ArrayList<Point> points = new ArrayList<Point>();
    points.add(left);
    points.add(right);
    GraphConstants.setPoints(edge.getAttributes(), points);

    // Set selectable for edge
    GraphConstants.setSelectable(edge.getAttributes(), false);
    return edge;
  }

  public static DEdge createEdge(DEdge edge) {
    // Set selectable for edge
    GraphConstants.setSelectable(edge.getAttributes(), false);
    return edge;
  }

  /**
   * getArrows
   *
   * @return ArrayList
   */
  public static ArrayList<Integer> getArrows() {
    ArrayList<Integer> list = new ArrayList<Integer>();
    int arrow;
    for (int i = 0; i < ARROW.length; i++) {
      arrow = ARROW[i];
      list.add(new Integer(arrow));
    }
    return list;
  }

  /**
   * getDashLineStyles
   *
   * @param begin int
   * @param end int
   * @return ArrayList
   */
  public static ArrayList<LineStyle> getDashLineStyles(int begin, int middle,
      int end) {
    ArrayList<LineStyle> list = new ArrayList<LineStyle>();
    for (int i = 0; i < DASH.length; i++) {
      list.add(new LineStyle(DASH[i], begin, end, middle));
    }
    return list;
  }

  /**
   * getDashLineStyles
   *
   * @param begin int
   * @param end int
   * @return ArrayList
   */
  public static ArrayList<LineStyle> getNumberLineStyles(float dash[],
      int begin, int middle, int end) {
    ArrayList<LineStyle> list = new ArrayList<LineStyle>();
    for (int i = 0; i < NUMBERS.length; i++) {
      list.add(new LineStyle(dash, NUMBERS[i], begin, middle, end));
    }
    return list;
  }

  /**
   * getBeginStyles
   *
   * @param dash float[]
   * @param end int
   * @return ArrayList
   */
  public static ArrayList<LineStyle> getBeginStyles(float[] dash, int middle,
      int end) {
    ArrayList<LineStyle> list = new ArrayList<LineStyle>();
    for (int i = 0; i < ARROW.length; i++) {
      list.add(new LineStyle(dash, ARROW[i], middle, end));
    }
    return list;
  }

  /**
   * getEndStyles
   *
   * @param dash float[]
   * @param begin int
   * @return ArrayList
   */
  public static ArrayList<LineStyle> getEndStyles(float[] dash, int begin,
                                                  int middle) {
    ArrayList<LineStyle> list = new ArrayList<LineStyle>();
    for (int i = 0; i < ARROW.length; i++) {
      list.add(new LineStyle(dash, begin, middle, ARROW[i]));
    }
    return list;
  }

  /**
   * get middle styles
   *
   * @param dash float[]
   * @param begin int
   * @return ArrayList
   */
  public static ArrayList<LineStyle> getMiddleStyles(float[] dash, int begin,
      int end) {
    ArrayList<LineStyle> list = new ArrayList<LineStyle>();
    for (int i = 0; i < ARROW.length; i++) {
      list.add(new LineStyle(dash, begin, ARROW[i], end));
    }
    return list;
  }

  /**
   * createLines
   *
   * @param lineStyles ArrayList
   * @param left Point
   * @param right Point
   * @return ArrayList
   */
  public static ArrayList<DEdge> createLinesForStyles(ArrayList<LineStyle>
      lineStyles,
      Point left, Point right) {
    ArrayList<DEdge> edges = new ArrayList<DEdge>();

    for (int i = 0; i < lineStyles.size(); i++) {
      edges.add(new DEdge(lineStyles.get(i)));
    }
    return edges;
  }

  /**
   * createVertex
   *
   * @param object Object  -> user object represented by the vertex
   * @param x double       -> vertex position on the x axis
   * @param y double       -> vertex position on the y axis
   * @param w double       -> vertex width
   * @param h double       -> vertex height
   * @param bg Color       -> background color
   * @param raised boolean -> is the border raised
   * @return DVertex     -> created new vertex
   */
  public static DefaultGraphCell createVertex(Object object, double x,
                                              double y, double w, double h,
                                              Color bg, boolean raised) {
    DefaultGraphCell vertex = new DefaultGraphCell(object);

    GraphConstants.setChildrenSelectable(vertex.getAttributes(), false);
    GraphConstants.setBounds(vertex.getAttributes(), new Rectangle2D.Double(
        x, y, w, h));

    // Set fill color
    if (bg != null) {
      GraphConstants.setGradientColor(vertex.getAttributes(), bg);
      GraphConstants.setOpaque(vertex.getAttributes(), true);
    }

    // Set raised border
    if (raised) {
      GraphConstants.setBorder(vertex.getAttributes(), BorderFactory
                               .createRaisedBevelBorder());
    }
    else {
      // Set black border
      GraphConstants.setBorderColor(vertex.getAttributes(), Color.black);
    }
    vertex.addPort();
    return vertex;
  }

  /**
   * createVertex
   *
   * @param vertex Object -> user object represented by the vertex
   * @param x double -> vertex position on the x axis
   * @param y double -> vertex position on the y axis
   * @param w double -> vertex width
   * @param h double -> vertex height
   * @param bg Color -> background color
   * @param raised boolean -> is the border raised
   * @return DVertex -> created new vertex
   */
  public static DVertex createVertex(DVertex vertex, double x,
                                     double y, double w, double h, Color bg,
                                     boolean raised) {

    GraphConstants.setChildrenSelectable(vertex.getAttributes(), false);
    GraphConstants.setBounds(vertex.getAttributes(), new Rectangle2D.Double(
        x, y, w, h));

// Set fill color
    if (bg != null) {
      GraphConstants.setGradientColor(vertex.getAttributes(), bg);
      GraphConstants.setOpaque(vertex.getAttributes(), true);
    }

// Set raised border
    if (raised) {
      GraphConstants.setBorder(vertex.getAttributes(), BorderFactory
                               .createRaisedBevelBorder());
    }
    else {
// Set black border
      GraphConstants.setBorderColor(vertex.getAttributes(), Color.black);
    }
    return vertex;
  }

}
