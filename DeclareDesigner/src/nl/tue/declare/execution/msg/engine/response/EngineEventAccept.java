package nl.tue.declare.execution.msg.engine.response;

import nl.tue.declare.datamanagement.XMLWorkItemBroker;
import nl.tue.declare.domain.instance.*;
import nl.tue.declare.domain.organization.*;
import nl.tue.declare.execution.*;
import nl.tue.declare.execution.msg.*;

public class EngineEventAccept
    extends WorkItemMessage {

  public static final String ACTION = "EVENT_ACCEPT";

  public EngineEventAccept(WorkItem wi, Event event) {
    super(wi,event);
  }

  public EngineEventAccept(String ID, String eventID, String eventName,
                           String itemID, String xml) {
    super(ID, eventID, eventName, itemID, xml);
  }

  public WorkItem createItem(User user, Activity activity) {
    int id = Integer.parseInt(this.itemID);
    WorkItem workItem = new WorkItem(id, activity, user);
    XMLWorkItemBroker broker = new XMLWorkItemBroker(workItem);
    broker.readDocumentString(this.xml);
    broker.fromElement();
    return workItem;
  }

  protected String action() {
    return ACTION;
  }
}
