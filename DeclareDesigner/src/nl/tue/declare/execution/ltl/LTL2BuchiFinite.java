package nl.tue.declare.execution.ltl;

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

import ltl2aut.*;
import ltl2aut.automaton.*;
import nl.tue.declare.execution.automaton.ExecutableAutomaton;


public class LTL2BuchiFinite {

  //private static String TRUE ="true";
  
  private static Object synchronizer = new Object();
  
  //private final String PROPOSITION_LABEL_PREFIX = "p";

  //private Map<IProposition, String> propositionToInternalLabel;
  //private Map<String, IProposition> internalLabelToProposition;
 //private int propCount;
  /**
   * LTL2BuchiFinite
   */
  public LTL2BuchiFinite() {
    super();
    //this.propositionToInternalLabel = new HashMap<IProposition, String> ();
    //this.internalLabelToProposition = new HashMap<String, IProposition> ();
    //this.propCount = 0; 
  }
  
  /**
  *
  * @param formula String
  * @return Automaton
  * @todo Implement this nl.tue.declare.execution.translate.ILTL2Buchi method
  */
 public synchronized ExecutableAutomaton translate(String formula) throws Throwable {
   // assign propositions to internal labels (e.g., p0, p1, p2,... ) and
   //this.propositionsToLabels(formula);

   // replace propostions with internal labels (e.g., "receive bill.completed" is replaced
   // with "p4"). this is done for the simplicity
  // ltl = replacePropositions(ltl);

//	 System.out.println(formula); // This is totally kosher...
	 
   // generate the automaton for the simplifeid formula string (with p0, p1, p2,... as propositions )
   ExecutableAutomaton a = this.generate(formula);

   // now map and copy back the right propositions to the simple propostions in the automaton
   // (e.g., "p4" is replaced with "receive bill.completed" )
   //labelsToPropositions(a);
   return a;
 }

  
 /*private void propositionsToLabels(IFormula formula) {
	    // get all propositions fo the formula
	    List<IProposition> propositions = formula.getPropositions();
	    if (propositions != null) {
	      // loop through all propositins from the formula
	      Iterator<IProposition> iterator = propositions.iterator();
	      while (iterator.hasNext()) {
	        IProposition proposition = iterator.next();
	        this.getInternalLabel(proposition);
	      }
	    }
	  }

	  private String getInternalLabel(IProposition proposition) {
	    String internalLabel = null;
	    if (this.propositionToInternalLabel.containsKey(proposition)) {
	      //fetch old label
	      internalLabel = this.propositionToInternalLabel.get(proposition);
	    }
	    else {
	      //generate new label
	      if (proposition instanceof TrueProposition) {
	        internalLabel =  TRUE; // true stays the same
	      }
	      else
	      if (proposition instanceof FalseProposition) {
	        internalLabel = TRUE; // false stays the same
	      }
	      else {
	        internalLabel = PROPOSITION_LABEL_PREFIX + this.propCount;
	        this.propCount++;
	      }
	      this.propositionToInternalLabel.put(proposition, internalLabel);
	      this.internalLabelToProposition.put(internalLabel, proposition);
	    }
	    return internalLabel;
	  }

	  protected IProposition getProposition(String internalLabel) {
	    return this.internalLabelToProposition.get(internalLabel);
	  }

	  private String replacePropositions(String ltl) {
	    String result = new String(ltl);
	    Iterator<IProposition> iterator = this.propositionToInternalLabel.keySet().
	        iterator();
	    while (iterator.hasNext()) {
	      IProposition proposition = iterator.next();
	      String internalLabel = this.propositionToInternalLabel.get(proposition);
	      result = result.replace(proposition.toString(), internalLabel);
	    }
	    return result;
	  }

	  @SuppressWarnings("unchecked")
	private void labelsToPropositions(Automaton a) {
	    if (a != null) {
	      Iterator<Node> states = a.states();
	      while (states.hasNext()) {
	        Node state = states.next();

	        Iterator<Edge> transitions = state.getOutgoingEdges().iterator();
	        while (transitions.hasNext()) {
	          Edge edge = transitions.next();
	          Iterator<String> prop = AutomatonUtils.getPropositions(edge).iterator(); // these are now p0, p1, p2,...
	          ArrayList<String> realPopositions = new ArrayList<String>();
	          while (prop.hasNext()){
	        	  String p = prop.next();
	        	  if (!p.equals("-")){ // do not replace a SIGMA (true) proposition
	        	  boolean n = p.contains("!");
	        	  if (n){ 
	        		  p = p.replace("!", ""); // remove the negation sign
	        	  }    
	              String newProposition = this.internalLabelToProposition.get(p).getLabel(); // this is now, e.g., activity 1.COMPLETED
	              if (n){ 
	            	  newProposition = "!"  + newProposition; // if negation, put the negation sign
	              }
	              realPopositions.add(newProposition);
	        	  } else realPopositions.add(p);
	          }
	          
	          AutomatonUtils.setGuard(realPopositions.iterator(), edge);
	        }
	      }
	    }
	  }*/
   

 /**
   *
   * @param ltl String
   * @return Automaton
   */
  protected synchronized ExecutableAutomaton generate(String ltl) throws Throwable {
    try {
      Automaton automaton = null;
      synchronized (synchronizer) {
    	  // System.out.println(ltl);
    	  automaton = LTL2Automaton.getInstance().translate(ltl).op.reduce();
      }
      return new ExecutableAutomaton(automaton);
    }
    catch (Exception ex) {
      Exception e = new Exception("Could not parse formula " + ltl, ex);
      throw e;
    }
    catch (Error ex) {
      Error e = new Error("Could not generate automaton for formula " + ltl, ex);
      throw e;
    }
  }
}
