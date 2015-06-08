package nl.tue.declare.execution.msg;

import nl.tue.declare.datamanagement.XMLWorkItemBroker;
import nl.tue.declare.domain.instance.*;
import nl.tue.declare.execution.*;
import nl.tue.declare.utils.*;

public abstract class WorkItemMessage
    extends EventMessage {

  public static final String TYPE = "WORKITEM";

  protected String xml;
  protected String itemID = "";


  public WorkItemMessage(WorkItem item, Event event) {
    super(item.getActivity().getAssignment(), event);
    //this.item = item;
    this.itemID = item.getIdString();
    this.setXml(item);
  }

  public WorkItemMessage(String assignemntID, String activityID,
                         String eventName, String itemID, String xml) {
    super(assignemntID, activityID, eventName);
    this.xml = xml;
    this.itemID = itemID;
  }

  private void setXml(WorkItem item) {
    XMLWorkItemBroker broker = new XMLWorkItemBroker(item);
    xml = XMLParser.toString(broker.toElelemnt());
  }

  protected String info() {
    return itemID + IMessage.SEPARATOR + super.info() + IMessage.SEPARATOR + action() +IMessage.SEPARATOR +
        xml;
  }

  protected abstract String action();

  /**
   * msgType
   *
   * @return String
   * @todo Implement this nl.tue.declare.execution.msg.Message method
   */
  protected String msgType() {
    return TYPE;
  }

  public String getItemId() {
    return this.itemID;
  }

  public String getXML() {
    return this.xml;
  }
}
