package nl.tue.declare.appl.framework.engine;

import java.io.*;
import java.net.*;

import nl.tue.declare.utils.*;

public abstract class EngineGate
    extends Thread {

  private static final int DEFAULT_PORT = 8190;
  private int port = DEFAULT_PORT;
  private boolean listen;
  private ServerSocket socket = null;

  protected EngineGate() {
    super();
  }

  public void setPort(int port) {
    this.port = port;
  }

  /**
   *
   * @todo Implement this nl.tue.declare.execution.engine.IEngineGate method
   */
  public void init() throws Exception {
    this.setListen(true);
    socket = new ServerSocket(port);
    SystemOutWriter.singleton().println(this.getClass().getSimpleName() +
                                        " initialized on [" +
                                        port +
                                        "]");
    this.start();
  }

  protected abstract IWorklistRemoteProxy createProxy(Socket connection);

  public void run() {
    try {
      while (listen) {
        Socket connection = socket.accept();
        try {
          IWorklistRemoteProxy proxy = createProxy(connection);
          proxy.connect();
          this.connected(proxy);
          proxy.initiate();

        }
        catch (Exception e) {
          closeConnection(connection);
        }
      }
    }
    catch (Exception e) {
      // ignore
    }
  }

  public abstract void connected(IWorklistRemoteProxy proxy);

  private void closeConnection(Socket socket) {
    try {
      socket.close();
    }
    catch (IOException e) {}
  }

  public synchronized void setListen(boolean listen) {
    this.listen = listen;
  }
}
