package nl.tue.declare.appl.design.model;

import javax.swing.*;

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
public interface AssignmentCoordinatorListener {
  /**
   * deactivated
   */
  public void deactivated(JInternalFrame frame);

  /**
   * activated
   */
  public void activated(JInternalFrame frame);

  public void closed(JInternalFrame frame);
}
