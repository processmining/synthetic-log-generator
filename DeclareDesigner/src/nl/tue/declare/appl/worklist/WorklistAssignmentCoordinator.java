package nl.tue.declare.appl.worklist;

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
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import nl.tue.declare.appl.util.swing.MessagePane;
import nl.tue.declare.appl.util.swing.TPanel;
import nl.tue.declare.appl.util.swing.TSplitPane;
import nl.tue.declare.appl.worklist.gui.ActivitiesPane;
import nl.tue.declare.appl.worklist.gui.FrmWarning;
import nl.tue.declare.appl.worklist.gui.ResponsePanel;
import nl.tue.declare.domain.instance.Activity;
import nl.tue.declare.domain.instance.Assignment;
import nl.tue.declare.domain.instance.Constraint;
import nl.tue.declare.domain.instance.Event;
import nl.tue.declare.domain.instance.State;
import nl.tue.declare.domain.organization.User;
import nl.tue.declare.execution.AssignmentState;
import nl.tue.declare.execution.WorkItem;
import nl.tue.declare.execution.msg.IMessage;
import nl.tue.declare.execution.msg.MessageFactory;
import nl.tue.declare.execution.msg.engine.response.EngineEventAccept;
import nl.tue.declare.execution.msg.engine.response.EngineEventReject;
import nl.tue.declare.graph.assignment.AssignmentView;

import org.processmining.operationalsupport.messages.reply.ResponseSet;

public class WorklistAssignmentCoordinator implements IAssignmentExecutionListener {
	public interface IListener {
		void closeAssignment(WorklistAssignmentCoordinator coordinator);

		void compare(final Assignment assignment, final String query);

		IMessage executeRequest(IMessage request);

		void predict(final Assignment assignment, final String query);

		void recommend(final Assignment assignment, final String query);

		void simple(final Assignment assignment, final String query);

		void simpleMonitor(Assignment assignment, String text);

		void recommendMonitor(Assignment assignment, String text);

		void predictMonitor(Assignment assignment, String text);

		void compareMonitor(Assignment assignment, String text);

		void removeMonitor(Assignment assignment, int id);
	}

	// extends AbstractAssignmentCoordinator {
	protected Assignment assignment;
	protected AssignmentView view = null;
	protected Collection<WorkItem> items;
	protected HistoryCoordinator history;
	protected IListener listener = null;

	private User user = null;
	private JFrame frame = null;
	private JPanel panel = null;
	private final ResponsePanel recommendation;
	private ActivitiesPane activitiesPane = null;

	private JSplitPane split;

	private JPanel graphPanel = null;

	FrmWarning frmWarning;

	public WorklistAssignmentCoordinator(final Assignment assignment, final JFrame frame,
			final User user) {
		super();
		this.assignment = assignment;
		this.user = user;
		items = new ArrayList<WorkItem>();
		history = new HistoryCoordinator(getAssignment());
		final Thread t = new Thread(history);
		t.start();

		this.frame = frame;
		panel = new JPanel(new BorderLayout());
		activitiesPane = new ActivitiesPane();
		recommendation = new ResponsePanel(this);
	
		frmWarning = new FrmWarning(getMainFrame(), panel);
		prepareUI();
	}

	/**
	 * 
	 * @param workItem
	 *            WorkItem
	 */
	public void cancelActivity(final WorkItem workItem) {
		if (workItem != null) {
			final Event event = new Event(getUser(), workItem.getActivity(), Event.Type.CANCELLED);
			if (warn(event)) {
				if (executeWorkItem(workItem, event) != null) {
					removeWorkItem(workItem);
				} else {
					MessagePane.inform(panel,
							"At the moment it is not possible to cancel activity "
									+ workItem.getActivity());
				}
			}
		}
	}

	public boolean canStop() {
		return getAssignmentState().getState().equals(State.SATISFIED);
	}

	public void change(final Assignment assignment, final AssignmentView view) {
		setAssignment(assignment);
		setView(view);
	}

	public void compare(final String query) {
		if (listener != null) {
			listener.compare(assignment, query);
		}
	}

	public void compareMonitor(final String text) {
		if (listener != null) {
			listener.compareMonitor(assignment, text);
		}
	}

