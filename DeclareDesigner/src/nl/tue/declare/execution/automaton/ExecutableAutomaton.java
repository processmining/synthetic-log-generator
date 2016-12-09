package nl.tue.declare.execution.automaton;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
public class ExecutableAutomaton {

	private ltl2aut.automaton.Automaton graph;

	private PossibleNodes current;

	public ExecutableAutomaton(ltl2aut.automaton.Automaton graph) {
		super();
		this.graph = graph;
		ini();
	}


	public void ini() {
		current = new PossibleNodes();
		current.add(graph.getInit());
	}

	public PossibleNodes next(String label) {
		if (current != null) {
			current = current.next(label);
		}
		return current;
	}

	public int stateCount() {
		return this.graph.getStateCount();
	}

	public Iterable<State> states() {
		return graph;
	}

	public PossibleNodes currentState() {
		return this.current;
	}

	public boolean parses(String label) {
		boolean parses = false;
		if (current != null) {
			parses = current.parses(label);
		}
		return parses;
	}



	/**
	 * 
	 * @param label
	 *            String
	 * @return boolean
	 */
	public boolean reachableTransition(String label) {
		boolean reachable = false;
		if (current != null) {
			reachable = this.current.reachableTransition(new ArrayList<State>(),
					label);
		}
		return reachable;
	}

	/**
	 * isEmpty
	 */
	public boolean isEmpty() {
		return graph.getStateCount() == 0;
	}

	/**
	 * Get all states that have an input transition that parses the given label.
	 * 
	 * @param label
	 *            String
	 * @return Iterator
	 */
	private Set<State> input(final String label) {
		final Set<State> result = new HashSet<State>();
		for (Transition t : graph.transitions()) {
			if (AutomatonUtils.edgeParses(t, label))
				result.add(t.getTarget());
		}
		return result;
	}

    /*
     * Checks if the automaton accepts a word word where two literals (first and second) appear in one after another. 
     * The wanted form of the word is *first*second*.
     */
	public boolean order(String first, String second) {
		Iterator<State> firstStatesIterator = this.input(first).iterator();
		Collection<State> secondStates = this.input(second);
		boolean found = false;
		while (firstStatesIterator.hasNext() && !found) {
			State stateFirst = firstStatesIterator.next();
			ArrayList<State> reachable = new ArrayList<State>();
			AutomatonUtils.reachableStates(stateFirst, reachable);
			Iterator<State> secondStatesIterator = secondStates.iterator();
			while (secondStatesIterator.hasNext() && !found) {
				State stateSecond = secondStatesIterator.next();
				found = reachable.contains(stateSecond);
			}
		}
		return found;
	}
}
