package nl.tue.declare.appl;

import javax.swing.*;

import nl.tue.declare.utils.sockets.*;

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
public class Project {
  public static final String NAME = "DECLARE";
  private static final String LOG = "log";
  private static final String SYSTEM_LOOK_AND_FEEL = "system";
  //private static final String APPLICATION_PATH = "user.dir";

  public static void ini(String[] args) {
    boolean log = false;
    boolean system = false;
    int i = 0;
    // get arguments
    while (i < args.length) {
      log |= args[i].equals(LOG); // get log
      system |= args[i].equals(SYSTEM_LOOK_AND_FEEL); // get system
      i++;
    }

    SocketConnectionLogWriter.setLog(log); // set logging

    if (system) { // set system
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          }
          catch (Exception exception) {
            exception.printStackTrace();
          }
        }
      });
    }
  }

  public static boolean getArgument(String[] args, String name) {
    boolean found = false;
    int i = 0;
    // get arguments
    while (i < args.length && !found) {
      found = args[i].equals(name); // get argument name
      i++;
    }
    return found;
  }
}
