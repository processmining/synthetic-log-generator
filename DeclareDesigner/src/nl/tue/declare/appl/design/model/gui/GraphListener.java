package nl.tue.declare.appl.design.model.gui;

import java.awt.geom.*;

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


public interface GraphListener {
  /**
   * insertVertex
   *
   * @param point Point2D
   */
  public void insertVertex(Point2D point);

  /**
   * remove
   *
   * @param cells Object[]
   */
  public void remove(Object[] cells);

  /**
   * edit
   *
   * @param cell Object
   */
  public void edit(Object cell);


  public void addConstraint();

}
