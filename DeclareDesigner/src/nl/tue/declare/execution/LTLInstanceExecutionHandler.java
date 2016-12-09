package nl.tue.declare.execution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import nl.tue.declare.domain.instance.Assignment;
import nl.tue.declare.domain.instance.Constraint;
import nl.tue.declare.domain.instance.Event;
import nl.tue.declare.domain.instance.IConstraintListener;
import nl.tue.declare.domain.instance.State;
import nl.tue.declare.domain.model.ConstraintDefinition;
import nl.tue.declare.execution.automaton.ExecutableAutomaton;
import nl.tue.declare.execution.ltl.ConstraintParser;
import nl.tue.declare.execution.ltl.LTL2BuchiFinite;
import nl.tue.declare.logging.ProcessLogWriter;

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
public class LTLInstanceExecutionHandler implements IConstraintListener {
	
	private LTL2BuchiFinite ltl2Buchi;
	protected Assignment assignment;
	private ConstraintParser parser;
	private HashMap<Constraint, ExecutableAutomaton> constraints;

	private ExecutableAutomaton mandatory = null;

	/**
	 * AbstractExecutionStrategy
	 * 
	 * @param ltl2Buchi
	 *            ILTL2Buchi
	 * @param assignment
	 *            Assignment
	 */
	public LTLInstanceExecutionHandler(Assignment assignment) throws Throwable {
		this.ltl2Buchi = new LTL2BuchiFinite();
		this.assignment = assignment;
		parser = new ConstraintParser();
		constraints = new HashMap<Constraint, ExecutableAutomaton>();
		this.createMandatory();
		this.setConstraintListener();
	}

	private void createMandatory() throws Throwable {
		mandatory = generateAutomaton(createMandatoryFormula()); 														
	}

	public synchronized void reset(Assignment assignment) throws Throwable {
		this.assignment = assignment;
		this.constraints.clear();
		this.changed();
		this.setConstraintListener();
	}

	private void setConstraintListener() {
		for (int i = 0; i < assignment.constraintDefinitionsCount(); i++) {
			assignment.constraintAt(i).addListener(this);
		}
	}

	/**
	 * 
	 * @return IFormula
	 */
	private String createMandatoryFormula() {
		String result = null;
		Collection<ConstraintDefinition> constraints = new ArrayList<ConstraintDefinition>();
		for (int i = 0; i < assignment.constraintDefinitionsCount(); i++) {
			Constraint constraint = assignment.constraintAt(i);
			// only active
			if (constraint.isActive()) {
				constraints.add(constraint);
			}
		}
		result = createFormula(constraints);
		return result;
	}

	/**
	 * 
	 * @param event
	 *            Event
	 * @param automaton
	 *            Automaton
	 * @return boolean
	 */
	protected boolean executeAutomaton(Event event,
			ExecutableAutomaton automaton) {
		boolean reads = true;
		if (event != null) {
			reads = (automaton.next(this.eventProposition(event)) != null);
		}
		return reads;
	}

	/**
	 * 
	 * @param formula
	 *            IFormula
	 * @return Automaton
	 */
	protected synchronized ExecutableAutomaton generateAutomaton(
			String formula) throws Throwable {
		ExecutableAutomaton automaton = null;
		if (formula == null) {
			automaton = ltl2Buchi.translate("true");
		}
		automaton = ltl2Buchi.translate(formula);
		return automaton;
	}

	/**
	 * 
	 * @param constraint
	 *            IFormula
	 * @return Automaton
	 */
	protected synchronized ExecutableAutomaton getAutomaton(
			Constraint constraint) throws Throwable {
		ExecutableAutomaton automaton = constraints.get(constraint);
		if (automaton == null) {
			List<ConstraintDefinition> list = new ArrayList<ConstraintDefinition>();
			list.add(constraint);
			String formula = this.createFormula(list);
			automaton = generateAutomaton(formula);
			constraints.put(constraint, automaton);
		}
		return automaton;
	}

	public synchronized String createFormula(
			Iterable<ConstraintDefinition> constraints) {
		return parser.createFormula(constraints);

	}

	/**
	 * 
	 * @param event
	 *            Event
	 * @return boolean
	 */
	public synchronized boolean allowedEvent(Event event) {
		boolean possible = this.possible(event);
		 // log the scheduling event
		if (possible && event.getType().equals(Event.Type.STARTED)) { 
			ProcessLogWriter.singleton().schedule(assignment,
					event.getActivity());
		}
		return possible;
	}

	/**
	 * 
	 * @param event
	 *            Event
	 * @return Collection
	 */
	public Collection<Constraint> violatesConstraints(Event event)
			throws Throwable {
		Collection<Constraint> violate = new ArrayList<Constraint>();
		for (int i = 0; i < assignment.constraintDefinitionsCount(); i++) {
			Constraint constraint = assignment.constraintAt(i);
			boolean violates = this.red(event, constraint);
			if (violates) {
				violate.add(constraint);
			}
		}
		return violate;
	}

	/**
	 * 
	 * @param event
	 *            Event
	 * @return boolean
	 */
	protected String eventProposition(Event event) {
		return event.getProposition();
	}

