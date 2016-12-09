package nl.tue.declare.appl.framework.engine;

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
public class EngineInitializer
    extends Initializer {

  private static final String INI_FILE = "engine.ini";

  private static final String YAWL_PORT_OLD = "YAWL_PORT";
  private static final String PORT_YAWL_SERVER = "PORT_YAWL_SERVER";

  private static final String PORT_YAWL_CLIENT = "PORT_YAWL_CLIENT";
  private static final String HOST_YAWL_CLIENT = "HOST_YAWL_CLIENT";

  private static final String ENGINE_PORT_INFO = "PORT_INFORMATION";
  private static final String ENGINE_PORT_REQ = "PORT_REQUEST";

  private static final String ENGINE_PORT_RECOMMENDATION =
      "PORT_RECOMMENDATION";
  private static final String ENGINE_HOST_RECOMMENDATION =
      "HOST_RECOMMENDATION";

  private static EngineInitializer instance = null;

  private EngineInitializer(String path) {
    super(INI_FILE, path);
  }

  public static EngineInitializer singleton() {
  if (instance == null) {
    instance = new EngineInitializer(null);
  }
  return instance;
}


/*  public int getYAWLPort() {
    String value = getValue(PORT_YAWL_SERVER);
    int port = -1;
    if (value != null) {
      try {
        port = Integer.parseInt(value);
      }
      catch (Exception e) {
        value = getValue(YAWL_PORT_OLD);
        if ()
      }
    }
    return port;
  }*/

public int getPortYAWLServer() {
    try {
      return stringToInt(PORT_YAWL_SERVER);
    }
    catch (Exception e) {
      try {
        return stringToInt(YAWL_PORT_OLD);
      }
      catch (Exception e2) {
         // ignore
    }
    }
  return -1;
}

  private int stringToInt(String parameter) throws Exception {
    String value = getValue(parameter);
    if (value != null) {
       return Integer.parseInt(value);
    } else throw new Exception();
  }

  public int getPortInformation() {
    String value = getValue(ENGINE_PORT_INFO);
    int port = -1;
    if (value != null) {
      try {
        port = Integer.parseInt(value);
      }
      catch (Exception e) {
        //ignore
      }
    }
    return port;
  }

  public int getPortRequest() {
    String value = getValue(ENGINE_PORT_REQ);
    int port = -1;
    if (value != null) {
      try {
        port = Integer.parseInt(value);
      }
      catch (Exception e) {
        //ignore
      }
    }
    return port;
  }

  public int getPortRecommendation() {
    String value = getValue(ENGINE_PORT_RECOMMENDATION);
    int port = -1;
    if (value != null) {
      try {
        port = Integer.parseInt(value);
      }
      catch (Exception e) {
        //ignore
      }
    }
    return port;
  }

  public String getHostRecommendation() {
    return getValue(ENGINE_HOST_RECOMMENDATION);
  }

  public void setPortYAWLServer(String str) {
    setValue(PORT_YAWL_SERVER, str);
  }

  public int getPortYAWLClient() {
    try {
        return stringToInt(PORT_YAWL_CLIENT);
      }
      catch (Exception e) {
        return -1;
      }
  }

  public String getHostYAWLClient() {
    return getValue(HOST_YAWL_CLIENT);
  }

  public void setRequestPort(String str) {
    setValue(ENGINE_PORT_REQ, str);
  }

  public void setInformationPort(String str) {
    setValue(ENGINE_PORT_INFO, str);
  }

  public void setRecommendationPort(String str) {
    setValue(ENGINE_PORT_RECOMMENDATION, str);
  }

  public void setRecommendationHost(String str) {
    setValue(ENGINE_HOST_RECOMMENDATION, str);
  }
}
