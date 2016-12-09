package nl.tue.declare.execution.msg.engine.msg;

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
public class CloseAssignmentEngineMessage
    //extends AbstractAssignmentMessage {
    extends AssignmentMessage {

  public static final String TYPE = "CLOSE";

  public CloseAssignmentEngineMessage(Assignment assignment) {
    super(assignment);
  }

  public CloseAssignmentEngineMessage(String ID) {
    super(ID);
  }

  /**
   * msgBody
   *
   * @return String
   * @todo Implement this nl.tue.declare.execution.msg.Message method
   */
  protected String info() {
    return "";
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
}
