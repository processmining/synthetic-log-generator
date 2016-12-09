package nl.tue.declare.datamanagement;

import java.util.*;

import org.w3c.dom.*;
import nl.tue.declare.control.*;
import nl.tue.declare.domain.*;
import nl.tue.declare.domain.instance.*;
import nl.tue.declare.domain.organization.*;
import nl.tue.declare.execution.*;

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
public class XMLHistoryBroker
    extends XMLBroker {
  //EventLog log;
  Execution execution;
  ElementFactory factory;
  public XMLHistoryBroker(Execution execution /*,  EventLog log*/) {
    super("", "history");
    this.factory = new ElementFactory(this);
    // this.log = log;
    this.execution = execution;
  }

  public Element toElelemnt() {
    Element root = this.getDocumentRoot();
    if (execution != null) {
      factory.executionToElement(root, execution);
    }
    return root;
  }

  public void fromElement() {
    if (execution != null) {
      this.execution.clear();
      Element root = this.getDocumentRoot();
      factory.executionFromElement(this.execution, root);
    }
  }

  private class ElementFactory
      extends XMLElementFactory {
    private static final String WORK_ITEM_EVENT = "workitemevent";
    private static final String TIME = "timestamp";
    private static final String WORK_ITEM = "workitem";
    private static final String ACTIVITY = "activity";
    private static final String USER = "user";
    private static final String EVENT = "event";

    @SuppressWarnings("unused")
	XMLBroker broker;

    /**
     * ElementFactory
     *
     * @param aBroker XMLBroker
     */
    public ElementFactory(XMLBroker aBroker) {
      super(aBroker);
    }

    private void executionToElement(Element element, Execution execution) {
      Iterator<WorkItem> iterator = execution.items();
      while (iterator.hasNext()) {
        WorkItem event = iterator.next();
        Element item = this.workItemToElement(event);
        element.appendChild(item);
      }
    }

    private Element workItemToElement(WorkItem workItem) {
      Element element = baseToElement(workItem, WORK_ITEM);
      setAttribute(element, ACTIVITY, workItem.getActivity().getIdString());
      setAttribute(element, USER, workItem.getUser().getIdString());
      Iterator<Event> events = workItem.getEvents();
      while (events.hasNext()) {
        Event event = events.next();
        element.appendChild(this.workItemEventToElement(event));
      }
      return element;
    }

    private Element workItemEventToElement(Event workItemEvent) {
      Element element = createElement(WORK_ITEM_EVENT);
      setAttribute(element, TIME, workItemEvent.getTime().toString());
      setAttribute(element, EVENT, workItemEvent.getType().name());
      return element;
    }

    private void executionFromElement(Execution execution, Element element) {
      NodeList nodes = element.getElementsByTagName(WORK_ITEM);
      for (int i = 0; i < nodes.getLength(); i++) {
        Element event = (Element) nodes.item(i);
        workItemFromElement(event);
      }
    }

    private WorkItem workItemFromElement(Element element) {
      Base base = this.elementToBase(element);
      String activityString = element.getAttribute(ACTIVITY);
      String userString = element.getAttribute(USER);
      Activity activity = (Activity) execution.getAssignment().
          activityDefinitionWithId(Integer.parseInt(activityString));
      User user = Control.singleton().getOrganization().getUserWithID(Integer.
          parseInt(userString));
      WorkItem item = new WorkItem(base.getId(), activity, user);
      NodeList nodes = element.getElementsByTagName(WORK_ITEM_EVENT);
      for (int i = 0; i < nodes.getLength(); i++) {
        Element event = (Element) nodes.item(i);
        String timeString = event.getAttribute(TIME);
        String eventString = event.getAttribute(EVENT);
        Event e = new Event(user, activity, Event.Type.valueOf(eventString));
        e.getTime().parse(timeString);
      }
      return item;
    }
  }
}
