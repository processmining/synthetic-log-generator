package nl.tue.declare.execution.msg.worklist;


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
public class MessageLogIn
    extends AbstractLoggingMessage {

  public static final String TYPE = "LOGIN";

  public MessageLogIn(String id, String username, String password) {
    super(id, username, password);
  }

  /**
   * type
   *
   * @return String
   * @todo Implement this
   *   nl.tue.declare.execution.client.msg.AbstractLoggingMessage method
   */
  /* protected String type() {
     return TYPE;
   }*/

  protected String msgType() {
    return TYPE;
  }
}
