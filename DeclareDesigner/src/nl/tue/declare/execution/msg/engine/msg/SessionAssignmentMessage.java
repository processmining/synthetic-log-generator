package nl.tue.declare.execution.msg.engine.msg;

import nl.tue.declare.domain.instance.*;
import nl.tue.declare.execution.*;
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
public class SessionAssignmentMessage
    extends AssignmentMessage {

  public static final String TYPE = "SESSION";
  private Session session;

  public SessionAssignmentMessage(Assignment assignment, Session session) {
    super(assignment);
    this.session = session;
  }

  /**
   * info
   *
   * @return String
   * @todo Implement this nl.tue.declare.execution.msg.AssignmentMessage
   *   method
   */
  protected String info() {
    return session.toString();
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
