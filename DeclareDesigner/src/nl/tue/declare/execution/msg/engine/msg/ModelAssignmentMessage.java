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
public abstract class ModelAssignmentMessage
    extends AssignmentMessage {

  protected String assignmentModel;

  public ModelAssignmentMessage(Assignment assignment, String assignmentModel) {
    super(assignment);
    this.assignmentModel = assignmentModel;

  }

  public ModelAssignmentMessage(String ID, String assignmentModel) {
    super(ID);
    this.assignmentModel = assignmentModel;

  }

  protected String info() {
    return this.assignmentModel;
  }

  public String getModel() {
    return this.assignmentModel;
  }
}
