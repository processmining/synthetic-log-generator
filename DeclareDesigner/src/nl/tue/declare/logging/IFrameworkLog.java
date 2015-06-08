package nl.tue.declare.logging;

/**
 *
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
import yawlservice.*;

public interface IFrameworkLog {
  /**
   * arrived
   */
  public void arrived(ExternalWorkItem external);
}
