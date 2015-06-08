package nl.tue.declare.graph.assignment;

import java.util.*;

import java.awt.*;

import org.jgraph.graph.*;
import nl.tue.declare.domain.instance.*;
import nl.tue.declare.domain.instance.Event;
import nl.tue.declare.domain.model.*;
import nl.tue.declare.domain.template.Parameter;
import nl.tue.declare.execution.*;
import nl.tue.declare.graph.*;
import nl.tue.declare.graph.model.*;
import nl.tue.declare.appl.worklist.*;

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
public class AssignmentView
    extends AssignmentModelView implements IAssignmentMarqueeListener {

  AssignmentState state;

  //IAssignmentViewListener listener;
  IAssignmentExecutionListener listener;

  //AssignmentMarqueeHandler marqueeHandler = null;

  /**
   *
   * @param state Assignment
   */
  public AssignmentView(AssignmentState state) {
    super(state.getAssignment());
    this.state = state;
    AssignmentMarqueeHandler marqueeHandler = new AssignmentMarqueeHandler(this.
        graph);
    marqueeHandler.setListener(this);
    graph.setMarqueeHandler(marqueeHandler);
  }

  protected ActivityDefinitonCell createActivityCell(ActivityDefinition
      activityDefinition, int x, int y) {
    ActivityCell view = new ActivityCell( (Activity) activityDefinition,
                                         x, y);
    return view;
  }

  protected ConstraintDefinitionEdge createConstraintCell(ConstraintDefinition
      constraint, ActivityDefinition branch, Parameter formal) {
    return new ConstraintEdge( (Constraint) constraint, branch,formal);
  }

  /**
   *
   * @param listener IAssignmentViewListener
   */
  public void setListener(IAssignmentExecutionListener listener) {
    this.listener = listener;
  }

  /**
   *
   * @return Assignment
   */
  private Assignment getAssignment() {
    return this.state.getAssignment();
  }

  /**
   *
   * @return Color
   */
  public Color getColor() {
    return getColor(this.state);
  }

  public static Color getColor(AssignmentState assignmentState) {
    State state = assignmentState.getState();
    Color color = DGraphConstants.falsePermanentColor();
    if (state != null) {
      switch (state) {
        case SATISFIED:
          color = DGraphConstants.trueTemporaryColor();
          break;
        case VIOLATED_TEMPORARY:
          color = DGraphConstants.falseTemporaryColor();
          break;
      }
    }
    return color;
  }

  /**
   *
   */
  private void displayConstraintStates() {
    if (this.state != null) {
      Assignment assignment = state.getAssignment();
      if (assignment != null) {
        for (int i = 0; i < assignment.constraintDefinitionsCount(); i++) {
          Constraint constraint = assignment.constraintAt(i);
          this.constraintState(constraint);
        }
      }
    }
  }

  /**
   *
   * @param constraint Constraint
   */
  private void constraintState(Constraint constraint) {
    if (constraint != null) {
      Iterator<DefaultGraphCell>
          iterator = this.getCells(constraint).iterator(); // get all cells that are assigned to teh constrint
      // this is important when constraint is branched, because
      // there are three edsed and a vertex for that constraint.
      while (iterator.hasNext()) { // for all cells set the new color
        DefaultGraphCell cell = iterator.next();
        if (cell instanceof DEdge) {
          ( (DEdge) cell).refresh();
        }
      }
    }
  }

  /**
   *
   * @return AssignmentState
   */

  public AssignmentState getState() {
    return this.state;
  }

  /**
   *
   */
  private void displayEnabledActivities() {
    if (state != null) {
      for (int i = 0; i < state.eventCount(); i++) {
        AssignmentState.EventState eventState = state.eventAt(i);
        nl.tue.declare.domain.instance.Event event = eventState.event();
        if (event.getType().equals(nl.tue.declare.domain.instance.Event.Type.STARTED)) {
          Activity activity = state.getAssignment().getActivityForDefinition(
              event.getActivity());
          this.enableActivity(activity);
        }
      }
    }
  }

  /**
   *
   * @param activity Activity
   */
  private void enableActivity(Activity activity) {
    if (activity != null) {
      Iterator<DefaultGraphCell>
          iterator = this.getCells(activity).iterator();
      while (iterator.hasNext()) {
        DefaultGraphCell cell = iterator.next();
        if (cell instanceof ActivityCell) {
          ( (ActivityCell) cell).setEnabled(true);
        }
      }
    }
  }

  /**
   *
   * @param activity Activity
   */
  private void disableActivity(Activity activity) {
    if (activity != null) {
      Iterator<DefaultGraphCell>
          iterator = this.getCells(activity).iterator();
      while (iterator.hasNext()) {
        DefaultGraphCell cell = iterator.next();
        if (cell instanceof ActivityCell) {
          ( (ActivityCell) cell).setEnabled(false);
        }
      }
    }
  }

  public void disableAll() {
    for (int i = 0; i < state.getAssignment().activitiesCount(); i++) {
      this.disableActivity(state.getAssignment().activityAt(i));
    }
  }

  /**
   *
   */
  public void updateStateView() {
    this.displayConstraintStates();
    this.disableAll();
    this.displayEnabledActivities();
    this.displayStateActivities();
    this.updateUI();
  }

  public void cellDoubleClicked(DefaultGraphCell cell) {
    if (activityDefinitionViewClass(cell)) {
      ActivityDefinition activityDefiniton = this.getActivityDefiniton(cell);
      Activity activity = this.state.getAssignment().getActivityForDefinition(
          activityDefiniton);
      if (activity != null && listener != null) {
        listener.startActivity(activity);
      }
    }
  }

  /**
   *
   */
  private void displayStateActivities() {
    Assignment assignment = this.getAssignment();
    if (assignment != null) {
      for (int i = 0; i < assignment.activitiesCount(); i++) {
        Activity activity = assignment.activityAt(i);
        if (state != null) {
          boolean start = state.possible(Event.Type.STARTED, activity);
          boolean complete = state.possible(Event.Type.COMPLETED, activity);
          this.stateActivity(activity, start, complete);
        }
      }
    }
  }

  /**
   *
   * @param activity Activity
   */
  private void stateActivity(Activity activity, boolean start, boolean complete) {
    if (activity != null) {
      Iterator<DefaultGraphCell>
          iterator = this.getCells(activity).iterator();
      while (iterator.hasNext()) {
        DefaultGraphCell cell = iterator.next();
        if (cell instanceof ActivityCell) {
          ( (ActivityCell) cell).setState(start, complete);
        }
      }
    }
  }
}
