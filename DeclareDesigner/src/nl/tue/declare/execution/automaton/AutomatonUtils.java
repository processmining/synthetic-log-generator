package nl.tue.declare.execution.automaton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import ltl2aut.automaton.*;

public class AutomatonUtils {

	static boolean edgeParses(Transition t, String label) {
		return t.parses(label);
	}

	/**
	 * 
	 * @param reachable
	 *            Set
	 */
	static void reachableStates(State n, final ArrayList<State> reachable) {
		if (n != null) {
			if (!reachable.contains(n)) {
				reachable.add(n);
				for (Transition t : n.getOutput()) {
					State target = t.getTarget();
					reachableStates(target, reachable);
				}

			}
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
	@SuppressWarnings("unchecked")
	static boolean reachableTransition(State node,
			final ArrayList<State> reachable, final String label) {
		boolean ok = false;
		if (node != null && !reachable.contains(node)) {
			reachable.add(node);
			Iterator iterator = node.getOutput().iterator();
			while (iterator.hasNext() && !ok) {
				Transition edge = (Transition) iterator.next();
				ok = edgeParses(edge, label);
				if (!ok) {
					State target = edge.getTarget();
					ok = reachableTransition(target, reachable, label);
				}
			}
		}
		return ok;
	}

	static boolean isAccepting(State node) {
		if (node == null) {
			return false;
		} else {
			return node.isAccepting();
		}
	}

	public static ArrayList<String> getPropositions(Transition edge) {
		ArrayList<String> result = new ArrayList<String>();
		StringTokenizer maja = new StringTokenizer(edge.toString(), "&");
		while (maja.hasMoreTokens()) {
			result.add(maja.nextToken());
		}
		return result;
	}
}
