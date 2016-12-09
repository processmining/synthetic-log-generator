package nl.tue.declare.execution.msg.engine.response;

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
public class EngineLogInRejectPassword
    extends EngineLogInReject {
  public static final String DESCRIPTION = "The password is not correct.";

  public EngineLogInRejectPassword() {
    super("The password is not correct.");
  }

  /*String desc() {
    return DESCRIPTION;
     }*/

  protected String msgBody() {
    return DESCRIPTION;
  }
}
