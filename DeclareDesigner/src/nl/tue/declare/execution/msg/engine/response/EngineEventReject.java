package nl.tue.declare.execution.msg.engine.response;

/**
 *
 */
import nl.tue.declare.domain.instance.*;
import nl.tue.declare.execution.*;
import nl.tue.declare.execution.msg.*;

/**
 *
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
public class EngineEventReject
    extends WorkItemMessage {

  public static final String ACTION = "EVENT_REJECT";
  private String reason;

  public EngineEventReject(WorkItem item, Event event, String reason) {
    super(item, event);
    this.reason = reason;
  }

  public EngineEventReject(String ID, String eventID, String eventName,
                           String itemID, String xml, String reason) {
    super(ID, eventID, eventName, itemID, xml);
    //this.item = event.getWorkItem();
    this.reason = reason;
  }


  protected String info() {
    return super.info() +
        SEPARATOR + reason;
  }

  protected String action() {
    return ACTION;
  }
}
