package nl.tue.declare.logging;

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

//import nl.tue.declare.appl.framework.yawl.external.*;
import yawlservice.*;

public class FrameworkLogWriter
    extends LogWriter {

  private static final String CONNECTED = "CONNECTED";
  private static final String ARRIVED = "ARRIVED";
  private static final String RETOURNED = "RETOURNED";


  /**
   * FrameworkLogWritter
   */
  public FrameworkLogWriter() {
    super();
  }

  private void prettyWrite(ExternalWorkItem external, String event) {
    super.write(event + " " + external.getEngineID() + " " +
                external.getDecompositionID());
  }

  public void connected() {
    super.write(CONNECTED);
  }

  public void arrived(ExternalWorkItem external) {
    prettyWrite(external, ARRIVED);
  }

  public void retourned(ExternalWorkItem external) {
    prettyWrite(external, RETOURNED);
  }
}
