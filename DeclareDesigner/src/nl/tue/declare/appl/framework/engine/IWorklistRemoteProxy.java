package nl.tue.declare.appl.framework.engine;

import java.io.*;
import java.net.*;

import nl.tue.declare.domain.organization.*;
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
public interface IWorklistRemoteProxy {
  /**
   *
   * @throws UnknownHostException
   * @throws IOException
   */
  public void connect() throws UnknownHostException, IOException;

  /**
   *
   * @return String
   */
  //public IEngineMessageHandler receive(User user);
  /**
   *
   * @param msg String
   */
  public void send(IMessage msg);

  /**
   *
   */
  public void close();

  public void initiate();

  /**
   * addListener
   *
   * @param listener IWorklistRemoteProxyListener
   */
  public void addListener(IWorklistRemoteProxyListener listener);

  public void setRequestListener(IRequestListener l);

  User getUser();

  RemoteWorklist getWorklist();

  boolean isConnected();
}
