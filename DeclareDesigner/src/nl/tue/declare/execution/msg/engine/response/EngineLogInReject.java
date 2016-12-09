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
public class EngineLogInReject
    extends AbstractEngineLoggingMessage {

  public static final String TYPE = "LOGIN_REJECT";
  private String reason;

  public EngineLogInReject(String reason) {
    super();
    if (reason != null) {
      this.reason = reason;
    }
    else {
      this.reason = "Unknown reason!";
    }
  }

  /* String type() {
     return TYPE;
   }*/

  //abstract String desc();

  protected String msgType() {
    return TYPE;
  }

  protected String msgBody() {
    return "";
  }

  public String getReason() {
    return reason;
  }
}
