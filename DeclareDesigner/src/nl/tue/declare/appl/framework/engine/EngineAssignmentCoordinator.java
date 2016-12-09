package nl.tue.declare.appl.framework.engine;

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
import java.io.File;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.tue.declare.appl.framework.recommendation.RecommendationCoordinator;
import nl.tue.declare.datamanagement.AssignmentBroker;
import nl.tue.declare.datamanagement.AssignmentViewBroker;
import nl.tue.declare.datamanagement.XMLBrokerFactory;
import nl.tue.declare.datamanagement.assignment.XMLAssignmentViewBroker;
import nl.tue.declare.domain.instance.Assignment;
import nl.tue.declare.domain.instance.Constraint;
import nl.tue.declare.domain.instance.Event;
import nl.tue.declare.domain.model.ConstraintDefinition;
import nl.tue.declare.domain.organization.User;
import nl.tue.declare.execution.AssignmentState;
import nl.tue.declare.execution.Execution;
import nl.tue.declare.execution.LTLInstanceExecutionHandler;
import nl.tue.declare.execution.WorkItem;
import nl.tue.declare.execution.msg.Ack;
import nl.tue.declare.execution.msg.IMessage;
import nl.tue.declare.execution.msg.MessageFactory;
import nl.tue.declare.execution.msg.worklist.QueryMessage.QueryType;
import nl.tue.declare.graph.model.AssignmentModelView;
import nl.tue.declare.utils.XMLParser;

public class EngineAssignmentCoordinator {

	private String assignmentString;
	private Assignment assignment;
	private Execution execution;
	private AssignmentState assignmentState;

	private final List<RemoteWorklist> worklists;

	private final LTLInstanceExecutionHandler strategy;

	private RecommendationCoordinator recommendationCoordinator;

	private IAssignmentStateListener assignmentStateListener;

	private final File file;

	private static int nextId = 0;

	private final Map<Integer, QueryType> monitorTypes = Collections
			.synchronizedMap(new HashMap<Integer, QueryType>());

	private final Map<Integer, String> monitorQueries = Collections
			.synchronizedMap(new HashMap<Integer, String>());

	/**
	 * 
	 * @param assignment
	 *            Assignment
	 * @param file
	 *            File
	 */
	protected EngineAssignmentCoordinator(final Assignment assignment, final File file)
			throws Throwable {
		super();
		this.file = file;
		this.assignment = assignment;
		assignmentState = new AssignmentState(this.assignment);
		execution = new Execution(this.assignment);

		assignmentState.addWorkItemListener(execution);
		assignmentStateListener = null;

		worklists = Collections.synchronizedList(new ArrayList<RemoteWorklist>());

		recommendationCoordinator = null;
		// read the assignment from the file
		getAssignmentAndViewString(file.getAbsolutePath());

		// here we choose the strategy and the
		// algorithm for translation of LTL to automata
		// in this case, we use the general strategy and the algorithm for finite traces
		strategy = new LTLInstanceExecutionHandler(this.assignment);
		getAssignmentState().addListener(strategy);
	}

	public boolean active(final int id) {
		boolean active = true;
		final ConstraintDefinition constraint = assignment.constraintWithId(id);
		if (constraint != null && constraint instanceof Constraint) {
			active = ((Constraint) constraint).isActive();
		}
		return active;
	}

	/**
	 * 
	 * @param worklist
	 *            RemoteWorklist
	 */
	public synchronized void addWorklist(final RemoteWorklist worklist) {
		if (worklist != null) {
			if (match(worklist)) { // only if this user belongs to my team
				if (!worklists.contains(worklist)) {
					synchronized (worklists) {
						worklists.add(worklist);
					}
					recommendationCoordinator.addUser(worklist.getUser());
					initiate(worklist);
				}
			}
		}
	}

	/**
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean canComplete() {
		return true;
	}

	public void change(final Assignment change, final AssignmentModelView view) throws Throwable {
		setAssignment(change);
		setAssignmentAndViewString(assignment, view);
		changeModel();
		changeState();
	}

	/**
   *
   */
	public synchronized void changeModel() {
		synchronized (worklists) {
			final Iterator<RemoteWorklist> iterator = worklists.iterator();
			while (iterator.hasNext()) {
				final RemoteWorklist worklist = iterator.next();
				changeAssignment(worklist);
			}
		}
	}

	/**
   *
   */
	public synchronized void changeState() {
		createState();
		synchronized (worklists) {
			final Iterator<RemoteWorklist> iterator = worklists.iterator();
			while (iterator.hasNext()) {
				final RemoteWorklist worklist = iterator.next();
				final List<IMessage> changeMessages = createChangeMessages(worklist);
				sendChangeMessages(worklist, changeMessages);
			}
		}
	}

