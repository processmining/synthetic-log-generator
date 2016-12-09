package nl.tue.declare.verification.impl;

import java.util.*;

import nl.tue.declare.domain.model.*;
import nl.tue.declare.execution.automaton.*;
import nl.tue.declare.verification.*;

public class ConflictVerificator
    extends AbstractVerificator {

  public ConflictVerificator(AssignmentModel model) {
    super(model);
  }

  public ArrayList<VerificationError> errors(ExecutableAutomaton automaton) {
	ArrayList<VerificationError> errors = new ArrayList<VerificationError>();
    if (automaton.isEmpty()) {
      errors.add(new ConflictError());
    }
    return errors;
  }
}
