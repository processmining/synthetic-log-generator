package nl.tue.declare.execution.msg.worklist;

import nl.tue.declare.execution.WorkItem;
import nl.tue.declare.execution.msg.WorkItemMessage;
import nl.tue.declare.domain.instance.Event;
import yawlservice.*;

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
public class ForwardEventMessage extends WorkItemMessage {

  public static final String ACTION = "FORWARD";
  private String ec;


  public ForwardEventMessage(WorkItem wi, Event event, ExternalCase ec) {
    super(wi,event);
    this.ec = ec.toXML();
  }

  public ForwardEventMessage(String ID, String eventID, String eventName,
                             String itemID, String xml, String ec) {
    super(ID, eventID, eventName, itemID, xml);
    this.ec = ec;
  }

  protected String action() {
    return ACTION;
  }

  protected String info() {
    return super.info() + SEPARATOR + ec;
  }

  public String getExternalCase(){
    return ec;
  }
}
