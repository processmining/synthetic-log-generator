package nl.tue.declare.utils.sockets;

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
public interface ISocketConnectionListener {

  /**
   * received
   *
   * @param message String
   */
  public void received(String message);

  /**
   * sent
   *
   * @param message String
   */
  public void sent(String message);

  public void disconnected();

  public void connected();
}