	/**
	 * 
	 * @return boolean
	 * @param secure
	 *            boolean
	 * @throws Exception
	 */
	public synchronized boolean closeAssignment(final boolean secure) {
		System.out.println("Closing assignment");
		final boolean ok = secure ? canComplete() : true;
		if (ok) {
			synchronized (worklists) {
				final Iterator<RemoteWorklist> iterator = worklists.iterator();
				while (iterator.hasNext()) {
					final RemoteWorklist worklist = iterator.next();
					this.closeAssignment(worklist);
				}
			}
			System.out.println("Closing assignment - worklists notified");
			recommendationCoordinator.done();
		}
		return ok;
	}

	/**
	 * 
	 * @param worklist
	 *            boolean
	 */
	public synchronized void closeAssignment(final RemoteWorklist worklist) {
		worklist.sendInformation(MessageFactory.messageCloseAssignment(assignment));
	}

	/**
	 * 
	 * @param item
	 *            User
	 * @param event
	 *            String
	 * @return WorkItemEvent
	 */
	public synchronized boolean event(final WorkItem item, final Event event) {
		execute(item, event);
		boolean ok = false;
		if (event != null) { // accept
			ok = strategy.next(event);
			changeState();
		}
		return ok;
	}

	public boolean forAssignment(final String id) {
		return assignment.getId() == Integer.parseInt(id);
	}

	/**
	 * 
	 * @return Assignment
	 */
	public Assignment getAssignment() {
		return assignment;
	}

	public String getAssignmentString() {
		return assignmentString;
	}

	public File getFile() {
		return file;
	}

	/**
	 * 
	 * @param worklist
	 *            RemoteWorklist
	 * @return boolean
	 */
	public boolean match(final RemoteWorklist worklist) {
		boolean ok = false;
		if (worklist != null) {
			final User user = worklist.getUser();
			ok = assignment.getTeam().isMember(user);
		}
		return ok;
	}

	public IMessage monitor(final QueryType type, final String query) {
		final int id = nextId++;
		synchronized (monitorTypes) {
			monitorTypes.put(id, type);
			monitorQueries.put(id, query);
		}
		final IMessage result = query(type, id, query);
		synchronized (worklists) {
			for (final RemoteWorklist worklist : worklists) {
				worklist.sendInformation(result);
			}
		}
		return new Ack();
	}

	public IMessage query(final QueryType type, final int id, final String query) {
		switch (type) {
			case SIMPLE:
				return MessageFactory.messageResponseAssignment(assignment, id,
						recommendationCoordinator.simple(query));
			case COMPARE:
				return MessageFactory.messageResponseAssignment(assignment, id,
						recommendationCoordinator.compare(query));
			case PREDICT:
				return MessageFactory.messageResponseAssignment(assignment, id,
						recommendationCoordinator.predict(query));
			case RECOMMEND:
				return MessageFactory.messageResponseAssignment(assignment, id,
						recommendationCoordinator.recommend(query));
		}

		return null;
	}

	public synchronized void reInitiate() {
		synchronized (worklists) {
			final List<RemoteWorklist> remove = new ArrayList<RemoteWorklist>();
			final Iterator<RemoteWorklist> iterator = worklists.iterator();
			while (iterator.hasNext()) {
				final RemoteWorklist worklist = iterator.next();
				if (!match(worklist)) {
					remove.add(worklist);
					closeAssignment(worklist);
				}
			}
			worklists.removeAll(remove);
		}
	}

	public IMessage removeMonitor(final int id) {
		synchronized (monitorTypes) {
			monitorTypes.remove(id);
			monitorQueries.remove(id);
		}
		final IMessage result = MessageFactory.messageMonitorRemoved(assignment, id);
		synchronized (worklists) {
			for (final RemoteWorklist worklist : worklists) {
				worklist.sendInformation(result);
			}
		}
		return new Ack();
	}

	/**
	 * 
	 * @param worklist
	 *            RemoteWorklist
	 */
	public void removeWorkList(final RemoteWorklist worklist) {
		if (worklist != null) {
			synchronized (worklists) {
				worklists.remove(worklist);
			}
			recommendationCoordinator.removeUser(worklist.getUser());
		}
	}

	public boolean sameModel(final EngineAssignmentCoordinator c) {
		return c.file.equals(file);
	}

	public void setAssignment(final Assignment assignment) throws Throwable {
		this.assignment = assignment;
		assignmentState = new AssignmentState(this.assignment);
		execution = new Execution(this.assignment);
		assignmentState.addWorkItemListener(execution);

		strategy.reset(assignment);
		getAssignmentState().addListener(strategy);
	}

