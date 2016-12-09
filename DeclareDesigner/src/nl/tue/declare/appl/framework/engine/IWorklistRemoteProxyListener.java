package nl.tue.declare.appl.framework.engine;

import nl.tue.declare.domain.organization.*;

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


public interface IWorklistRemoteProxyListener {
  /**
   * received
   */
  //public void received(String msg);

  public void disconnected(IWorklistRemoteProxy proxy);

  public User getUser();

  public RemoteWorklist getWorklist();

  //public IMessage request(IMessage msg, IWorklistRemoteProxy proxy);

}
