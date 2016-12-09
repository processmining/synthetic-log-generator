package nl.tue.declare.appl.worklist;

import nl.tue.declare.utils.*;

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
class WorklistInitializer
    extends Initializer {

  private static final String ENGINE_HOST = "HOST";
  private static final String ENGINE_PORT_INFO = "PORT_INFORMATION";
  private static final String ENGINE_PORT_REQ = "PORT_REQUEST";

  /**
   *
   * @param path String
   */
  protected WorklistInitializer(String path, String file) {
    super(file, path);
  }

  /**
   *
   * @return int
   */
  public int getPortInfo() {
    String value = getValue(ENGINE_PORT_INFO);
    int port = -1;
    if (value != null) {
      try {
        port = Integer.parseInt(value);
      }
      catch (Exception e) {

      }
    }
    return port;
  }

  public void setPortInfo(int port) {
    setValue(ENGINE_PORT_INFO, Integer.toString(port));
  }

  /**
   *
   * @return int
   */
  public int getPortReqest() {
    String value = getValue(ENGINE_PORT_REQ);
    int port = -1;
    if (value != null) {
      try {
        port = Integer.parseInt(value);
      }
      catch (Exception e) {

      }
    }
    return port;
  }

  public void setPortRequest(int port) {
    setValue(ENGINE_PORT_REQ, Integer.toString(port));
  }

  /**
   *
   * @return String
   */
  public String getHost() {
    return getValue(ENGINE_HOST);
  }

  public void setHost(String host) {
    setValue(ENGINE_HOST, host);
  }
}
