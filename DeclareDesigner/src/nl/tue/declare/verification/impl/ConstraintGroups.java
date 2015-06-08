package nl.tue.declare.verification.impl;

import java.util.*;

import nl.tue.declare.domain.model.*;
import nl.tue.declare.execution.automaton.ExecutableAutomaton;
import nl.tue.declare.execution.ltl.ConstraintParser;
import nl.tue.declare.execution.ltl.LTL2BuchiFinite;

public class ConstraintGroups {
	
	private Collection<Constraints> groups;
	private LTL2BuchiFinite ltl2Buchi;
	
	public ConstraintGroups(){
		super();
		ltl2Buchi =  new LTL2BuchiFinite();
		groups = new ArrayList<Constraints>();
	}
	
	public Constraints add(Collection<ConstraintDefinition> elements) throws Throwable{
		ExecutableAutomaton automaton = this.createAutomaton(elements);
		Constraints constraints = new Constraints(elements,automaton); 
		groups.add(constraints);
		return constraints;
	}
	
	public ExecutableAutomaton get(Collection<ConstraintDefinition> elements) throws Throwable{
		Constraints constraints = null; 
		Iterator<Constraints> iter = groups.iterator();
		boolean found = false;
		while (iter.hasNext() && (!found)){
			constraints = iter.next();
			found = constraints.equalTo(elements);
		}
		if (!found) {
			constraints = this.add(elements);
		}
		return (constraints!=null)?constraints.automaton:null;
	}	
	
	private ExecutableAutomaton createAutomaton(Iterable<ConstraintDefinition> elements) throws Throwable{
	    ConstraintParser parser = new ConstraintParser();
	    //IFormula lifeCycle = this.getlifeCycle();
	    String constraints =  parser.createFormula(elements);
	    //IFormula result = new And(constraints,lifeCycle);
	    
	    
	    //PrettyTime time = new PrettyTime();
	    //time.printCurrentDateTime24();    
	    ExecutableAutomaton  automaton = ltl2Buchi.translate(constraints);		    
	    //time.currentTime24();
	    //time.printCurrentDateTime24(); 
	    return automaton;
	}
	
	 /* private IFormula getlifeCycle(){
    IFormula formula = null;
    for (int i = 0; i < model.activityDefinitionsCount(); i++){
      ActivityDefinition activity = model.activityDefinitionAt(i);

      IProposition started = new Proposition(new StringLiteral((new StartedEvent(new User(0), activity)).getProposition()));
      IProposition completed = new Proposition(new StringLiteral((new CompletedEvent(new User(0), activity)).getProposition()));
      IProposition cancelled = new Proposition(new StringLiteral((new CanceledEvent(new User(0), activity)).getProposition()));
      IFormula temp = new WeakUntil(new Not(new Or(completed,cancelled)),started);

      if (formula==null){
        formula = temp;
      } else {
        formula = new And(formula,temp);
      }
    }
    return formula;
  }*/

	private class Constraints {		
		
		private Collection<ConstraintDefinition> elements;
		private ExecutableAutomaton automaton;		
		
		Constraints(Collection<ConstraintDefinition> constraints,ExecutableAutomaton automaton){
			super();
			elements = constraints;
			this.automaton = automaton;
		}
		
		public boolean equalTo(Collection<ConstraintDefinition> constraints){
			return elements.equals(constraints);
		}
		

	}

}
