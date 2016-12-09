package nl.tue.declare.appl.worklist;


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

public interface IRemoteEngineListener {
  /**
   * received
   *
   * @param engine RemoteEngineProxy
   * @param msg String
   */
  public void received(String message);

  /**
   * sent
   *
   * @param engine RemoteEngineProxy
   * @param msg String
   */
  public void sent(RemoteEngineProxy engine, String msg);

  /**
   * connected
   *
   * @param engine RemoteEngineProxy
   */
  public void connected(RemoteEngineProxy engine);

  /**
   * disconnected
   *
   * @param engine RemoteEngineProxy
   */
  public void disconnected(RemoteEngineProxy engine);
}
