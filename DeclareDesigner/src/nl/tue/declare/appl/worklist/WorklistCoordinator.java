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

import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import nl.tue.declare.appl.util.ErrorMessage;
import nl.tue.declare.appl.util.swing.MessagePane;
import nl.tue.declare.appl.worklist.gui.IWorklistFrameListener;
import nl.tue.declare.appl.worklist.gui.LogInFrame;
import nl.tue.declare.appl.worklist.gui.WorklistFrame;
import nl.tue.declare.datamanagement.XMLBrokerFactory;
import nl.tue.declare.datamanagement.assignment.XMLAssignmentViewBroker;
import nl.tue.declare.datamanagement.assignment.XMLAssignmnetStateBroker;
import nl.tue.declare.domain.instance.Assignment;
import nl.tue.declare.domain.instance.State;
import nl.tue.declare.domain.model.AssignmentModel;
import nl.tue.declare.domain.organization.Administrator;
import nl.tue.declare.domain.organization.User;
import nl.tue.declare.execution.AssignmentState;
import nl.tue.declare.execution.msg.AssignmentMessage;
import nl.tue.declare.execution.msg.IMessage;
import nl.tue.declare.execution.msg.MessageFactory;
import nl.tue.declare.execution.msg.engine.msg.ChangeAssignmentMessage;
import nl.tue.declare.execution.msg.engine.msg.CloseAssignmentEngineMessage;
import nl.tue.declare.execution.msg.engine.msg.HistoryAssignmentMessage;
import nl.tue.declare.execution.msg.engine.msg.MonitorRemovedMessage;
import nl.tue.declare.execution.msg.engine.msg.OpenAssignmentMessage;
import nl.tue.declare.execution.msg.engine.msg.ResponseAssignmentMessage;
import nl.tue.declare.execution.msg.engine.msg.StateAssignmentMessage;
import nl.tue.declare.execution.msg.engine.response.EngineCloseAssignmentAccept;
import nl.tue.declare.execution.msg.engine.response.EngineCloseAssignmentReject;
import nl.tue.declare.execution.msg.engine.response.EngineLogInAccept;
import nl.tue.declare.execution.msg.engine.response.EngineLogInReject;
import nl.tue.declare.execution.msg.worklist.MessageLogIn;
import nl.tue.declare.execution.msg.worklist.QueryMessage.QueryType;
import nl.tue.declare.graph.assignment.AssignmentView;

import org.processmining.operationalsupport.messages.reply.Recommendation;
import org.processmining.operationalsupport.messages.reply.ResponseSet;
import org.processmining.operationalsupport.xml.OSXMLConverter;

