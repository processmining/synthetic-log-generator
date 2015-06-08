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
public interface IMessage {
  static final String SEPARATOR = "|";
  /**
   * msg
   *
   * @return String
   */
  public String msg();
}
