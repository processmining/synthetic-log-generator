package nl.tue.declare.verification;

import java.util.*;

import nl.tue.declare.domain.model.*;
import nl.tue.declare.execution.automaton.*;
import nl.tue.declare.verification.impl.*;

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
public class ModelVerification
    extends AbstractVerification {
	
  private IVerificator conflict;
  private IVerificator dead;

  public ModelVerification(AssignmentModel model) {
    super(model);
    conflict = new ConflictVerificator(model);
    dead = new DeadActivityVerificator(model);
  }


  protected ArrayList<VerificationError> errors(ExecutableAutomaton automaton) {
	ArrayList<VerificationError> errors = new ArrayList<VerificationError>();
    errors.addAll(conflict.errors(automaton));
    errors.addAll(dead.errors(automaton));
    return errors;
  }
  
 }
