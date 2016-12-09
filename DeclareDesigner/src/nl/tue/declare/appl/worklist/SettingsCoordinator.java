package nl.tue.declare.appl.worklist;


/**
 * <p>Title: DECLARE</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: TU/e</p>
 *
 * @author not attributable
 * @version 1.0
 */
public class SettingsCoordinator {
  private static final String INI_FILE = "worklist.ini";
  private static SettingsCoordinator instance = null;
  private WorklistInitializer ini;

  private SettingsCoordinator() {
    super();
    ini = new WorklistInitializer(null, INI_FILE);
  }

  public static SettingsCoordinator singleton() {
    if (instance == null) {
      instance = new SettingsCoordinator();
    }
    return instance;
  }

  public int getPortInfo() {
    return ini.getPortInfo();
  }

  public void setPortInfo(int port) {
    ini.setPortInfo(port);
  }

  /**
   *
   * @return int
   */
  public int getPortReqest() {
    return ini.getPortReqest();
  }

  public void setPortRequest(int port) {
    ini.setPortRequest(port);
  }

  /**
   *
   * @return String
   */
  public String getHost() {
    return ini.getHost();
  }

  public void setHost(String host) {
    ini.setHost(host);
  }

  public void save() {
    ini.save();
  }

  public void restore() {
    ini.restore();
  }

}
