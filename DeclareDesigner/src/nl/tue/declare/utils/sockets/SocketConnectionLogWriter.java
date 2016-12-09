package nl.tue.declare.utils.sockets;

import nl.tue.declare.logging.*;

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

public class SocketConnectionLogWriter
    extends LogWriter {

  private final String DELIMITER = " -> ";

  private final String RECEIVED = "RECEIVED";
  private final String SENT = "SENT";

  private static boolean log = true;

  /**
   *
   */
  public SocketConnectionLogWriter() {
    super();
  }

  /**
   *
   * @param msg String
   */
  public void received(String msg) {
    this.write(RECEIVED, msg);
  }

  /**
   *
   * @param msg String
   */
  public void sent(String msg) {
    this.write(SENT, msg);
  }

  /**
   *
   * @param type String
   * @param msg String
   */
  private void write(String type, String msg) {
    if (log) {
      super.write(type + DELIMITER + msg);
    }
  }

  public static void setLog(boolean l) {
    log = l;
  }
}
