package nl.tue.declare.graph;

import java.awt.*;
import java.awt.geom.*;

import org.jgraph.graph.*;

/**
 * <p>Title: </p>
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
public class DEdgeRenderer
    extends EdgeRenderer {

  /**
	 * 
	 */
	private static final long serialVersionUID = -2245131380109364539L;

  protected transient Color middleColor;

  /** Painting attributes of the current edgeview */
  protected transient int lineNumber;
  protected transient float width;

  /**
   * Constructs a renderer that may be used to render edges.
   */
  public DEdgeRenderer() {
    super();
  }

  /**
   * Paint the current view's direction. Sets tmpPoint as a side-effect such
   * that the invoking method can use it to determine the connection point to
   * this decoration.
   *
   * @param size int
   * @param style int
   * @param src Point2D
   * @param dst Point2D
   * @return Shape
   */
  protected Shape createLineEnd(int size, int style, Point2D src, Point2D dst) {
    if (src == null || dst == null) {
      return null;
    }
    if (style != DGraphConstants.ARROW_TECHNICAL_CIRCLE) {
      return super.createLineEnd(size, style, src, dst);
    } else {
      Area areaCircle = new Area(super.createLineEnd(size,
          GraphConstants.ARROW_CIRCLE, src, dst));
      Shape arrow = super.createLineEnd(size, GraphConstants.ARROW_TECHNICAL,
                                        src, dst);
      Area areaPoly = new Area(arrow);
      areaCircle.add(areaPoly);
      return areaCircle;
    }
  }

  /**
   * Installs the attributes of specified cell in this renderer instance. This
   * means, retrieve every published key from the cells hashtable and set
   * global variables or superclass properties accordingly.
   *
   * @param view
   *            the cell view to retrieve the attribute values from.
   */

 protected void installAttributes(CellView view) {
    super.installAttributes(view);
    AttributeMap map = view.getAllAttributes();
    lineNumber = DGraphConstants.getLineNumber(map);
    
    width = this.lineWidth;
    this.lineWidth = this.switchWidth(this.lineNumber);
    
  }

  /**
   * getDEdgeView
   *
   * @return DEdgeView
   */
  public DEdgeView getDEdgeView() {
    if (view instanceof DEdgeView) {
      return (DEdgeView) view;
    }
    return null;
  }

  /*
   rewritten method paint from EdgeRenderer. Added extra edge in them middle of fifferent color.
   */
  public void paint(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    super.paint(g);
    this.drawLines(g2);
  }

  /**
   *
   * @param g2 Graphics2D
   */
  private void drawLines(Graphics2D g2) {
    if (this.lineNumber > 1) {
      Color color = this.getForeground();
      for (int i = this.lineNumber - 1; i > 0; i--) {
        float w = this.switchWidth(i);
        color = this.switchColor(color);
        drawLine(g2, w, color);
      }
    }
  }

  private float switchWidth(int number) {
    return width * ( ( 2 * number) - 1);
  }

  private Color switchColor(Color color) {
    Color c = this.getForeground();
    if (color != null) {
      if (color.equals(graph.getBackground())) {
        c = this.getForeground();
      } else {
        c = graph.getBackground();
      }
    }

    if (selected) {
      c = graph.getHighlightColor();
    }
    return c;
  }

  /**
   *
   * @param g2 Graphics2D
   * @param width int
   * @param color Color
   */
  private void drawLine(Graphics2D g2, float width, Color color) {
    if (view.lineShape != null) {
      g2.setColor(color);
      g2.setStroke(this.getStroke(width));
      g2.draw(view.lineShape);
    }
  }

 private Stroke getStroke(float width) {
    Stroke s = null;
    if (lineDash != null) { // Dash For Line Only
      s = new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                          10.0f, lineDash, dashOffset);
    } else {
      s = new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
    }

    if (selected) { // Paint Selected
      s = GraphConstants.SELECTION_STROKE;
    }
    return s;
  }
}