public class WorklistCoordinator implements WindowListener, IRemoteEngineListener,
		IWorklistFrameListener, WorklistAssignmentCoordinator.IListener {

	protected RemoteEngineProxy proxy;

	private LogInFrame logInFrame;

	protected User user;
	private int id = -1;

	protected List<WorklistAssignmentCoordinator> coordinators;

	private final Object readLock = new Object();
	private final OSXMLConverter converter;

	private WorklistFrame frame;

	public WorklistCoordinator() {
		super();
		coordinators = new ArrayList<WorklistAssignmentCoordinator>();
		converter = new OSXMLConverter();
		ini();
	}

	/**
	 * 
	 * @param assignmentId
	 *            String
	 * @return AssignmentCoordinator
	 */
	public WorklistAssignmentCoordinator assignmentCoordinator(final Assignment assignment) {
		boolean found = false;
		int i = 0;
		WorklistAssignmentCoordinator coordinator = null;
		while (i < coordinators.size() && !found) {
			coordinator = coordinators.get(i++);
			found = coordinator.getAssignment() == assignment;
		}
		return found ? coordinator : null;
	}

	/**
	 * 
	 * @param assignmentId
	 *            String
	 * @return AssignmentCoordinator
	 */
	public WorklistAssignmentCoordinator assignmentCoordinator(final int assignmentId) {
		boolean found = false;
		int i = 0;
		WorklistAssignmentCoordinator coordinator = null;
		while (i < coordinators.size() && !found) {
			coordinator = coordinators.get(i++);
			found = coordinator.getAssignment().getId() == assignmentId;
		}
		return found ? coordinator : null;
	}

	/**
	 * 
	 * @param assignmentId
	 *            String
	 * @return AssignmentCoordinator
	 */
	public WorklistAssignmentCoordinator assignmentCoordinator(final String assignmentId) {
		WorklistAssignmentCoordinator coordinator = null;
		try {
			final int id = Integer.parseInt(assignmentId);
			coordinator = assignmentCoordinator(id);
		} catch (final Exception e) {
			// ignore
		}
		return coordinator;
	}

	/**
	 * 
	 * @param object
	 *            Object
	 */
	public void assignmentSelected(final Object object) {
		if (object != null) {
			if (object instanceof WorklistAssignmentCoordinator) {
				final WorklistAssignmentCoordinator assignment = (WorklistAssignmentCoordinator) object;
				frame.addWorkPanel(assignment.getWorkPanel());
			}
		}
	}

	/**
	 * cancelLogIn
	 */
	public void cancelLogIn() {
		System.exit(0);
	}

	/**
	 * 
	 * @param object
	 *            Object
	 */
	public void closeAssignment(final Object object) {
		if (object != null) {
			if (object instanceof WorklistAssignmentCoordinator) {
				this.closeAssignment((WorklistAssignmentCoordinator) object);
			}
		}
	}

	/**
	 * 
	 * @param object
	 *            Object
	 */
	public void closeAssignment(final WorklistAssignmentCoordinator coordinator) {
		if (coordinator != null) {
			if (close(coordinator)) {
				if (coordinator.warnClosing()) {
					request(MessageFactory.messageCloseAssignment(coordinator.getAssignment()));
				}
			}
		}
	}

	public void compare(final Assignment assignment, final String query) {
		final IMessage msg = MessageFactory.messageQuery(assignment, QueryType.COMPARE, query);
		request(msg);
	}

	@Override
	public void compareMonitor(final Assignment assignment, final String text) {
		final IMessage msg = MessageFactory.messageMonitor(assignment, QueryType.COMPARE, text);
		request(msg);
	}

	public void connected(final RemoteEngineProxy engine) {
		// TODO Auto-generated method stub

	}

	public void disconnected(final RemoteEngineProxy engine) {
		// TODO Auto-generated method stub

	}

	public IMessage executeRequest(final IMessage request) {
		return proxy.request(request);
	}

	public void finish() {
		logOut();
	}

	/**
	 * 
	 * @param assignmentId
	 *            String
	 * @return AssignmentCoordinator
	 */
	public WorklistAssignmentCoordinator getAssignmentCoordinator(final Assignment assignment) {
		WorklistAssignmentCoordinator coordinator = assignmentCoordinator(assignment.getId());
		if (coordinator == null) {
			coordinator = new WorklistAssignmentCoordinator(assignment, frame, getUser());
			coordinators.add(coordinator);
		}
		return coordinator;
	}

	public int getId() {
		return id;
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
	 * @param username
	 *            String
	 * @param password
	 *            String
	 */
	public void logIn(final String username, final String password) {

		final SettingsCoordinator ini = SettingsCoordinator.singleton(); // initialize form ini file
		final String host = ini.getHost(); // load engine host from in file
		final int portInfo = ini.getPortInfo(); // load engine information port from ini file
		final int portReq = ini.getPortReqest(); // load engine request port from ini file

		// create a proxy for connection with the engine
		proxy = new RemoteEngineProxy(username, password, portInfo, portReq, host);

		proxy.addListener(this);
		try {
			// conect the proxy to the engine
			proxy.connect();
			// create a request message for logging in
			final MessageLogIn msgLogIn = new MessageLogIn(Integer.toString(id), username, password);

			// get the response
			final IMessage response = proxy.request(msgLogIn);
			if (response != null) { // if no error
				if (response instanceof EngineLogInAccept) { // the logging in is ACCEPTED
					id = Integer.parseInt(((EngineLogInAccept) response).getId());

					proxy.monitorLogIn(new MessageLogIn(Integer.toString(id), username, password));
					// hide the loggingin frame
					logInFrame.setVisible(false);
					// accept logging -> preceede by starting-up the worklist form
					acceptLogIn(username, password);
				} else if (response instanceof EngineLogInReject) { // logging in is REJECTED
					// display the message about the reason of rejection
					MessagePane.inform(logInFrame, ((EngineLogInReject) response).getReason()
					/*handler.handle()*/);
				}
			}
		} catch (final Exception ex) {
			ErrorMessage.showError(logInFrame, ex, "Could not connect to engine.");
		}
	}

	public void predict(final Assignment assignment, final String query) {
		final IMessage msg = MessageFactory.messageQuery(assignment, QueryType.PREDICT, query);
		request(msg);
	}

	@Override
	public void predictMonitor(final Assignment assignment, final String text) {
		final IMessage msg = MessageFactory.messageMonitor(assignment, QueryType.PREDICT, text);
		request(msg);
	}

	public void received(final String message) {
		System.out.println("RECV: " + message);
		IMessage received = null;
		synchronized (readLock) {
			received = MessageFactory.getEngineResponse(message);
		}
		if (received != null) {
			receive(received);
		}
	}

	public void recommend(final Assignment assignment, final String query) {
		final IMessage msg = MessageFactory.messageQuery(assignment, QueryType.RECOMMEND, query);
		request(msg);
	}

	@Override
	public void recommendMonitor(final Assignment assignment, final String text) {
		final IMessage msg = MessageFactory.messageMonitor(assignment, QueryType.RECOMMEND, text);
		request(msg);
	}

	public void sent(final RemoteEngineProxy engine, final String msg) {
		System.out.println("SEND: " + msg);
		// TODO Auto-generated method stub

	}

	public void setId(final int id) {
		this.id = id;
	}

	public void simple(final Assignment assignment, final String query) {
		final IMessage msg = MessageFactory.messageQuery(assignment, QueryType.SIMPLE, query);
		request(msg);
	}

	@Override
	public void simpleMonitor(final Assignment assignment, final String text) {
		final IMessage msg = MessageFactory.messageMonitor(assignment, QueryType.SIMPLE, text);
		request(msg);
	}

	public void windowActivated(final WindowEvent e) {
	}

	public void windowClosed(final WindowEvent e) {
		proxy.finish();
		logOut();
		System.exit(0);
	}

	public void windowClosing(final WindowEvent e) {
	}

	public void windowDeactivated(final WindowEvent e) {
	}

	public void windowDeiconified(final WindowEvent e) {
	}

	public void windowIconified(final WindowEvent e) {
	}

	public void windowOpened(final WindowEvent e) {
	}

	protected void added(final WorklistAssignmentCoordinator coordinator) {
		if (coordinator != null) {
			frame.addAssignment(coordinator);
		}
	}

	protected int assignmentCount() {
		return coordinators.size();
	}

	protected void begin() {
		// startup the worklist frame
		frame = new WorklistFrame(user.getUserName());
		frame.addWindowListener(this);
		frame.addWorklistFrameListener(this);
		frame.showMe();
	}

	protected void changed(final WorklistAssignmentCoordinator coordinator) {
		if (coordinator != null) {
			final Color color = coordinator.getView().getColor();
			final boolean enabled = coordinator.getState().enabled();
			frame.setState(coordinator, enabled, color);
			coordinator.stateChanged();
		}
	}

	protected boolean close(final WorklistAssignmentCoordinator coordinator) {
		if (coordinator != null) {
			if (!coordinator.state().equals(State.SATISFIED)) {
				MessagePane.inform(frame, "Cannot close assignment "
						+ coordinator.getAssignment().toString()
						+ " because some of the constraints are violated.");
				return false;
			} else {
				final String msg = "Are you sure you want to close assignment "
						+ coordinator.getAssignment() + "?";
				return MessagePane.ask(frame, msg);
			}
		}
		return true;
	}

	protected void closed(final WorklistAssignmentCoordinator coordinator) {
		if (coordinator != null) {
			frame.removeAssignment(coordinator);
			frame.repaint();
		}
	}

	protected void logOut() {
		final IMessage msg = MessageFactory.logOut(Integer.toString(id), user.getUserName(), user
				.getPassword());
		request(msg);
	}

	protected void receive(final IMessage msg) {
		if (msg instanceof AssignmentMessage) { // AssignmentMessage
			final WorklistAssignmentCoordinator coordinator = this
					.assignmentCoordinator(((AssignmentMessage) msg).id());
			if (msg instanceof OpenAssignmentMessage) { // OpenAssignment
				openModel(((OpenAssignmentMessage) msg).id(), ((OpenAssignmentMessage) msg)
						.getModel());
			} else if (msg instanceof CloseAssignmentEngineMessage) { // CloseAssignment
				assignmentClosed(coordinator);
			} else if (msg instanceof StateAssignmentMessage) { // StateAssignment
				state(coordinator, ((StateAssignmentMessage) msg).getState());
			} else if (msg instanceof ChangeAssignmentMessage) { // ChangeAssignment
				change(coordinator, ((ChangeAssignmentMessage) msg).getModel());
			} else if (msg instanceof ResponseAssignmentMessage) { // RecommendationAssignment
				ResponseAssignmentMessage responseAssignmentMessage = (ResponseAssignmentMessage) msg;
				response(coordinator, responseAssignmentMessage.getId(), responseAssignmentMessage.getRecommendation());
			} else if (msg instanceof MonitorRemovedMessage) { // RecommendationAssignment
				MonitorRemovedMessage responseAssignmentMessage = (MonitorRemovedMessage) msg;
				monitorRemoved(coordinator, responseAssignmentMessage.getId());
			} else if (msg instanceof HistoryAssignmentMessage) { // HistoryAssignment
				history(coordinator, ((HistoryAssignmentMessage) msg).getHistory());
			} else if (msg instanceof EngineCloseAssignmentAccept) { // CloseAssignmentAccept
				assignmentClosed(coordinator);
			} else if (msg instanceof EngineCloseAssignmentReject) { // CloseAssignmentReject
				MessagePane.inform(frame, ((EngineCloseAssignmentReject) msg).getReason());
			}
		}
	}

	/**
	 * 
	 * @param username
	 *            String
	 * @param password
	 *            String
	 */
	private void acceptLogIn(final String username, final String password) {
		// create a fake user object for the username and password
		user = new User(0);
		user.setUserName(username);
		user.setPassword(password);

		// wait for messages from the proxy engine
		proxy.start();
		begin();
	}

	private void addAssignment(final WorklistAssignmentCoordinator coordinator) {
		coordinator.setListener(this);
		added(coordinator);
	}

	/**
	 * 
	 * @param id
	 *            String
	 */
	private void assignmentClosed(final WorklistAssignmentCoordinator coordinator) {
		if (coordinator != null) {
			coordinators.remove(coordinator);
			closed(coordinator);
		}
	}

	private void change(WorklistAssignmentCoordinator coordinator, final String assignmentString) {
		final int id = coordinator.getAssignment().getId();

		final XMLAssignmentViewBroker broker = (XMLAssignmentViewBroker) XMLBrokerFactory
				.newAssignmentBroker();
		final AssignmentModel model = broker.readAssignmentfromString(assignmentString);

		final Assignment assignment = new Assignment(id, model);
		final AssignmentState state = new AssignmentState(assignment);

		final AssignmentView view = new AssignmentView(state);

		coordinator = getAssignmentCoordinator(assignment);
		coordinator.setView(view);
		broker.readAssignmentGraphicalFromString(model, view, assignmentString);
		coordinator.change(assignment, view);
	}

	private void history(final WorklistAssignmentCoordinator coordinator, final String history) {
		/* HistoryCoordinator history = this.coordinator.getHistoryCoordinator();
		     history.update(this.history);*/
	}

	/**
   *
   */
	private void ini() {
		logInFrame = new LogInFrame("Worklist", this, Administrator.singleton().getUserName(),
				Administrator.singleton().getPassword());
		logInFrame.showMe();
	}

	private void openModel(final String id, final String assignmentString) {
		final XMLAssignmentViewBroker broker = (XMLAssignmentViewBroker) XMLBrokerFactory
				.newAssignmentBroker();
		final AssignmentModel model = broker.readAssignmentfromString(assignmentString);
		final Assignment assignment = new Assignment(new Integer(id).intValue(), model);
		final AssignmentState state = new AssignmentState(assignment);
		final AssignmentView view = new AssignmentView(state);
		final WorklistAssignmentCoordinator coordinator = getAssignmentCoordinator(assignment);
		coordinator.setView(view);
		broker.readAssignmentGraphicalFromString(model, view, assignmentString);
		addAssignment(coordinator);
	}

	/**
	 * 
	 * @param coordinator
	 *            AssignmentCoordinator
	 */
	private synchronized void processResult(final WorklistAssignmentCoordinator coordinator,
			final int id, final ResponseSet<?> result) {
		if (coordinator != null) {
			coordinator.processResult(id, result);
		}
	}

	private synchronized void monitorRemoved(final WorklistAssignmentCoordinator coordinator,
			final int id) {
		if (coordinator != null) {
			coordinator.monitorRemoved(id);
		}
	}

	
	private synchronized void request(final IMessage message) {
		proxy.request(message);
	}

	@SuppressWarnings("unchecked")
	private void response(final WorklistAssignmentCoordinator coordinator, int id,
			final String recommendation) {
		try {
			final ResponseSet<Recommendation<?>> result = (ResponseSet<Recommendation<?>>) converter
					.fromXML(recommendation);
			processResult(coordinator, id, result);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void state(final WorklistAssignmentCoordinator coordinator, final String state) {
		final XMLAssignmnetStateBroker broker = new XMLAssignmnetStateBroker(coordinator
				.getAssignmentState());
		broker.readDocumentString(state);
		broker.fromElement();
		stateChanged(coordinator);
	}

	/**
	 * 
	 * @param coordinator
	 *            AssignmentCoordinator
	 */
	private synchronized void stateChanged(final WorklistAssignmentCoordinator coordinator) {
		if (coordinator != null) {
			coordinator.stateChanged();
			changed(coordinator);
		}
	}

	@Override
	public void removeMonitor(Assignment assignment, int id) {
		final IMessage msg = MessageFactory.messageRemoveMonitor(assignment, id);
		request(msg);
	}
}
