package nl.tue.declare.execution.msg.engine.response;

import nl.tue.declare.domain.instance.*;
import nl.tue.declare.execution.msg.*;

public class EngineCloseAssignmentReject
    extends AssignmentMessage {
  public static final String TYPE = "CLOSE_REJECT";

  private String reason;

  public EngineCloseAssignmentReject(Assignment assignment, String info) {
    super(assignment);
    this.reason = info;
  }

  public EngineCloseAssignmentReject(String ID, String info) {
    super(ID);
    this.reason = info;
  }

  /**
   * info
   *
   * @return String
   * @todo Implement this nl.tue.declare.execution.msg.AssignmentMessage
   *   method
   */
  protected String info() {
    return reason;
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

  public String getReason() {
    return this.reason;
  }
}
