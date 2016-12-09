package nl.tue.declare.execution.msg.worklist;

import nl.tue.declare.domain.instance.Event;
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
public class RequestEventMessage
    extends WorkItemMessage {

  public static final String ACTION = "REQUEST";

  public RequestEventMessage(WorkItem wi, Event event) {
    super(wi, event);
  }

  public RequestEventMessage(String ID, String eventID, String eventName,
                             String itemID, String xml) {
    super(ID, eventID, eventName, itemID, xml);
  }

  protected String action() {
    return ACTION;
  }
}
