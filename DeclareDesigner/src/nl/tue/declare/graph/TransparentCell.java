package nl.tue.declare.graph;

import java.awt.geom.*;

import org.jgraph.graph.*;

import nl.tue.declare.domain.*;

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


public class TransparentCell
    extends DVertex {

  /**
	 * 
	 */
	private static final long serialVersionUID = 2453468667806401105L;

/**
   * Creates a custom graph cell with a given user object
   *
   * @param userObject
   */
  public TransparentCell(Base userObject) {
    //super(userObject);
    super(userObject, new Rectangle2D.Double(0, 0, 100, 100), null, false, false);
    this.initialize(new Rectangle2D.Double(0, 0, 100, 100));
  }

  /**
   * Initializes the graph cell
   *
   */
  private void initialize(Rectangle2D bounds) {
    GraphConstants.setSizeable(this.getAttributes(), false);
    setBounds(bounds);
  }

  /**
   * Initializes the graph cell
   *
   */
  /* public void setBounds(Rectangle2D bounds) {
     GraphConstants.setBounds(this.getAttributes(), bounds);
   }*/

  public void setPosition(double x, double y) {
    Rectangle2D old = GraphConstants.getBounds(getAttributes());
    GraphConstants.setBounds(this.getAttributes(),
                             new Rectangle2D.Double(x, y, old.getWidth(),
        old.getHeight()));
  }

  public Object addPort(Point2D offset, Object userObject) {
    DefaultPort port = new TransparentPort(userObject);
    if (offset == null) {
      add(port);
    }
    else {
      GraphConstants.setOffset(port.getAttributes(), offset);
      add(port);
    }
	return port;
  }
}
