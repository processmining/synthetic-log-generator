package nl.tue.declare.execution;

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
import java.util.*;

import nl.tue.declare.domain.instance.*;
import nl.tue.declare.domain.organization.*;
import nl.tue.declare.execution.IWorkItemStateListener;

public class AssignmentState {

  private final String SEPARATOR = "!";
  private Assignment assignment;
  private IWorkItemStateListener workItemListener;

  private Collection<EventState> events;

  private LTLInstanceExecutionHandler listener;

  public AssignmentState(Assignment assignment) {
    super();
    this.assignment = assignment;
    this.events = Collections.synchronizedList(new ArrayList<EventState>());
    this.listener = null;
    this.workItemListener = null;
  }

  /**
   * addListener
   *
   * @param listener IAssignmentStateListener
   */
  public void addListener(LTLInstanceExecutionHandler listener) {
    this.listener = listener;
  }

  public void addWorkItemListener(IWorkItemStateListener workItemListener) {
    this.workItemListener = workItemListener;
  }

  public Iterable<EventState> getPossibleEvents() {
    return events;
  }

  /**
   * addEvent
   *
   * @param event AbstractEvent
   * @return EventState
   */
  public AssignmentState.EventState addEvent(Event event) {
    AssignmentState.EventState state = null;
    if (event != null) {
      state = this.eventState(event);
      if (!events.contains(state)) {
        this.events.add(state);
      }
    }
    return state;
  }

  public void clear() {
    events.clear();
  }

  public AssignmentState.EventState eventState(Event event) {
    EventState state = null;
    boolean found = false;
    int i = 0;
    synchronized (events) {
      while (i < this.eventCount() && !found) {
        state = this.eventAt(i++);
        found = state.event().equals(event);
      }
    }
    if (!found) {
      state = new EventState(event);
    }
    return state;
  }

  /**
   * clearEvents
   */
  private void clearEvents() {
    this.events.clear();
  }

  /**
   * eventCount
   *
   * @return int
   */
  public int eventCount() {
    return this.events.size();
  }

  /**
   * eventAt
   *
   * @param index int
   * @return AbstractEvent
   */
  public EventState eventAt(int index) {
    EventState state = null;
    synchronized (events) {
      if (index < this.eventCount()) {
        int i = 0;
        Iterator<EventState> iterator = events.iterator();
        while (iterator.hasNext() && (i++ <= index)) {
          state = iterator.next();
        }
      }
    }
    return state;
  }

  public Assignment getAssignment() {
    return assignment;
  }

  /**
   *
   */
  private void allowedAssignementEvents() throws Throwable {
    if (getAssignment() != null) {
      this.clearEvents();
      for (int i = 0; i < getAssignment().activitiesCount(); i++) {
        Activity activity = getAssignment().activityAt(i);
        // test all event types that can be taken into account
        Iterator<Event> iterator = this.possibleEvents(activity).
            iterator();
        while (iterator.hasNext()) {
          Event event = iterator.next();

          if (this.allowedEvent(event)) {
            this.addEvent(event);
          }
        }
      }

      // check for every possible event if it would violate some constraints
      synchronized (events) {
        Iterator<AssignmentState.EventState> iterator = this.events.iterator();
        while (iterator.hasNext()) {
          AssignmentState.EventState state = iterator.next();
          if (this.listener != null) {
            Iterator<Constraint>
                violated = this.listener.violatesConstraints(state.event()).
                iterator();
            while (violated.hasNext()) {
              state.addViolates(violated.next());
            }
          }
        }
      }
    }
  }

  /**
   *
   */
  private void violatedConstraints() throws Throwable {
    if (getAssignment() != null) {
      for (int i = 0; i < getAssignment().constraintDefinitionsCount(); i++) {
        Constraint constraint = getAssignment().constraintAt(i);
        if (listener != null) {
          State state = listener.constraintState(constraint);
          constraint.setState(state);
        }
      }
    }
  }

  private void assignmentState() {
    if (listener != null) {
      getAssignment().setState(listener.assignmentState());
    }
  }

  public synchronized void createState() throws Throwable {
    this.assignmentState();
    this.allowedAssignementEvents();
    this.violatedConstraints();
  }

  /**
   *
   * @param activity Activity
   * @return Collection
   */
  private Collection<Event> possibleEvents(Activity activity) {
    return Event.possibleEvents(activity);
  }

  /**
   * allowedEvent
   *
   * @param event AbstractEvent
   * @return boolean
   */
  public boolean allowedEvent(Event event) {
    boolean allowed = true;
    if (event != null) {
      /**
       * This line is currently disabled because I do not want it. However, this lline works without error!
       * If you want this option, just enable this line
       *
       * allowed = this.workItemAllows(event); // first ask fow work item logic, e.g., can not complete before start activity         *
       *   if (allowed)
       */
      allowed = this.workItemAllows(event); // first ask fow work item logic, e.g., can not complete before start activity         *
      if (allowed && listener != null) {
        allowed = listener.allowedEvent(event);
      }
    }
    return allowed;
  }

