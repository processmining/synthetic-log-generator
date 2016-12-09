package nl.tue.declare.execution.msg.engine.msg;

import nl.tue.declare.domain.instance.*;

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
public class OpenAssignmentMessage
    extends ModelAssignmentMessage {

  public static final String TYPE = "OPEN";

  public OpenAssignmentMessage(Assignment assignment, String assignmentModel) {
    super(assignment, assignmentModel);
  }

  public OpenAssignmentMessage(String ID, String assignmentModel) {
    super(ID, assignmentModel);
  }

  protected String msgType() {
    return TYPE;
  }
}
