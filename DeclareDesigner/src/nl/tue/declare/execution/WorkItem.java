package nl.tue.declare.execution;

import java.util.*;

import nl.tue.declare.domain.*;
import nl.tue.declare.domain.instance.*;
import nl.tue.declare.domain.model.*;
import nl.tue.declare.domain.organization.*;
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
 * @author Maja Pesic
 * @version 1.0
 */

public class WorkItem
    extends Base {

  private User user;
  private Activity activity;
  private List<Event> events;
  private ArrayList<WorkItemData> data;
  private HashMap<String,String> attributes;

  /**
   *
   * @param id int
   * @param activity Activity
   * @param user User
   */
  public WorkItem(int id, Activity activity, User user) {
    super(id);
    this.activity = activity;
    this.user = user;
    this.events = new ArrayList<Event>();
    this.data = new ArrayList<WorkItemData>();
    this.attributes = new HashMap<String, String>();
    this.createData();
  }

  public void setId(int id) {
    super.setId(id);
  }

  /**
   *
   * @return User
   */
  public User getUser() {
    return this.user;
  }

  /**
   *
   * @return Activity
   */
  public Activity getActivity() {
    return this.activity;
  }

  public Iterator<Event> getEvents() {
    return events.iterator();
  }

  /**
   *
   * @param event AbstractEvent
   * @return WorkItemEvent
   */
  public void add(Event event) {
    //WorkItemEvent itemEvent = null;
    //itemEvent = new WorkItemEvent(this, event);
	event.getTime().setCurrentTime();	
	event.setWorkItem(Integer.toString(this.getId()));
	event.getAttributes().putAll(this.attributes);
    this.events.add(event);
  }

  /**
   *
   */
  private void createData() {
    data.clear();
    if (this.activity != null) {
      Iterator<ActivityDataDefinition> i = activity.data();
      while (i.hasNext()) {
        ActivityDataDefinition data = i.next();
        if (data instanceof ActivityData) {
          this.data.add(this.creteDataElement( (ActivityData) data));
        }
      }
    }
  }

  /**
   *
   * @param data ActivityData
   * @return WorkItemData
   */
  private WorkItemData creteDataElement(ActivityData data) {
    return new WorkItemData(data);
  }

  /**
   *
   */
  public void writeData() {
    Iterator<WorkItemData> i = data.iterator();
    while (i.hasNext()) {
      i.next().write();
    }
  }

  /**
   *
   * @return int
   */
  public int dataCount() {
    return data.size();
  }

  /**
   *
   * @param index int
   * @return WorkItemData
   */
  public WorkItemData getDataAt(int index) {
    WorkItemData data = null;
    if (index < this.data.size()) {
      data = this.data.get(index);
    }
    return data;
  }

  /**
   *
   * @param data ActivityData
   * @return WorkItemData
   */
  public WorkItemData getData(ActivityData data) {
    Iterator<WorkItemData> i = this.data.iterator();
    boolean found = false;
    WorkItemData result = null;
    while (i.hasNext() && !found) {
      result = i.next();
      found = result.forData(data);
    }
    return found ? result : null;
  }

  /**
   *
   * @return WorkItemData
   * @param id int
   */
  public WorkItemData getDataWithId(int id) {
    Iterator<WorkItemData> i = this.data.iterator();
    boolean found = false;
    WorkItemData result = null;
    while (i.hasNext() && !found) {
      result = i.next();
      found = result.getData().getId() == id;
    }
    return found ? result : null;
  }

  private boolean exists(Event.Type type) {
    Iterator<Event> i = events.iterator();
    boolean found = false;
    while (i.hasNext() && !found) {
      Event current = i.next();
      found = (current.getType().equals(type));
    }
    return found;
  }

  public boolean started() {
    //Event started = new Event(this.getUser(), activity, Event.Type.STARTED);
    return exists(Event.Type.STARTED);
  }

  public boolean canceled() {
    //Event canceled = new Event(this.getUser(), activity, Event.Type.CANCELLED);
    return exists(Event.Type.CANCELLED);
  }

  public boolean completed() {
    //Event completed = new Event(this.getUser(), activity, Event.Type.COMPLETED);
    return exists(Event.Type.COMPLETED);
  }

  public boolean finalized() {
    boolean finalized = false;
    finalized = started();
    if (finalized) {
      finalized = completed();
      if (!finalized) {
        finalized = canceled();
      }
    }
    return finalized;
  }

  public boolean canComplete() {
    boolean canComplete = started();
    if (canComplete) {
      canComplete = !completed();
      if (canComplete) {
        canComplete = !canceled();
      }
    }
    return canComplete;
  }

  public boolean possible(Event event) {
    boolean possible = activity.equals(event.getActivity());
    if (possible) {
      if (event.getType().equals(Event.Type.STARTED)) {
        possible = !started();
      }
      else if (event.getType().equals(Event.Type.COMPLETED)) {
        possible = !finalized();
      }
      else if (event.getType().equals(Event.Type.CANCELLED)) {
        possible = !finalized();

      }
    }

    return possible;
  }

  public void print(String indent) {
    SystemOutWriter.singleton().println(indent + this.getIdString() +
                                        " -> " + activity.getName());
    Iterator<Event> i = events.iterator();
    while (i.hasNext()) {
      SystemOutWriter.singleton().println(indent + "   " +
                                          i.next().getProposition());
    }
  }

  public String toString() {
    return getId() + " " + activity.getName();
  }

  public void printData() {
    Iterator<WorkItemData> i = data.iterator();
    while (i.hasNext()) {
      i.next().print();
    }
  }

  void copyData(WorkItem other) {
    Iterator<WorkItemData> i = data.iterator();
    while (i.hasNext()) {
      WorkItemData myData = i.next();
      WorkItemData otherData = other.getData(myData.getData());
      if (otherData != null) {
        myData.copuValue(otherData);
      }
    }
  }

  public HashMap<String,String> getAttributes() {
    return attributes;
  }

}
