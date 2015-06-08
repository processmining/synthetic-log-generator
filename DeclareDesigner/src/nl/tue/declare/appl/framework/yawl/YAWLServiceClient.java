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
import yawlservice.*;
import nl.tue.declare.utils.sockets.SocketConnectionLogWriter;
import nl.tue.declare.utils.SystemOutWriter;

/** A client-side class that uses a TCP/IP socket
 */
public class YAWLServiceClient {

  private PrintWriter out = null;
  private BufferedReader in = null;
  private SocketConnectionLogWriter logWriter = null;

  /** Constructor
   * @param host Internet address of the host
   *        where the server is located
   * @param port Port number on the host where
   *        the server is listening
   */
  public YAWLServiceClient(String host, int port) throws Exception {
    super();
    String temp = (host!=null)?host:InetAddress.getLocalHost().getHostAddress();
    SystemOutWriter.singleton().println("YAWLServiceClient Initialized on [" +
                                        port + " - " + temp + "]");
    try{
     Socket socket = new Socket(temp, port);
     out = new PrintWriter(socket.getOutputStream(), true);
     in = new BufferedReader(new InputStreamReader(new BufferedInputStream(
         socket.getInputStream())));
     logWriter = new SocketConnectionLogWriter();    
    } catch(Exception e){
	  // ignore this
    }
  }

  /**
   *
   * @param request Request
   * @return boolean
   */
  public boolean send(YAWLMessage msg) {
    try {
      String str = msg.toString();
      out.println(str);
      logWriter.sent(str);
      return true;
    }
    catch (Exception iox) {
    }
    return false;
  }

  /**
   *
   * @param request Request
   * @return String
   */
  public String request(YAWLMessage request) {
    if (send(request)) {
      try {
        String temp = "";
        String str = "";
        while (! (temp = in.readLine()).equals("")) {
          str += temp;
        }
        return str;
      }
      catch (IOException ex) {
        return null;
      }
    }
    return null;
  }

}
