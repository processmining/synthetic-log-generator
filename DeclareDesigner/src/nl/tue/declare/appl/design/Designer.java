package nl.tue.declare.appl.design;

import nl.tue.declare.appl.*;

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
public class Designer {
  /**
   * Application entry point.
   *
   * @param args String[]
   */
  public static void main(String[] args) {
    Project.ini(args);
    new DesignerCoordinator();
  }
}
