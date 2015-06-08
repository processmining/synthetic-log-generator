package nl.tue.declare.verification;

import java.util.*;

import nl.tue.declare.execution.automaton.*;

public interface IVerificator {

  public ArrayList<VerificationError> errors(ExecutableAutomaton automaton);

}