	/**
	 * 
	 * @param workItem
	 *            WorkItem
	 */
	public void completeActivity(final WorkItem workItem) {
		if (workItem != null) {
			final Event event = new Event(getUser(), workItem.getActivity(), Event.Type.COMPLETED);
			final boolean canComplete = getAssignmentState().containsEvent(event);
			if (!canComplete) {
				MessagePane.inform(panel, "At the moment it is not possible to complete activity "
						+ workItem.getActivity());
			} else {
				final boolean dataOk = activitiesPane.readData(workItem);
				if (dataOk) {
					if (warn(event)) {
						if (executeEvent(workItem, event) != null) {
							removeWorkItem(workItem);
						} else {
							MessagePane.inform(panel,
									"At the moment it is not possible to complete activity "
											+ workItem.getActivity());
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param WorkItemEvent
	 *            event
	 */
	public void forwardActivity(final WorkItem wi, final Event event) {
		if (event != null && listener != null) {
			final Activity activity = wi.getActivity();
			// add parameters
			final IMessage response = listener.executeRequest(MessageFactory.eventForward(wi,
					event, activity.getExternalCase()));
			if (response instanceof EngineEventReject) {
				MessagePane.inform(panel,
						"At the moment it is not possible to start/forward activity " + activity);
			}
		}
	}

	/**
	 * 
	 * @return Assignment
	 */
	public Assignment getAssignment() {
		// return getState().getAssignment();
		return assignment;
	}

	/**
	 * 
	 * @return AssignmentState
	 */
	public AssignmentState getAssignmentState() {
		return getState();
	}

	public HistoryCoordinator getHistoryCoordinator() {
		return history;
	}

	/**
	 * 
	 * @return AssignmentState
	 */
	public AssignmentState getState() {
		return view.getState();
	}

	/**
	 * 
	 * @return User
	 */
	public User getUser() {
		return user;
	}

	/**
	 * 
	 * @return AssignmentView
	 */
	public AssignmentView getView() {
		return view;
	}

	/**
	 * 
	 * @param id
	 *            int
	 * @return WorkItem
	 */
	public WorkItem getWorkItem(final int id) {
		WorkItem item = null;
		final Iterator<WorkItem> iterator = items.iterator();
		boolean found = false;
		while (iterator.hasNext() && !found) {
			item = iterator.next();
			found = item.getId() == id;
		}
		return found ? item : null;
	}

	/**
	 * 
	 * @return JPanel
	 */
	public JPanel getWorkPanel() {
		view.getGraph().updateUI();
		activitiesPane.updateUI();
		return panel;
	}

	/**
	 * 
	 * @return int
	 */
	@Override
	public int hashCode() {
		return getAssignment().hashCode();
	}

	public void predict(final String query) {
		if (listener != null) {
			listener.predict(assignment, query);
		}
	}

	public void predictMonitor(final String text) {
		if (listener != null) {
			listener.predictMonitor(assignment, text);
		}
	}

	public void processResult(final int id, final ResponseSet<?> result) {
		if (result != null) {
			recommendation.deliverResult(id, result);
		}
	}

	public void monitorRemoved(final int id) {
			recommendation.removeMonitor(id);
	}

	public void recommend(final String query) {
		if (listener != null) {
			listener.recommend(assignment, query);
		}
	}

	public void recommendMonitor(final String text) {
		if (listener != null) {
			listener.recommendMonitor(assignment, text);
		}
	}

	public void removeMonitor(final int id) {
		if (listener != null) {
			listener.removeMonitor(assignment, id);
		}
	}

	public void setAssignment(final Assignment assignment) {
		this.assignment = assignment;
		history.setAssignment(assignment);
	}

	public void setListener(final IListener l) {
		listener = l;
	}

	public void setView(final AssignmentView view) {
		this.view = view;
		if (view != null) {
			view.setListener(this);
		}
		graphPanel.removeAll();
		if (view != null) {
			graphPanel.add(view.getGraph(), BorderLayout.CENTER);
		}
	}

	public void simple(final String query) {
		if (listener != null) {
			listener.simple(assignment, query);
		}
	}

	public void simpleMonitor(final String text) {
		if (listener != null) {
			listener.simpleMonitor(assignment, text);
		}
	}

	/**
	 * 
	 * @param activity
	 *            Activity
	 */
	public void startActivity(final Activity activity) {
		if (activity != null) {
			final Event event = new Event(getUser(), activity, Event.Type.STARTED);
			if (view.getState().containsEvent(event)) {
				// check for violations
				if (warn(event)) {
					final WorkItem wi = new WorkItem(0, activity, getUser());
					if (activity.isExternal()) { // if this is an external activity then forward it
						forwardActivity(wi, event);
					} else { // if it is not an external activity just start it
						final WorkItem workItem = executeWorkItem(wi, event);
						if (workItem != null) {
							items.add(workItem);
							activitiesPane.addActivity(workItem, this);
							stateChanged();
						} else {
							MessagePane.inform(panel,
									"At the moment it is not possible to start activity "
											+ activity);
						}
					}
				}
			}
		}
	}

	/**
   *
   */
	public void stateChanged() {
		view.updateStateView();
		final AssignmentState state = view.getState();
		activitiesPane.disableAll();
		view.updateStateView();
		for (int i = 0; i < state.eventCount(); i++) {
			final AssignmentState.EventState eventState = state.eventAt(i);
			final Event event = eventState.event();
			if (event.getType().equals(Event.Type.COMPLETED)) {
				final Activity activity = state.getAssignment().getActivityForDefinition(
						event.getActivity());
				activitiesPane.setEnabledComplete(activity, true);
			}
			if (event.getType().equals(Event.Type.CANCELLED)) {
				final Activity activity = state.getAssignment().getActivityForDefinition(
						event.getActivity());
				activitiesPane.setEnabledCancel(activity, true);
			}
		}
		panel.repaint();
	}

	/**
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		return getAssignment().toString();
	}

	protected IMessage executeEvent(final WorkItem wi, final Event event) {
		if (event != null) {
			// AssignmentState.EventState eventState = getState().eventState(event. getEvent());
			if ( /*eventState != null && */listener != null) // ask the engine here
				return listener.executeRequest(MessageFactory.eventRequest(wi, event));
		}
		return null;
	}

	protected WorkItem executeWorkItem(final WorkItem wi, final Event event) {
		final IMessage response = executeEvent(wi, event);
		WorkItem workItem = null;
		if (response instanceof EngineEventAccept) {
			// workItem = execute(wi.getActivity(), event,( (EngineEventAccept) response));
			workItem = ((EngineEventAccept) response).createItem(getUser(), wi.getActivity());
		}
		return workItem;
	}

	private JFrame getMainFrame() {
		return frame;
	}

	/**
   *
   */
	private void prepareUI() {
		graphPanel = new TPanel(new BorderLayout(), getAssignment().toString());

		final JScrollPane graph = new JScrollPane(graphPanel);
		final JScrollPane activity = new JScrollPane(activitiesPane);

		split = new TSplitPane(JSplitPane.HORIZONTAL_SPLIT, graph, recommendation);
		Dimension d = new Dimension(recommendation.getPreferredSize());
		d.setSize(Math.max(d.getWidth(), 200), d.getHeight());
		recommendation.setPreferredSize(d);
		split.setDividerLocation(700);

		final JSplitPane splitPane = new TSplitPane(JSplitPane.VERTICAL_SPLIT, split, activity);
		splitPane.setDividerLocation(500);

		final Dimension minimumSize = new Dimension(200, 100);
		graph.setMinimumSize(minimumSize);
		activity.setMinimumSize(minimumSize);
		recommendation.setMinimumSize(minimumSize);

		panel.add(splitPane);
	}

	/**
	 * 
	 * @param item
	 *            WorkItem
	 */
	private void removeWorkItem(final WorkItem item) {
		items.remove(item);
		activitiesPane.removeWorkItem(item);
	}

	private boolean violate(final String msg, final Collection<Constraint> constraints) {
		frmWarning.fill(msg, constraints);
		return frmWarning.showCentered();
	}

	/**
	 * 
	 * @param event
	 *            AbstractEvent
	 * @return boolean
	 */
	private boolean warn(final Event event) {
		boolean violate = true;
		final AssignmentState.EventState eventState = getState().eventState(event);

		if (eventState != null) {
			// check if some constraints would be violated with this event
			if (eventState.violatedCout() > 0) {
				final Collection<Constraint> constraints = new ArrayList<Constraint>();

				// loop through all violated constraints to inform the user
				for (int i = 0; i < eventState.violatedCout(); i++) {
					final Constraint constraint = eventState.violatesAt(i);
					if (!constraint.getMandatory()) {
						constraints.add(constraint);
					}
				}
				if (constraints.size() > 0) {
					final String msg = "Event \"" + event.getProposition() + "\" will violate "
							+ constraints.size() + " constraint"
							+ (constraints.size() == 1 ? "." : "s.");
					violate = violate(msg, constraints);
				}
			}
		}
		return violate;
	}

	State state() {
		return getAssignment().getState();
	}

	/**
	 * 
	 * @return boolean
	 */
	boolean warnClosing() {
		boolean violate = true;
		final Collection<Constraint> constraints = new ArrayList<Constraint>();

		// loop through all violated constraints to inform the user
		for (int i = 0; i < getAssignment().constraintDefinitionsCount(); i++) {
			final Constraint constraint = getAssignment().constraintAt(i);
			if (!constraint.getMandatory()) {
				if (constraint.getState().equals(State.VIOLATED_TEMPORARY)) {
					constraints.add(constraint);
				}
			}
		}
		if (constraints.size() > 0) {
			final String msg = "Closing assignment \"" + getAssignment() + "\" will violate "
					+ constraints.size() + " constraint" + (constraints.size() == 1 ? "." : "s.");
			violate = violate(msg, constraints);
		}
		return violate;
	}
}
