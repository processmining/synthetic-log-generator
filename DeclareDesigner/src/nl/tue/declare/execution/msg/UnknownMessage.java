package nl.tue.declare.execution.msg;

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
public class UnknownMessage
    implements IMessage {

  /**
   *
   * @return String
   * @todo Implement this nl.tue.declare.execution.msg.IMessage method
   */
  public String msg() {
    return "Unknown message";
  }

  /**
   * UnknownRequest
   */
  public UnknownMessage() {
    super();
  }

}
