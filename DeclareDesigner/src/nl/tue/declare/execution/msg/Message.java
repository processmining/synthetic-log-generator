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
public abstract class Message
    implements IMessage {

  protected abstract String msgClass();

  protected abstract String msgType();

  protected abstract String msgBody();

  /**
   *
   * @return String
   * @todo Implement this nl.tue.declare.execution.msg.IMessage method
   */
  public String msg() {
    String msg = this.msgClass() + SEPARATOR + this.msgType() +
        SEPARATOR + this.msgBody();
    //return msg.replaceAll("[\\r\\f]", "").replaceAll("\n","");
    return msg.replaceAll("\\r", "").replaceAll("\\n","").replaceAll("\\f","");
  }
}
