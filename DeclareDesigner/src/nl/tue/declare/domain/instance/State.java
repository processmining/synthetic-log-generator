package nl.tue.declare.domain.instance;
/**
 * This class represents possible states of constraints 
 * during execution of instances of constraint models.
 * 
 * Constraint state is SATISFIED if the constraint is fulfilled (true).
 * 
 * Constraint state is VIOLATED_TEMPORARY if the constraint is currently not fulfilled (false),
 * but it is possible to bring the constraint to state SATISFIED in the future.
 * 
 * Constraint state is VIOLATED if the constraint is currently not fulfilled (false)
 * and it is not possible to bring the constraint to state SATISFIED in the future.
 *  
 * @author mpesic
 * 
 */
public enum State {
	
	SATISFIED("The constraint is currently satisfied."),  
	VIOLATED_TEMPORARY("The constraint is currently not satisfied, but it can be satisfied in the future."),
	VIOLATED("The constraint is currently not satisfied, nor it can be satisfied in the future.");
    
	/**
	 * Textual description of the state.
	 */
	private String description;
	
	/**
	 * The only constructor which assigns the textual description of the state.
	 * @param des is the textual description of the state.
	 */
	State(String des) {
		description = des;
	}	
	
	/**
	 * Returns the default state, i.e., the state which should be assigned to constraints by default. 
	 * This method is used when the state cannot be determined via available techniques.
	 * 
	 * @return VIOLATED  
	 */
	public static State getDefault(){
		return VIOLATED;
	}
	
	/**
	 * Provides the textual description of the state. 
	 * @return description
	 */
	public String getDescription(){
		return description;
	}	
}
