package nl.tue.declare.verification;

import java.util.*;

import nl.tue.declare.domain.model.*;
import nl.tue.declare.execution.automaton.*;
import nl.tue.declare.verification.impl.ConstraintGroups;
import nl.tue.declare.verification.impl.IPowerSetListener;
import nl.tue.declare.verification.impl.PowerSet;
//import nl.tue.declare.verification.impl.utils.*;

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
public abstract class AbstractVerification
    implements IVerification, IPowerSetListener {

  protected AssignmentModel model;
  protected ConstraintGroups constraintGroups;	
  protected VerificationResult result;

  public AbstractVerification(AssignmentModel model) {
    super();
    this.model = model;    
    constraintGroups = new ConstraintGroups();
    result = new VerificationResult(model);
  }
  
  private boolean active(ConstraintDefinition constraint) {
    return constraint.getMandatory();
  }

  private VerificationResult result() {
    return result;
  }

  protected ExecutableAutomaton automaton(ViolationGroup group) throws Throwable {
	  return constraintGroups.get(group);
  }

  /**
   * verify
   *
   * @param model AssignmentModel
   * @throws Exception
   * @todo Implement this nl.tue.declare.execution.verification.IVerificator
   *   method
   */

  public synchronized VerificationResult verify() throws
      Throwable {
    if (this.errorExists(model)) {
      // create root for powerset
      PowerSet root = new PowerSet();
      // first add all constraints to powerset
      for (int i = 0; i < model.constraintDefinitionsCount(); i++) {
        ConstraintDefinition constraint = model.constraintDefinitionAt(i);
        if (active(constraint)) {
          root.add(constraint);
        }
      }
      root.addListener(this); // add self as listener
      root.expand(); // expand power set
    }
    return this.result();
  }

  /**
   * Checks if error exists for the whole assignment at all.
   * @param model AssignmentModel
   * @return boolean
   */
  private boolean errorExists(AssignmentModel model) throws Throwable { 
    Collection<ConstraintDefinition> elements = new ArrayList<ConstraintDefinition>();
    for (int i = 0; i < model.constraintDefinitionsCount(); i++) {
      ConstraintDefinition constraint = model.constraintDefinitionAt(i);
      if (constraint.getMandatory()) {
        elements.add(constraint);
      }
    }
    ExecutableAutomaton automaton = automaton(group(elements));
    ArrayList<VerificationError> errors = errors(automaton);
    return hasErrors(errors);
  }



  public boolean expand(PowerSet set) throws Throwable {
    boolean valid = true;
    Iterator<Object> data = set.getData();
    if (data.hasNext()) {
      Collection<ConstraintDefinition> elements = new ArrayList<ConstraintDefinition>();
      while (data.hasNext()) {
        Object element = data.next();
        if (element instanceof ConstraintDefinition) {
          elements.add( (ConstraintDefinition) element);
        }
      }
      ViolationGroup group = group(elements);
      ExecutableAutomaton automaton = automaton(group);
      ArrayList<VerificationError> errors = errors(automaton);
      valid = !handleErrors(errors, group);
    }
    return valid;
  }

  private boolean hasErrors(ArrayList<VerificationError> errors) {
    boolean hasErrors = false;
    if (errors != null) {
      hasErrors = (errors.size() > 0);
    }
    return hasErrors;
  }

  private boolean handleErrors(ArrayList<VerificationError> errors,
      ViolationGroup group) {
    boolean hasErrors = hasErrors(errors);
    if (hasErrors) {
      for (int i = 0; i < errors.size(); i++) {
        VerificationError error = errors.get(i);
        error.setGroup(group);
        result.add(error);
      }
    }
    return hasErrors;
  }

  protected abstract ArrayList<VerificationError> errors(ExecutableAutomaton automaton);

  private ViolationGroup group(Collection<ConstraintDefinition> constraints) {
    ViolationGroup group = new ViolationGroup();
    Iterator<ConstraintDefinition> iterator = constraints.iterator();
    while (iterator.hasNext()) {
      ConstraintDefinition constraint = iterator.next();
      group.add(constraint);
    }
    return group;
  }
}