	public void setAssignmentAndViewString(final Assignment assignment,
			final AssignmentModelView view) {
		final AssignmentBroker broker = XMLBrokerFactory.newAssignmentBroker();
		if (broker != null) {
			if (broker instanceof XMLAssignmentViewBroker) {
				final XMLAssignmentViewBroker xmlBroker = (XMLAssignmentViewBroker) broker;
				assignmentString = XMLParser.toString(xmlBroker.assignmentAndViewElement(
						assignment, view));
			}
		}
	}

	public void setAssignmentStateListener(final IAssignmentStateListener listener) {
		assignmentStateListener = listener;
		createState();
	}

	public void setRecommendationProvider(final String host, final int port) {
		try {
			recommendationCoordinator = new RecommendationCoordinator(assignmentState, host, port);
		} catch (final UnknownHostException e) {
		}
	}

	@Override
	public String toString() {
		return Integer.toString(assignment.getId());
	}

	protected synchronized RemoteWorklist getWorklist(final User user) {
		RemoteWorklist worklist = null;
		boolean found = false;
		synchronized (worklists) {
			final Iterator<RemoteWorklist> iterator = worklists.iterator();
			while (iterator.hasNext() && !found) {
				worklist = iterator.next();
				found = worklist.getUser().equals(user);
			}
		}
		return found ? worklist : null;
	}

	protected void initiate(final RemoteWorklist worklist) {
		openAssignment(worklist);
		changeState();
	}

	protected void sendChangeMessages(final RemoteWorklist worklist, final List<IMessage> msgs) {
		if (worklist != null && msgs != null) {
			final Iterator<IMessage> i = msgs.iterator();
			while (i.hasNext()) {
				worklist.sendInformation(i.next());
			}
		}
	}

	/**
	 * 
	 * @param state
	 *            AssignmentState
	 * @param user
	 *            RemoteWorklist
	 * @return AssignmentState
	 */
	private AssignmentState authorize(final AssignmentState state, final User user) {
		return state.authorize(user);
	}

	private void changeAssignment(final RemoteWorklist worklist) {
		final IMessage change = MessageFactory.createChangeAssignmentMassage(assignment,
				assignmentString);
		worklist.sendInformation(change);
	}

	private List<IMessage> createChangeMessages(final RemoteWorklist worklist) {
		final IMessage state = getState(worklist);
		final List<IMessage> msgs = new ArrayList<IMessage>();
		synchronized (monitorTypes) {
			for (final Entry<Integer, QueryType> entry : monitorTypes.entrySet()) {
				final IMessage recommendation = query(entry.getValue(), entry.getKey(),
						monitorQueries.get(entry.getKey()));
				if (recommendation != null) {
					msgs.add(recommendation);
				}
			}
		}
		if (state != null) { // then state
			msgs.add(state);
		}
		return msgs;
	}

	private synchronized void createState() {
		try {
			getAssignmentState().createState();
			if (assignmentStateListener != null) {
				assignmentStateListener.stateChanged(getAssignmentState());
			}
		} catch (final Throwable ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 * @param item
	 *            String
	 * @param event
	 *            Event
	 * @return WorkItemEvent
	 */
	private void execute(final WorkItem item, final Event event) {
		execution.execute(item, event);
		if (event.getType().equals(Event.Type.COMPLETED)) {
			item.writeData();
		}
	}

	/**
	 * 
	 * @param path
	 *            String
	 */
	private void getAssignmentAndViewString(final String path) {
		final AssignmentViewBroker broker = XMLBrokerFactory.newAssignmentBroker(path);
		if (broker != null) {
			if (broker instanceof XMLAssignmentViewBroker) {
				final XMLAssignmentViewBroker xmlBroker = (XMLAssignmentViewBroker) broker;
				assignmentString = XMLParser.toString(xmlBroker.getAssignmentAndView());
			}
		}
	}

	private AssignmentState getAssignmentState() {
		return assignmentState;
	}

	/**
	 * 
	 * @param worklist
	 *            RemoteWorklist
	 * @return IMessage
	 */
	private IMessage getState(final RemoteWorklist worklist) {
		final AssignmentState authorizatiton = authorize(getAssignmentState(), worklist.getUser());
		return MessageFactory.createStateAssignmentMassage(authorizatiton);
	}

	private void openAssignment(final RemoteWorklist worklist) {
		final IMessage open = MessageFactory.createOpenAssignementMessage(assignment,
				assignmentString);
		worklist.sendInformation(open);
	}
}