	/**
	 * 
	 * @param events
	 *            EventLog
	 * @param automaton
	 *            Automaton
	 * @return boolean
	 */
	private boolean acceptsTrace(Iterable<Event> events,
			ExecutableAutomaton automaton) {
		boolean ok = true;
		if (events != null && automaton != null) {
			Iterator<Event> iterator = events.iterator();
			while (iterator.hasNext() && ok) {
				Event event = iterator.next();
				ok = this.executeAutomaton(event, automaton);
			}
		}
		if (ok) {
			ok = automaton.currentState().isAccepting();
		}
		return ok;
	}

	/**
	 * 
	 * @param events
	 *            EventLog
	 * @param automaton
	 *            Automaton
	 * @return boolean
	 */
	private boolean redTrace(Iterable<Event> events,
			ExecutableAutomaton automaton) {
		return !trace(events, automaton);
	}

	/**
	 * 
	 * @param events
	 *            EventLog
	 * @param automaton
	 *            Automaton
	 * @return boolean
	 */
	private boolean trace(Iterable<Event> events, ExecutableAutomaton automaton) {
		boolean ok = true;
		if (events != null && automaton != null) {
			automaton.ini(); // added now for TEST
			Iterator<Event> iterator = events.iterator();
			while (iterator.hasNext() && ok) {
				Event event = iterator.next();
				ok = this.executeAutomaton(event, automaton);
			}
		}
		return ok;
	}

	/**
	 * 
	 * @param event
	 *            Event
	 * @param constraint
	 *            Constraint
	 * @return boolean
	 */
	private boolean red(Event event, Constraint constraint) throws Throwable {
		boolean after = false;
		boolean before = true;
		try {
			// crate a fake log with the new event at the end
			// EventLog extendedLog = (EventLog) this.log.clone();
			ArrayList<Event> extendedLog = new ArrayList<Event>();
			for (Event e : assignment.getEventLog()) {
				extendedLog.add(e);
			}

			// check if the constraint is red now
			ExecutableAutomaton automaton = this.getAutomaton(constraint);
			before = this.redTrace(assignment.getEventLog(), automaton);
			if (!before) { // if the constraint is not red now check if the
							// event will violate it
				// add the event to the fake log

				extendedLog.add(event);
				// first check the log
				after = this.redTrace(extendedLog, automaton);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return after;
	}

	/**
	 * 
	 * @param workItemEvent
	 *            Event
	 * @return boolean
	 */
	public synchronized boolean next(Event event) {
		boolean ok = true;
		ok = this.allowedEvent(event);
		if (ok) {
			assignment.addEvent(event); // add to log first because
			this.executeEvent(event); // here it may be necessary to change some
										// things and then replay the log
		}
		if (ok) { // write the log in the file
			ProcessLogWriter.singleton().add(this.assignment, event);
		}
		return ok;
	}

	/**
	 * 
	 * @param constraint
	 *            Constraint
	 */
	public State constraintState(Constraint constraint) throws Throwable {
		State state = State.getDefault();
		try {
			ExecutableAutomaton automaton = this.getAutomaton(constraint);
			automaton.ini();
			this.acceptsTrace(assignment.getEventLog(), automaton);
			state = getState(automaton);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return state;
	}

	/**
	 * 
	 * @param event
	 *            Event
	 * @return boolean
	 */
	public boolean possible(Event event) {
		boolean result = true;
		if (event != null) {
			if (event.getType().equals(Event.Type.STARTED)) {
				Event completed = new Event(event.getUser(), event
						.getActivity(), Event.Type.COMPLETED);
				result = this.reachable(completed);
			}
		}
		if (result && (event != null)) {
			if (this.mandatory != null) {
				result = mandatory.parses(event.getProposition());
			}
		}
		return result;
	}

	/**
	 * 
	 * @param automaton
	 *            Automaton
	 */
	protected void replayLog(ExecutableAutomaton automaton) {
		if (automaton != null) {
			automaton.ini();
			for (Event event : assignment.getEventLog()) {
				executeAutomaton(event, automaton);
			}
		}
	}


	/**
	 * 
	 * @param event
	 *            AbstractEvent
	 */
	protected void executeEvent(Event event) {
		if (event != null) {
			this.executeAutomaton(event, this.mandatory);
			this.assignment.event();
		}
	}

	/**
	 * 
	 * @param event
	 *            AbstractEvent
	 * @return boolean
	 */
	public boolean reachable(Event event) {
		boolean result = false;
		if (event != null) {
			if (this.mandatory != null) {
				result = mandatory.reachableTransition(event.getProposition());
			}
		}
		return result;
	}

	/**
	 * TO DO: this is not very effitient. Automaton is cretaed whenever a
	 * constraint status changes However, it might happen that several
	 * constraints chenge status. It would be better to first check if any
	 * constraint changed status and then create mandatory
	 * 
	 * @param constraint
	 *            Constraint
	 */
	public void changed() throws Throwable {
		this.createMandatory();
		this.replayLog(this.mandatory);
	}

	public State assignmentState() {
		return getState(mandatory);
	}

	public State getState(ExecutableAutomaton automaton) {
		State st = State.getDefault();
		if (automaton.currentState() == null) {
			st = State.VIOLATED;
		} else if (automaton.currentState().isAccepting()) {
			st = State.SATISFIED;
		} else {
			if (automaton.currentState().acceptingReachable()) {
				st = State.VIOLATED_TEMPORARY;
			}
		}
		return st;
	}
}
