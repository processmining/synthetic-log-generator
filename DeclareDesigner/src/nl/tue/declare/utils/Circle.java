package nl.tue.declare.utils;

import java.awt.*;

/**
 * <p>Title: DECLARE</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: TU/e</p>
 *
 * @author Maja Pesic
 * @version 1.0
 */

public class Circle {

  public static double getRadius(double perimeter) {
    return perimeter / (2 * Math.PI);
  }

  public static Point[] getPoints(double radius, int npoints) {
    double diameter = Math.toRadians(360); // About 6.283
    double add_angle = diameter / (double) npoints;
    double angle = 0;

    Point[] points = new Point[npoints + 1];

    for (int i = 0; i < npoints; i++) {
      int x = (int) ( (double) radius + (radius * Math.cos(angle)));
      int y = (int) ( (double) radius + (radius * Math.sin(angle)));
      points[i] = new Point(x, y);
      angle += add_angle;
    }
    points[npoints] = points[0];

    return points;
  }
}
