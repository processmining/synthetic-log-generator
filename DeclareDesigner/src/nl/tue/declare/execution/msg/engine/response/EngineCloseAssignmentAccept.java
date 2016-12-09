package nl.tue.declare.execution.msg.engine.response;

import nl.tue.declare.domain.instance.*;
import nl.tue.declare.execution.msg.*;

public class EngineCloseAssignmentAccept
    extends AssignmentMessage {

  public static final String TYPE = "CLOSE_ACCEPT";

  public EngineCloseAssignmentAccept(Assignment assignment) {
    super(assignment);
  }

  public EngineCloseAssignmentAccept(String ID) {
    super(ID);
  }

  /**
   * info
   *
   * @return String
   * @todo Implement this nl.tue.declare.execution.msg.AssignmentMessage
   *   method
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
