package nl.tue.declare.execution.msg.engine.msg;

import nl.tue.declare.datamanagement.XMLHistoryBroker;
import nl.tue.declare.domain.instance.*;
import nl.tue.declare.execution.*;
import nl.tue.declare.execution.msg.*;
import nl.tue.declare.utils.*;

/**
 * <p>Title: DECLARE</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: TU/e</p>
 *
 * @author not attributable
 * @version 1.0
 */
public class HistoryAssignmentMessage
    extends AssignmentMessage {

  public static final String TYPE = "HISTORY";

  //Execution execution;
  private String execution = "";

  public HistoryAssignmentMessage(Assignment assignment, Execution execution) {
    super(assignment);
    XMLHistoryBroker broker = new XMLHistoryBroker(execution);
    this.execution = XMLParser.toString(broker.toElelemnt());
  }

  public HistoryAssignmentMessage(String ID, String execution) {
    super(ID);
    if (execution != null) {
      this.execution = execution;
    }
  }

  /**
   * info
   *
   * @return String
   * @todo Implement this nl.tue.declare.execution.msg.AssignmentMessage
   *   method
   */
  protected String info() {
    //XMLHistoryBroker broker = new XMLHistoryBroker(execution);
    //return XMLParser.toString(broker.toElelemnt());
    return this.execution;
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

  public String getHistory() {
    return this.execution;
  }
}
