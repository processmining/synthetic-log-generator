package nl.tue.declare.appl.util;

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
public class Utils {

  private static final String APPLICATION_PATH = "user.dir";

  public static String applciationPath() {
    return System.getProperty(APPLICATION_PATH);
  }

}
