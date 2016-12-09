package nl.tue.declare.execution.msg.engine.msg;

import nl.tue.declare.domain.instance.*;
import nl.tue.declare.execution.*;
import nl.tue.declare.execution.msg.*;
import nl.tue.declare.utils.*;
import nl.tue.declare.datamanagement.assignment.*;

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

public class StateAssignmentMessage
    extends AssignmentMessage {

  public static final String TYPE = "STATE";

  private String state;

  public StateAssignmentMessage(Assignment assignment, AssignmentState state) {
    super(assignment);
    this.setStateString(state);
  }

  public StateAssignmentMessage(String ID, String state) {
    super(ID);
    this.state = state;
  }

  private void setStateString(AssignmentState state) {
    XMLAssignmnetStateBroker broker = new XMLAssignmnetStateBroker(state);
    this.state = XMLParser.toString(broker.toElelemnt());
  }

  /**
   * info
   *
   * @return String
   * @todo Implement this
   *   nl.tue.declare.execution.msg.engine.msg.AbstractAssignmentMessage
   *   method
   */
  protected String info() {
    return state;
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

  public String getState() {
    return this.state;
  }
}
