package nl.tue.declare.appl.framework.yawl;

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
import java.io.*;
import java.net.*;

import nl.tue.declare.logging.*;
import nl.tue.declare.utils.*;
import yawlservice.*;

public abstract class YAWLServiceServer
    extends Thread{

  private FrameworkLogWriter frameworkLogWriter;
  private ServerSocket socket;

  /**
   * RemoteCaseLoader
   *
   * @param port int
   */
  public YAWLServiceServer(int port) {
    super();
    try {
      socket = new ServerSocket(port);
      SystemOutWriter.singleton().println("YAWLServiceServer Initialized on [" +
                                          port + "]");
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
    frameworkLogWriter = new FrameworkLogWriter();
  }

  public void run() {
      while (true) {
        Socket  connection = null;
        try {
          connection = socket.accept();
          BufferedReader in  = new BufferedReader(new InputStreamReader(new BufferedInputStream(connection.
              getInputStream())));
          String temp = "";
          String str = "";
          while (! (temp = in.readLine()).equals("")) {
            str += temp;
      }
         this.frameworkLogWriter.connected();
          YAWLMessage request = YAWLMessageFactory.parseMessage(str);
          received(request);
        }
        catch (Exception e) {
        e.printStackTrace();	
          closeConnection(connection);
        }
      }
  }

  public abstract void received(YAWLMessage request);

  private void closeConnection(Socket socket) {
    try {
      socket.close();
    }
    catch (IOException e) {
      // ignore
    }
  }
}
