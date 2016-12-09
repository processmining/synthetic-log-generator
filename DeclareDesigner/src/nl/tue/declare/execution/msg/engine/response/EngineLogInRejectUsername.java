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
public class EngineLogInRejectUsername
    extends EngineLogInReject {

  public static final String DESCRIPTION = "The username does not exist.";

  public EngineLogInRejectUsername() {
    super("The username does not exist.");
  }

  /*String desc() {
    return DESCRIPTION;
     }*/

  protected String msgBody() {
    return DESCRIPTION;
  }
}
