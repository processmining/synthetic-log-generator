package nl.tue.declare.graph;

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

import java.awt.*;
import java.awt.geom.*;

import org.jgraph.graph.*;

public class TransparentCellView
    extends VertexView {

  /**
	 * 
	 */
	private static final long serialVersionUID = -6460594714818908835L;
/**
   */
  public static transient JGraphTransparentRenderer renderer = new
      JGraphTransparentRenderer();

  /**
   */
  public TransparentCellView() {
    super();
  }

  /**
   */
  public TransparentCellView(Object cell) {
    super(cell);
  }

  /**
   * Returns the intersection of the bounding rectangle and the straight line
   * between the source and the specified point p. The specified point is
   * expected not to intersect the bounds.
   */
  public Point2D getPerimeterPoint(EdgeView edge, Point2D source, Point2D p) {
    return getCenterPoint(this);
  }

  /**
   */
  public CellViewRenderer getRenderer() {
    return renderer;
  }

  /**
   * The Vertex Renderer inherits from JLabel. Do not paint anything.
   */
  public static class JGraphTransparentRenderer
      extends VertexRenderer {
    /**
	 * 
	 */
	private static final long serialVersionUID = 961934859392648500L;

	/**
     * Paints the Ellipse.
     */
    public void paint(Graphics g) {}
  }

}
