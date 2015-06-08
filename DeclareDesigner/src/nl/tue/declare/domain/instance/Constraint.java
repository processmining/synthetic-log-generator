package nl.tue.declare.domain.instance;

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

import nl.tue.declare.domain.model.*;

public class Constraint           
    extends ConstraintDefinition {

  private State state;
  private ConditionEvaluator conditionEvaluator;
  private Collection<IConstraintListener> listeners;
  private boolean status;

  public Constraint(ConstraintDefinition definiton, Assignment assignment) {
    super(definiton);
    this.setAssignmentModel(assignment);
    this.listeners = new ArrayList<IConstraintListener>();
    this.state = State.getDefault();
    this.conditionEvaluator = new ConditionEvaluator(this.getAssignmentModel());
    this.resetStatus();
  }

  public Object clone() {
    Constraint clone = null;
    try {
      clone = new Constraint(this, this.getAssignment());
      clone.listeners.addAll(this.listeners);
      clone.state= this.state;
    }
    catch (Exception ex) {
    }
    return clone;
  }

  public void addListener(IConstraintListener l) {
    this.listeners.add(l);
  }

  private void changed() throws Throwable {
    Iterator<IConstraintListener> iterator = listeners.iterator();
    while (iterator.hasNext()) {
      iterator.next().changed( /*this*/);
    }
  }

  public Assignment getAssignment() {
    return (Assignment) getAssignmentModel();
  }

  public State getState() {
    return this.state;
  }

  public void setState(State state) {
    if (state != null) {
      this.state = state;
    }
  }

  /**
   * checkCondition
   *
   * @return boolean
   */
  private boolean checkCondition() {
    boolean result = true;
    if (!getCondition().getText().equals("")) {
      try {
        result = this.conditionEvaluator.parseCondition(this.getCondition().
            getText());
      }
      catch (Exception ex) {
        result = false;
      }
    }
    return result;
  }

  public boolean getStatus() {
    return this.status;
  }

  public void resetStatus() {
    this.setStatus(checkCondition());
  }

  public boolean isActive() {
    // only mandatory         discard when permanetly false  when condition is true
    // return (getMandatory() && !getState().isFalsePermanet() && getStatus());
    return (getMandatory() && getStatus());
  }

  public void setStatus(boolean status) {
    boolean old = this.status;
    this.status = status;
    if (old != status) {
      try {
        this.changed();
      }
      catch (Throwable ex) {
      }
    }
  }
  
  public String getCurrentStateMessage(){
	  return this.getStateMessage(state);
  }
 
}
