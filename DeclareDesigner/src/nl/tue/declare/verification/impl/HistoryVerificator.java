package nl.tue.declare.verification.impl;

import java.util.ArrayList;

import nl.tue.declare.domain.instance.Assignment;
import nl.tue.declare.domain.instance.Event;
import nl.tue.declare.execution.automaton.ExecutableAutomaton;
import nl.tue.declare.verification.VerificationError;

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
public class HistoryVerificator
    extends AbstractVerificator {


  public HistoryVerificator(Assignment assignment) {
    super(assignment);
  }
  
  private Assignment getAssignment(){
	  return (Assignment) model;
  }


  public ArrayList<VerificationError> errors(ExecutableAutomaton automaton) {
	ArrayList<VerificationError> errors = new ArrayList<VerificationError>();
      automaton.ini();	
      for (Event event: getAssignment().getEventLog()){
    	  automaton.next(event.getProposition());
      }
      if ( automaton.currentState() == null) { // trace cannot be replayed on the automaton
          errors.add(new HistoryError());
      }
    return errors;
  }
}
