package nl.tue.declare.datamanagement.assignment;

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
import org.w3c.dom.*;

import nl.tue.declare.datamanagement.XMLBroker;
import nl.tue.declare.datamanagement.XMLElementFactory;
import nl.tue.declare.domain.instance.Event;
import nl.tue.declare.domain.organization.User;
import nl.tue.declare.domain.instance.Activity;
import nl.tue.declare.domain.instance.Constraint;
import nl.tue.declare.domain.instance.State;
import nl.tue.declare.execution.*;

import java.util.StringTokenizer;

public class XMLAssignmnetStateBroker
    extends XMLBroker {
  AssignmentState state;
  ElementFactory factory;

  public XMLAssignmnetStateBroker(AssignmentState state) {
    super("", "assignment");
    this.state = state;
    factory = new ElementFactory(this);
  }

  public Element toElelemnt() {
    Element root = this.getDocumentRoot();
    factory.toElelemnt(root);
    return root;
  }

  public void fromElement() {
    if (state != null) {
      this.state.clear();
      Element root = this.getDocumentRoot();
      factory.fromElement(root);
    }
  }

  /**
     * <p>Title: DECLARE</p>
     *
     * <p>Description: </p>
     *
     * <p>Copyright: Copyright (c) 2006</p>
     *
     * <p>Company: TU/e</p>
     * @author Maja Pesic
     * @version 1.0
     */
  private class ElementFactory
      extends XMLElementFactory {

      private static final String STATE = "state";

      private static final String EVENTS = "events";
      private static final String EVENT = "event";
      private static final String EVENTNAME = "name";
      private static final String VIOLATES = "violates";
      private static final String VIOLATESID = "constraint";

      private static final String CONSTRAINTS = "constraints";
      private static final String CONSTRAINT = "constraint";
      private static final String CONSTRAINTID = "id";
      private static final String CONSTRAINTSTATE = "state";
      private static final String CONSTRAINTSTATUS = "status";


      /**
       * ElementFactory
       *
       * @param aBroker XMLBroker
       */
      public ElementFactory(XMLBroker aBroker) {
        super(aBroker);
      }

      public void toElelemnt(Element element) {
        if (state != null) {
          element.setAttribute(STATE,state.getAssignment().getState().name());
          Element events = eventsToElement();
          Element constraints = constraintsToElement();
          element.appendChild(events);
          element.appendChild(constraints);
        }
      }

      private Element eventsToElement() {
        Element events = createElement(EVENTS);
        for (int i = 0; i < state.eventCount(); i++) {
          Element event = this.eventToElement(state.eventAt(i));
          events.appendChild(event);
        }
        return events;
      }

      private Element eventToElement(AssignmentState.EventState eventState) {
        Element event = createElement(EVENT);
        setAttribute(event, EVENTNAME, eventState.event().getProposition());
        for (int i = 0; i < eventState.violatedCout(); i++) {
          Element violates = createElement(VIOLATES);
          setAttribute(violates, VIOLATESID,
                       Integer.toString(eventState.violatesAt(i).getId()));
          event.appendChild(violates);
        }
        return event;
      }

      private Element constraintsToElement() {
        Element constraints = createElement(CONSTRAINTS);
        for (int i = 0; i < state.getAssignment().constraintDefinitionsCount(); i++) {
          Element constraint = createElement(CONSTRAINT);
          Constraint c = state.getAssignment().constraintAt(i);
          setAttribute(constraint, CONSTRAINTID, Integer.toString(c.getId()));
          setAttribute(constraint, CONSTRAINTSTATE, c.getState().toString());
          setAttribute(constraint, CONSTRAINTSTATUS, Boolean.toString(c.getStatus()));
          constraints.appendChild(constraint);
        }
        return constraints;
      }

      void fromElement(Element element) {
        if (element != null && state != null) {
          String stateString = element.getAttribute(STATE);
          state.getAssignment().setState(State.valueOf(stateString));
          this.eventsFromElement(element);
          this.constraintsFromElement( element);
        }
      }

      void eventsFromElement(Element element) {
        Element events = getFirstElement(element, EVENTS);
        NodeList nodes = events.getElementsByTagName(EVENT);
        for (int i = 0; i < nodes.getLength(); i++) {
          Element event = (Element) nodes.item(i);
          this.eventFromElement(event);
        }
      }

      void eventFromElement(Element element) {
        String eventString = element.getAttribute(EVENTNAME);
        StringTokenizer st = new StringTokenizer(eventString, Event.DOT);
        String name = st.nextToken();
        Activity activity = state.getAssignment().activityWithName(name);
        if (activity != null) {
          String type = st.nextToken();
          Event event = null;
          User user = new User(0);

          event = new Event(user, activity, Event.Type.valueOf(type));

          AssignmentState.EventState eventState = state.addEvent(event);
          this.eventStateFromElement(eventState, element);
        }
      }

      void eventStateFromElement(AssignmentState.EventState eventState,
                                 Element element) {
        if (eventState != null) {
          // get violated constraints
          NodeList nodes = element.getElementsByTagName(VIOLATES);
          for (int i = 0; i < nodes.getLength(); i++) {
            Element violates = (Element) nodes.item(i);
            String violatesIdString = violates.getAttribute(VIOLATESID);
            int id = Integer.parseInt(violatesIdString);
            Constraint constraint = state.getAssignment().constraintWithId(id);
            eventState.addViolates(constraint);
          }
        }

      }

      void constraintsFromElement(Element element) {
        Element constraints = getFirstElement(element, CONSTRAINTS);
        NodeList nodes = constraints.getElementsByTagName(CONSTRAINT);
        for (int i = 0; i < nodes.getLength(); i++) {
          Element node = (Element) nodes.item(i);
          String idString = node.getAttribute(CONSTRAINTID);
          String stateString = node.getAttribute(CONSTRAINTSTATE);
          String statusString = node.getAttribute(CONSTRAINTSTATUS);

          int id = Integer.parseInt(idString);
          boolean status = Boolean.parseBoolean(statusString);

          Constraint constraint = state.getAssignment().constraintWithId(id);
          constraint.setState(State.valueOf(stateString));
          try {
            constraint.setStatus(status);
          }
          catch (Exception ex) {
          }
        }
      }
  }
}
