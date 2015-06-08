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
public class EngineLogInAccept
    extends AbstractEngineLoggingMessage {

  public static final String TYPE = "LOGIN_ACCEPT";
  String id;
  public EngineLogInAccept(String id) {
    super();
    this.id = id;
  }

  /*String type() {
    return TYPE;
     }*/

  String desc() {
    return "";
  }

  protected String msgType() {
    return TYPE;
  }

  protected String msgBody() {
    return id;
  }

  public String getId() {
    return id;
  }
}