  private boolean workItemAllows(Event event) {
    boolean ok = true;
    if (this.workItemListener != null) {
      ok = workItemListener.allowedEvent(event);
    }
    return ok;
  }

  public boolean containsEvent(Event event) {
    boolean found = false;
    int i = 0;
    synchronized (events) {
      while (i < this.eventCount() && !found) {
        EventState state = this.eventAt(i++);
        found = state.event().equals(event);
      }
    }
    return found;
  }

  public boolean possible(Event.Type type, Activity activity) {
    boolean found = false;
    int i = 0;
    synchronized (events) {
      while (i < this.eventCount() && !found) {
        EventState state = this.eventAt(i++);
        found = (state.event().getType().equals(type)) &&
            state.event.getActivity().equals(activity);
      }
    }
    return found;
  }

  /**
   * toString
   *
   * @return String
   */
  public String toString() {
    String events = "";
    synchronized (events) {
      for (int i = 0; i < this.eventCount(); i++) {
        AssignmentState.EventState eventState = this.eventAt(i);
        if (!events.equals("")) {
          events += ";";
        }
        events += eventState.toString();
      }
    }
    String constraints = "";
    return events + this.SEPARATOR + constraints;
  }

  /**
   * getState()
   *
   * @return int
   */
  public State getState() {
    return this.getAssignment().getState();
  }

  public class EventState {
    Event event;
    Collection<Constraint> violated;
    /**
     * EventState
     *
     * @param event AbstractEvent
     */
    EventState(Event event) {
      super();
      this.event = event;
      this.violated = new ArrayList<Constraint>();
    }

    /**
     *
     * @return int
     */
    public int violatedCout() {
      return this.violated.size();
    }

    /**
     *
     * @param violate Constraint
     * @return boolean
     */
    boolean violates(Constraint violate) {
      return this.violated.contains(violate);
    }

    /**
     *
     * @param violate Constraint
     * @return boolean
     */
    public boolean addViolates(Constraint violate) {
      boolean canAdd = !this.violates(violate);
      if (canAdd) {
        canAdd = violated.add(violate);
      }
      return canAdd;
    }

    /**
     *
     * @param violate Constraint
     * @return boolean
     */
    boolean removeViolates(Constraint violate) {
      boolean remove = this.violates(violate);
      if (remove) {
        remove = this.violated.remove(violate);
      }
      return remove;
    }

    /**
     *
     * @param index int
     * @return Constraint
     */
    public Constraint violatesAt(int index) {
      Constraint violate = null;
      if (index < this.violatedCout()) {
        int i = 0;
        Iterator<Constraint> iterator = violated.iterator();
        while (iterator.hasNext() && i++ <= index) {
          violate = iterator.next();
        }
      }
      return violate;
    }

    /**
     *
     * @return AbstractEvent
     */
    public Event event() {
      return this.event;
    }

    /**
     * toString
     *
     * @return String
     */
    public String toString() {
      String violate = "";
      for (int j = 0; j < this.violatedCout(); j++) {
        Constraint constraint = this.violatesAt(j);
        if (!violate.equals("")) {
          violate += ",";
        }
        violate += constraint.getId();
      }
      return this.event().getProposition() + "{" + violate + "}";
    }
  }

  public boolean enabled() {
    return this.events.size() > 0;
  }

  /**
   *
   * @return Object
   */
  public Object clone() {
    AssignmentState clone = new AssignmentState(this.assignment);
    clone.addListener(this.listener);
    clone.events.addAll(this.events);
    return clone;
  }

  /**
   *
   * @param eventState EventState
   */
  public void removeEvent(EventState eventState) {
    this.events.remove(eventState);
  }

  /**
   *
   * @param user RemoteWorklist
   * @return AssignmentState
   */
  public AssignmentState authorize(User user) {
    AssignmentState authorized = (AssignmentState)this.clone();

    for (int i = 0; i < this.eventCount(); i++) {
      AssignmentState.EventState eventState = this.eventAt(i);
      if (!authorized.getAssignment().authorized(eventState.event().
                                                 getActivity(),
                                                 user)) {
        authorized.removeEvent(eventState);
      }
      /* else {
             if ( eventState.event() instanceof StartedEvent) // log the AUTHORIZING event in the log file
        ProcessLogWriter.singleton().assign(this.assignment, eventState.event().getActivity(), user);
            }*/
    }
    return authorized;
  }

}
