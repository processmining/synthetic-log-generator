package nl.tue.declare.graph.model;

import java.awt.geom.*;

import nl.tue.declare.graph.*;

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
public class ConstraintView
    extends DEdgeView {
  /**
	 * 
	 */
	private static final long serialVersionUID = -5588825562683988981L;

public ConstraintView(Object object) {
    super(object);
  }

  /**
   * Hook to return the vector that is taken as the base vector to compute
   * relative label positions. Normally, the vector goes from the first to the
   * last point on the edge, unless these points are equal, in which case the
   * average distance of all points to the source point is used.
   *
   * @return Point2D
   */
  public Point2D getLabelVector() {
    if (labelVector == null) {
      labelVector = super.getLabelVector();
      Point2D p0 = getPoint(0);
      double dx = 0;
      double dy = 0;
      // Finds an average distance
      dx = 0;
      dy = 0;
      int n = getPointCount();
      if (isLoop()) {
        for (int i = 1; i < n; i++) {
          Point2D point = getPoint(i);
          dx += point.getX() - p0.getX();
          dy += point.getY() - p0.getY();
        }
        n /= 2;
        dx /= (double) n;
        dy *= (double) n * 0.65;
        labelVector = new Point2D.Double(dx, dy);
      }
    }
    return labelVector;
  }

}
