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

public abstract class EventMessage
    extends AssignmentMessage {

  private String activityID = "";
  private String eventName = "";

  public EventMessage(Assignment assignment, Event event) {
    super(assignment);
    this.activityID = event.getActivity().getIdString();
    this.eventName = event.getType().name();
  }

  public EventMessage(String assignemntID, String activityID, String eventName) {
    super(assignemntID);
    this.activityID = activityID;
    this.eventName = eventName;
  }

  protected String info() {
    return activityID + IMessage.SEPARATOR + eventName;
  }

  public String getActivityId() {
    return this.activityID;
  }

  public String getEventName() {
    return this.eventName;
  }
}
