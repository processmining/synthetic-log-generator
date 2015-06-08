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

import nl.tue.declare.domain.instance.*;

public abstract class AssignmentMessage
    extends Message {


  public static final String CLASS = "ASSIGNMENT";
  protected String ID = "0";

  public AssignmentMessage(Assignment assignment) {
    this(Integer.toString(assignment.getId()));
  }

  public AssignmentMessage(String ID) {
    super();
    if (ID != null) {
      this.ID = ID;
    }
  }

  /**
   * msgClass
   *
   * @return String
   * @todo Implement this nl.tue.declare.execution.msg.Message method
   */
  public String msgClass() {
    return CLASS;
  }

  protected abstract String info();

  public String msgBody() {
    return id() + SEPARATOR + this.info();
  }

  public String id() {
    return ID;
  }
}
