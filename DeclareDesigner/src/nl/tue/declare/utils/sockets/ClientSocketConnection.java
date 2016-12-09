package nl.tue.declare.utils.sockets;

import java.io.*;
import java.net.*;

public class ClientSocketConnection
    extends SocketConnection {

  private static final int DEFAULT_PORT = 8989;

  private int port;
  private String host;

  public ClientSocketConnection() {
    super();
    this.port = DEFAULT_PORT;
    // try {
    // this.host = InetAddress.getLocalHost().getHostAddress();
    this.host = SocketConnection.LOCAL_HOST;
    /*/  }
     catch (UnknownHostException ex) {
       this.host = "";
     }*/
  }

  public ClientSocketConnection(int port) {
    super();
    this.port = port;
    try {
      this.host = InetAddress.getLocalHost().
          getHostAddress();
    }
    catch (UnknownHostException ex) {
      this.host = "";
    }
  }

  public ClientSocketConnection(int port, String host) {
    super();
    this.port = port;
    this.host = host;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public void setHost(String host) {
    if (host != null) {
      this.host = host;
    }
  }

  protected void createSocket() throws UnknownHostException, IOException {
    connection = new Socket(host, port);
  }

}
