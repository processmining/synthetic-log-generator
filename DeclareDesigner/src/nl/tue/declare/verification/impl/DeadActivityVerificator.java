package nl.tue.declare.verification.impl;

import java.util.*;

import nl.tue.declare.domain.instance.*;
import nl.tue.declare.domain.model.*;
import nl.tue.declare.domain.organization.*;
import nl.tue.declare.execution.automaton.*;
import nl.tue.declare.verification.*;

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
public class DeadActivityVerificator
    extends AbstractVerificator {

  public DeadActivityVerificator(AssignmentModel model) {
    super(model);
  }

  public ArrayList<VerificationError> errors(ExecutableAutomaton automaton) {
	ArrayList<VerificationError> errors = new ArrayList<VerificationError>();
    if (!automaton.isEmpty()) {
      for (int i = 0; i < model.activityDefinitionsCount(); i++) {
        ActivityDefinition activity = model.activityDefinitionAt(i);
        if (dead(automaton, activity)) {        
          DeadActivityError error = new DeadActivityError(activity);
          errors.add(error);
        }
      }
    }
    return errors;
  }

  private boolean dead(ExecutableAutomaton automaton, ActivityDefinition activity) {
    boolean dead = false;
    Event completed = new Event(new User(0), activity, Event.Type.COMPLETED);
    Event started = new Event(new User(0), activity, Event.Type.STARTED);
    dead = !automaton.order(started.getProposition(),completed.getProposition());
    return dead;
  }
}
