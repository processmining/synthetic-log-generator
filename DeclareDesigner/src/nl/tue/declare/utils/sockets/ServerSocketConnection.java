package nl.tue.declare.utils.sockets;

import java.io.*;
import java.net.*;

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
public class ServerSocketConnection
    extends SocketConnection {
  public ServerSocketConnection(Socket socket) {
    super();
    this.connection = socket;
  }

  /**
   * createSocket
   *
   * @throws UnknownHostException
   * @throws IOException
   * @todo Implement this nl.tue.declare.utils.SocketConnection method
   */
  protected void createSocket() throws UnknownHostException, IOException {
  }
}
