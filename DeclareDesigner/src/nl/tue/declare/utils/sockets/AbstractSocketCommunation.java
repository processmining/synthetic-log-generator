package nl.tue.declare.utils.sockets;

import java.io.*;
import java.net.*;

class AbstractSocketCommunation {

  protected SocketConnection connection;

  public AbstractSocketCommunation(SocketConnection connection) {
    super();
    this.connection = connection;
  }

  /**
   *
   * @param listener ISocketConnectionListener
   */
  public void addListener(ISocketConnectionListener listener) {
    connection.addListener(listener);
  }

  public void connect() throws UnknownHostException, IOException {
    connection.connect();
  }

  public void start() {
    connection.start();
  }

  public void finish() {
    connection.finish();
  }

  public void log(boolean log) {
    connection.log(log);
  }

  protected String receive() {
    return connection.receive();
  }

  /**
   *
   * @param msg String
   */
  protected void send(String msg) {
    connection.send(msg);
  }

  public boolean isConnected() {
    return connection.isConnected();
  }
}
