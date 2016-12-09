package nl.tue.declare.execution.msg.worklist;

import nl.tue.declare.domain.instance.*;
import nl.tue.declare.execution.msg.*;

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
public class MessageCloseAssignment
    extends AssignmentMessage {

  public final static String TYPE = "CLOSE";

  public MessageCloseAssignment(Assignment assignment) {
    super(assignment);
  }

  public MessageCloseAssignment(String ID) {
    super(ID);
  }

  /**
   * msgType
   *
   * @return String
   * @todo Implement this nl.tue.declare.execution.msg.Message method
   */
  protected String msgType() {
    return TYPE;
  }

  protected String info() {
    return "";
  }

}
