package nl.tue.declare.execution;

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
import java.util.*;

import nl.tue.declare.domain.instance.*;
import nl.tue.declare.utils.*;

public class Execution
    implements IWorkItemStateListener {

  private Assignment assignment;
  private ArrayList<WorkItem> items;
  /**
   *
   * @param assignment Assignment
   */
  public Execution(Assignment assignment) {
    super();
    this.setAssignment(assignment);
    this.items = new ArrayList<WorkItem>();
  }

  public void setAssignment(Assignment assignment) {
    this.assignment = assignment;
  }

  public Assignment getAssignment() {
    return this.assignment;
  }

  private int nextItemId() {
    int id = 1;
    if (items.size() > 0) {
      id = items.get(items.size() - 1).getId() + 1;
    }
    return id;
  }

  /**
   *
   * @param id int
   * @return WorkItem
   */
  public WorkItem getItem(int id) {
    boolean found = false;
    Iterator<WorkItem> iterator = items.iterator();
    WorkItem result = null;
    while (!found && iterator.hasNext()) {
      WorkItem item = iterator.next();
      found = item.getId() == id;
      if (found) {
        result = item;
      }
    }
    return result;
  }

  public Iterator<WorkItem> items() {
    return items.iterator();
  }

  public boolean allowedEvent(Event event) {
//    boolean allowed = items.isEmpty() && (event instanceof StartedEvent);
    if (event.getType().equals(Event.Type.STARTED)) {
      return true; // in any case allow to start work items
    }
    boolean allowed = false;
    Iterator<WorkItem> iterator = items.iterator();
    while (iterator.hasNext() && !allowed) {
      WorkItem wi = iterator.next();
      allowed = wi.possible(event);
    }
    return allowed;
  }

  public Collection<WorkItem> workItems(Event event) {
//    boolean allowed = items.isEmpty() && (event instanceof StartedEvent);
    Collection<WorkItem> list = new ArrayList<WorkItem>();
    Iterator<WorkItem> iterator = items.iterator();
    while (iterator.hasNext()) {
      WorkItem wi = iterator.next();
      if (wi.possible(event))
      list.add(wi);
    }
    return list;
  }

  public WorkItem getStartedItem(Activity activity) {
    boolean found = false;
    Iterator<WorkItem> iterator = items.iterator();
    WorkItem wi = null;
    while (iterator.hasNext() && !found) {
      wi = iterator.next();
      if (wi.getActivity().equals(activity)) {
        if (wi.started()) {
          found = !wi.finalized();
        }
      }
    }
    return found ? wi : null;
  }

  private void addWorkItem(WorkItem wi, Event event) {
    //Activity activity = (Activity)this.assignment.activityDefinitionWithId(event.getActivity().getId());
    //WorkItem item = new WorkItem(this.nextItemId(), activity,event.getUser());
	  wi.setId(this.nextItemId());
    if (!items.contains(wi)) {
      items.add(wi);
    }
  }

  /**
   * Create a new event for the work item. If the event is of type StartedEvent, a new work item
   * is created, added to the list and new started work item event is added to the new work item.
   * @param item WorkItem
   * @param event Event
   * @return WorkItemEvent
   */
  public void add(WorkItem item, Event event) {
    if (event != null) {
      if (allowedEvent(event)) {
        if (event.getType().equals(Event.Type.STARTED)) {
          this.addWorkItem(item,event);
        }
       item.add(event);
     }
    }
  }

  /**
   *
   * @param item String
   * @param event Event
   * @return WorkItemEvent
   */
  public void execute(WorkItem item, Event event) {
    if (event.getType().equals(Event.Type.STARTED)) {
      add(item, event);
    }
    else {
      WorkItem workItem = getItem(item.getId());
      if (workItem != null) {
        workItem.copyData(item);
        add(workItem, event);
      }
    }
  }

  public void print() {
    SystemOutWriter.singleton().println("execution: " + Integer.toString(assignment.getId()));
    Iterator<WorkItem> i = items.iterator();
    while (i.hasNext()) {
      i.next().print("  ");
    }

  }

  public void clear() {
    items.clear();
  }

  public boolean canComplete() {
    boolean ok = true;
    Iterator<WorkItem> i = items.iterator();
    while (i.hasNext() && ok) {
      ok = i.next().finalized();
    }
    return ok;
  }
}
