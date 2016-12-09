package nl.tue.declare.utils.sockets;

import java.io.*;
import java.net.*;

public abstract class SocketConnection
    extends Thread {
  // get local host
  public static String LOCAL_HOST = null;
  static {
    try {
      LOCAL_HOST = InetAddress.getLocalHost().
          getHostAddress();
    }
    catch (UnknownHostException ex) {
    }
  }

  protected Socket connection = null;
  PrintWriter out = null;
  BufferedReader in = null;

  SocketConnectionLogWriter logWriter;

  boolean stopped = false;

  ISocketConnectionListener listener = null;

  protected abstract void createSocket() throws UnknownHostException,
      IOException;

  public SocketConnection() {
    super();
    logWriter = new SocketConnectionLogWriter();
  }

  public void connect() throws UnknownHostException, IOException {
    this.createSocket();
    BufferedInputStream is = new BufferedInputStream(connection.
        getInputStream());
    InputStreamReader isr = new InputStreamReader(is);
    this.in = new BufferedReader(isr);
    this.out = new PrintWriter(connection.getOutputStream(), true);
    if (this.listener != null) {
      this.listener.connected();
    }
  }

  /**
   *
   * @return String
   */
  String receive() {
    if (in != null) {
      try {
        String msg = "";
        synchronized (in) {
          msg = in.readLine();
        }
        this.received(msg);
        if (this.listener != null) {
          this.listener.received(msg);
        }
        return msg;
      }
      catch (IOException ex) {
        if (listener != null) {
          listener.disconnected();
        }
        return null;
      }
    }
    else {
      return null;
    }
    //  }
  }

  /**
   *
   * @param msg String
   */
  void send(String msg) {
    synchronized (out) {
      if (out != null) {
        try {
          if (out.checkError()) {
            throw new Exception("MOJ JE");
          }
          out.println(msg);
          this.sent(msg);
          if (this.listener != null) {
            this.listener.sent(msg);
          }
        }
        catch (Throwable e) {
          if (listener != null) {
            listener.disconnected();
          }
        }
      }
    }
  }

  /**
   * close
   */
  void close() {
    try {
      in.close();
      out.close();
      connection.close();
      if (listener != null) {
        listener.disconnected();
      }
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   *
   */
  public void run() {
    while (!stopped && (this.receive() != null)) {
    }
    if (stopped) {
      this.close();
    }
  }

  /**
   *
   * @param listener ISocketConnectionListener
   */
  public void addListener(ISocketConnectionListener listener) {
    this.listener = listener;
  }

  /**
   *
   */
  void finish() {
    this.stopped = true;
  }

  private void received(String msg) {
    logWriter.received( /*"-"+this.connection.getLocalPort()+"-"+*/msg);
  }

  private void sent(String msg) {
    logWriter.sent( /*"-"+this.connection.getLocalPort()+"-"+*/msg);
  }

  public synchronized void log(boolean log) {
	  SocketConnectionLogWriter.setLog(log);
  }

  public boolean isConnected() {
    return!out.checkError();
  }
}
