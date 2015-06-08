package nl.tue.declare.execution.automaton;

import java.util.*;

import ltl2aut.automaton.*;

/**
 * <p>
 * Title: DECLARE
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: TU/e
 * </p>
 * 
 * @author Maja Pesic
 * @version 1.0
 */
public class PossibleNodes extends ArrayList<State> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9214472132268674896L;

	public PossibleNodes() {
		super();
	}

	public boolean isAccepting() {
		for (State s : this) {
			if (s.isAccepting()) {
				return true;
			}
		}
		return false;
	}

	public Collection<Transition> output() {
		Collection<Transition> out = new ArrayList<Transition>();
		for (State s : this) {
			for (Transition o : s.getOutput()) {
				out.add(o);
			}
		}
		return out;
	}

	/**
	 * Here we get the next set of possible states for one execution of the
	 * current set of possible states.
	 * 
	 * @param label
	 *            String
	 * @return PossibleStates
	 */
	PossibleNodes next(String label) {
		// create a set of next possible states
		PossibleNodes next = new PossibleNodes();

		// loop through all output transitions
		for (Transition t : output()) {

			if (AutomatonUtils.edgeParses(t, label)) { // transition parses the
														// label
				next.add(t.getTarget()); // add the target state 
			}
			// set of next possible states
		}
		if (next.size() == 0) { // if no transition has been found that would
			// parse the label
			return null; // leave the execution
		}
		return next;
	}

	public boolean parses(String label) {
		// create a temp variable-true if a transition that parses the label is
		// found
		boolean found = false;

		// get all output transitions
		Iterator<Transition> output = this.output().iterator();

		// loop through all output transitions untill you find one transition
		// that parses the label
		while (output.hasNext() && !found) {
			// get the next output transition
			Transition edge = output.next();

			found = AutomatonUtils.edgeParses(edge, label); // if transition
			// parses the label
		}
		return found;
	}

	/**
	 * Checks if from at least one of the possible states an acceptig state is
	 * reachable. If from at least one possible state an accepting state is
	 * reachable returns TRUE. If from none of the possible states an accepting
	 * state is reachable returns FALSE;
	 * 
	 * @return boolean
	 */
	public boolean acceptingReachable() {
		boolean accepting = false;
		Iterator<State> iterator = this.iterator();
		while (iterator.hasNext() && !accepting) {
			State state = iterator.next();
			accepting = acceptingReachable(state);
		}
		return accepting;
	}

	public boolean acceptingReachable(State node) {
		if (this.isAccepting()) {
			return true;
		}
		ArrayList<State> reachableStates = new ArrayList<State>();
		AutomatonUtils.reachableStates(node, reachableStates);
		boolean reachable = false;
		Iterator<State> iterator = reachableStates.iterator();
		while (!reachable && iterator.hasNext()) {
			State state = iterator.next();
			reachable = AutomatonUtils.isAccepting(state);
		}
		return reachable;
	}

	/**
	 * 
	 * @param reachable
	 *            Set
	 */
	public void reachableStates(ArrayList<State> reachable) {
		Iterator<State> iterator = this.iterator();
		while (iterator.hasNext()) {
			State state = iterator.next();
			AutomatonUtils.reachableStates(state, reachable);
		}
	}

	/**
	 * 
	 * @param reachable
	 *            Set
	 * @param label
	 *            String
	 * @return boolean
	 */
	public boolean reachableTransition(ArrayList<State> reachable, String label) {
		boolean ok = false;
		Iterator<State> iterator = this.iterator();
		while (iterator.hasNext() && !ok) {
			State state = iterator.next();
			ok = AutomatonUtils.reachableTransition(state, reachable, label);
		}
		return ok;
	}
}
