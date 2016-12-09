package nl.tue.declare.verification;

import java.util.*;

import nl.tue.declare.domain.instance.Assignment;
import nl.tue.declare.domain.model.AssignmentModel;
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
public class HistoryVerification
    extends AbstractVerification {

  protected IVerificator history;

  public HistoryVerification(Assignment assignment) {
    super(assignment);
    result = new  HistoryVerificationResult(assignment);
    history = new HistoryVerificator(assignment);    
  }

  protected ArrayList<VerificationError> errors(ExecutableAutomaton automaton) {
	ArrayList<VerificationError> errors = new ArrayList<VerificationError>();
    errors.addAll(history.errors(automaton));
    return errors;
  }
  
  private class HistoryVerificationResult extends VerificationResult{
		
		private static final long serialVersionUID = -563355904753590234L;
		
		public HistoryVerificationResult(AssignmentModel model) {
			super(model);
		}

		public String toString() {		
			String str = super.toString();
			if (isEmpty()) {
				str += " Change is completed sucessfully.";
			} else {
				str += " Change is aborted.";
			}
	     	return str;
		}
	}
}
