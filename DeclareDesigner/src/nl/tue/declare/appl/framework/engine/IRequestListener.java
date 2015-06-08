package nl.tue.declare.appl.framework.engine;

import nl.tue.declare.execution.msg.*;

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
public interface IRequestListener {
  public IMessage request(IMessage msg, IWorklistRemoteProxy proxy);
}
