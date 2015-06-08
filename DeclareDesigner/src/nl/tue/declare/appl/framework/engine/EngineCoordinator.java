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
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import nl.tue.declare.appl.framework.FrameworkCoordinator;
import nl.tue.declare.control.Control;
import nl.tue.declare.datamanagement.XMLWorkItemBroker;
import nl.tue.declare.domain.instance.Activity;
import nl.tue.declare.domain.instance.Assignment;
import nl.tue.declare.domain.instance.Event;
import nl.tue.declare.domain.organization.User;
import nl.tue.declare.execution.WorkItem;
import nl.tue.declare.execution.msg.ApprovedRequest;
import nl.tue.declare.execution.msg.AssignmentMessage;
import nl.tue.declare.execution.msg.IMessage;
import nl.tue.declare.execution.msg.RejectedRequest;
import nl.tue.declare.execution.msg.UnknownRequest;
import nl.tue.declare.execution.msg.WorkItemMessage;
import nl.tue.declare.execution.msg.engine.response.EngineCloseAssignmentAccept;
import nl.tue.declare.execution.msg.engine.response.EngineCloseAssignmentReject;
import nl.tue.declare.execution.msg.engine.response.EngineEventAccept;
import nl.tue.declare.execution.msg.engine.response.EngineEventReject;
import nl.tue.declare.execution.msg.engine.response.EngineLogInAccept;
import nl.tue.declare.execution.msg.engine.response.EngineLogInReject;
import nl.tue.declare.execution.msg.worklist.AbstractLoggingMessage;
import nl.tue.declare.execution.msg.worklist.ForwardEventMessage;
import nl.tue.declare.execution.msg.worklist.MessageCloseAssignment;
import nl.tue.declare.execution.msg.worklist.MessageLogIn;
import nl.tue.declare.execution.msg.worklist.MessageLogOut;
import nl.tue.declare.execution.msg.worklist.MonitorMessage;
import nl.tue.declare.execution.msg.worklist.QueryMessage;
import nl.tue.declare.execution.msg.worklist.RemoveMonitorMessage;
import nl.tue.declare.execution.msg.worklist.RequestEventMessage;
import nl.tue.declare.execution.msg.worklist.QueryMessage.QueryType;
import nl.tue.declare.graph.model.AssignmentModelView;
import nl.tue.declare.logging.ProcessLogWriter;
import nl.tue.declare.utils.sockets.SocketConnection;
import yawlservice.ExternalCase;

public class EngineCoordinator implements IRemoteWorklistListener, IRequestListener {

	/**
	 * 
	 * @return EngineCoordinator
	 */
	public static EngineCoordinator singleton() {
		if (instance == null) {
			instance = new EngineCoordinator();
		}
		return instance;
	}

	private final EngineGate requestGate; // for request-response communication with worklists, eg.
	// WL_request_event -> ENG_response_accept_or_reject

	private final EngineGate monitorGate; // for sending information to worklists, eg.,
	// ENG_change_state is sent to worklists without a
	// request
	protected List<RemoteWorklist> worklists;
	private final List<EngineAssignmentCoordinator> assignments;

	protected static EngineCoordinator instance = null;
	private final int portRecommendation;
	private String hostRecommendation;

	/**
   *
   */
	protected EngineCoordinator() {
		super();
		assignments = Collections.synchronizedList(new ArrayList<EngineAssignmentCoordinator>());
		worklists = Collections.synchronizedList(new ArrayList<RemoteWorklist>());

		final EngineInitializer ini = EngineInitializer.singleton(); // load settings for ini file
		final int portInfo = ini.getPortInformation(); // read engine information port from ini file
		final int portReq = ini.getPortRequest(); // read engine request port from ini file

		portRecommendation = ini.getPortRecommendation(); // read recommendation proxy port from ini
		// file
		hostRecommendation = ini.getHostRecommendation(); // read recommendation proxy host from ini
		// file
		if (hostRecommendation == null) {
			hostRecommendation = SocketConnection.LOCAL_HOST;
		}

		requestGate = new EngineGate() { // create request gate
			@Override
			public void connected(final IWorklistRemoteProxy proxy) {
				proxy.setRequestListener(EngineCoordinator.this);
			}

			@Override
			protected IWorklistRemoteProxy createProxy(final Socket connection) {
				return new ResponseWorklistProxy(connection);
			}
		};
		if (portReq > 0) { // if loaded
			requestGate.setPort(portReq); // set port
		}

		monitorGate = new EngineGate() { // create monitor gate
			@Override
			public void connected(final IWorklistRemoteProxy proxy) {
				proxy.setRequestListener(EngineCoordinator.this);
			}

			@Override
			protected IWorklistRemoteProxy createProxy(final Socket connection) {
				return new InformationWorklistProxy(connection);
			}
		};
		if (portInfo > 0) { // if loaded
			monitorGate.setPort(portInfo); // set port
		}
	}

	/**
	 * 
	 * @param assignmentCoordinator
	 *            AssignmentCoordinator
	 * @param scheduledAt
	 *            Double
	 */
	public synchronized void add(final EngineAssignmentCoordinator assignmentCoordinator) {
		if (assignmentCoordinator != null) {
			assignmentCoordinator.setRecommendationProvider(hostRecommendation, portRecommendation);
			synchronized (assignments) {
				assignments.add(assignmentCoordinator);
			}
			initiateAssignment(assignmentCoordinator);
		}
	}

	/**
	 * 
	 * @param assignment
	 *            String
	 * @throws Exception
	 * @return boolean
	 */
	public boolean canComplete(final Assignment assignment) throws Exception {
		final EngineAssignmentCoordinator coordinator = this.getAssignmentCoordinator(assignment);
		boolean ok = false;
		if (coordinator != null) {
			ok = coordinator.canComplete();
		}
		return ok;
	}

	public synchronized void change(final Assignment original, final Assignment change,
			final AssignmentModelView view) throws Throwable {
		final EngineAssignmentCoordinator coordinator = getAssignmentCoordinator(original);
		if (coordinator != null) {
			coordinator.change(change, view);
			coordinator.reInitiate();
			initiateAssignment(coordinator);
		}
	}

	public synchronized void change(final EngineAssignmentCoordinator coordinator,
			final Assignment change, final AssignmentModelView view) throws Throwable {
		if (coordinator != null) {
			coordinator.change(change, view);
			coordinator.reInitiate();
			initiateAssignment(coordinator);
		}
	}

	/**
	 * close
	 */
	public void close() {
		ProcessLogWriter.singleton().finish();
	}

	/**
	 * 
	 * @param assignment
	 *            assignment that sould be closed
	 * @param secure
	 *            true close iff assignment in state to be closed; false close without checking the
	 *            state of assinment
	 * @throws Exception
	 * @return boolean
	 */
	public synchronized boolean complete(final Assignment assignment, final boolean secure) {
		boolean ok = false;
		final EngineAssignmentCoordinator coordinator = this.getAssignmentCoordinator(assignment);
		if (coordinator != null) {
			ok = coordinator.closeAssignment(secure);
			if (ok) {
				synchronized (assignments) {
					assignments.remove(coordinator);
				}
			}
		}
		return ok;
	}

	/**
	 * 
	 * @param id
	 *            int
	 * @param user
	 *            User
	 * @param proxy
	 *            IWorklistRemoteProxy
	 * @return int
	 */
	public int connected(final int id, final User user, final IWorklistRemoteProxy proxy) {
		int newId = 0;
		if (proxy != null) {
			final RemoteWorklist worklist = getWorklist(id, user);
			if (proxy instanceof ResponseWorklistProxy) {
				worklist.addResponseChannel((ResponseWorklistProxy) proxy);
			}
			if (proxy instanceof InformationWorklistProxy) {
				worklist.addInformationChannel((InformationWorklistProxy) proxy);
			}
			if (worklist.connected()) {
				initiateWorklist(worklist);
			}
			newId = worklist.getId();
		}
		return newId;
	}

	/**
	 * 
	 * @param worklist
	 *            RemoteWorklist
	 */
	public void disconnected(final RemoteWorklist worklist) {
		synchronized (worklists) {
			worklists.remove(worklist);
		}

		synchronized (assignments) {
			for (int i = 0; i < assignments.size(); i++) {
				final EngineAssignmentCoordinator assignmentCoordinator = assignments.get(i);
				assignmentCoordinator.removeWorkList(worklist);
			}
		}
	}

	/**
	 * 
	 * @param item
	 *            WorkItem
	 * @param event
	 *            String
	 * @return WorkItemEvent
	 */
	public void eventRequest(final WorkItem item, final Event event) {
		final EngineAssignmentCoordinator coordinator = this.getAssignmentCoordinator(Integer
				.toString(item.getActivity().getAssignment().getId()));
		if (coordinator != null) {
			coordinator.event(item, event);
		}
	}

	/**
	 * response
	 * 
	 * @return List
	 * @todo Implement this nl.tue.declare.execution.msg.engine.handle.IEngineMessageHandler method
	 */
	public IMessage forwardEventMessage(final ForwardEventMessage msg,
			final EngineAssignmentCoordinator coordinator, final User user) {
		final WorkItem item = createItem(coordinator.getAssignment(), user, msg);
		final Event event = new Event(item.getUser(), item.getActivity(), Event.Type.valueOf(msg
				.getEventName()));
		final ExternalCase ec = new ExternalCase();
		ec.fromString(msg.getExternalCase());
		try {
			if (item != null && event != null && coordinator != null) {
				coordinator.event(item, event);
				if (handleForward(item, ec)) // add here my attempt to forward to YAWL
					return new EngineEventAccept(item, event);
				else
					return new EngineEventReject(item, event, "");
			} else
				return new UnknownRequest();
		} catch (final Throwable t) {
			return new EngineEventReject(item, event, "");
		}
	}

	/**
	 * 
	 * @param assignment
	 *            String
	 * @return AssignmentCoordinator
	 */
	public EngineAssignmentCoordinator getAssignmentCoordinator(final Assignment assignment) {
		boolean found = false;
		int i = 0;
		EngineAssignmentCoordinator coordinator = null;
		synchronized (assignments) {
			while (!found && i < assignments.size()) {
				coordinator = assignments.get(i++);
				found = coordinator.getAssignment() == assignment;
			}
		}
		return found ? coordinator : null;
	}

	/**
	 * 
	 * @param id
	 *            String
	 * @return AssignmentCoordinator
	 */
	public EngineAssignmentCoordinator getAssignmentCoordinator(final String id) {
		boolean found = false;
		int i = 0;
		EngineAssignmentCoordinator coordinator = null;
		synchronized (assignments) {
			while (!found && i < assignments.size()) {
				coordinator = assignments.get(i++);
				found = coordinator.forAssignment(id);
			}
		}
		return found ? coordinator : null;
	}

	public Collection<EngineAssignmentCoordinator> getInstances(final EngineAssignmentCoordinator ac) {
		final Collection<EngineAssignmentCoordinator> instances = new ArrayList<EngineAssignmentCoordinator>();
		synchronized (assignments) {
			for (int i = 0; i < assignments.size(); i++) {
				final EngineAssignmentCoordinator curr = assignments.get(i);
				if (curr.sameModel(ac)) {
					instances.add(curr);
				}
			}
		}
		return instances;
	}

	public void initiate() throws Exception {
		requestGate.init();
		monitorGate.init();
	}

	public boolean logout(final int id, final User user) {
		final boolean ok = true;
		final RemoteWorklist worklist = getWorklist(id, user);
		if (worklist != null) {
			disconnected(worklist);
		}
		return ok;
	}

	public IMessage request(final IMessage msg, final IWorklistRemoteProxy proxy) {
		if (msg == null) return new UnknownRequest();
		if (msg instanceof AbstractLoggingMessage) { // USER
			if (msg instanceof MessageLogIn)
				return logInMessage((AbstractLoggingMessage) msg, proxy);
			else if (msg instanceof MessageLogOut)
				return logOutMesage((AbstractLoggingMessage) msg);
		} else if (msg instanceof AssignmentMessage) { // ASSIGNMENT
			final EngineAssignmentCoordinator coordinator = this
					.getAssignmentCoordinator(((AssignmentMessage) msg).id());
			if (msg instanceof MessageCloseAssignment)
				return closeMessage(coordinator);
			else if (msg instanceof QueryMessage) {
				final QueryMessage queryMessage = (QueryMessage) msg;
				return query(coordinator, queryMessage.getType(), queryMessage.getQuery());
			} else if (msg instanceof MonitorMessage) {
				final MonitorMessage queryMessage = (MonitorMessage) msg;
				return monitor(coordinator, queryMessage.getType(), queryMessage.getQuery());
			} else if (msg instanceof RemoveMonitorMessage) {
				final RemoveMonitorMessage queryMessage = (RemoveMonitorMessage) msg;
				return removeMonitor(coordinator, queryMessage.getId());
			} else if (msg instanceof WorkItemMessage) { // WORK ITEM
				final WorkItemMessage wimsg = (WorkItemMessage) msg;
				if (wimsg instanceof RequestEventMessage)
					return requestEventMessage(wimsg, coordinator, proxy.getUser());
				else if (wimsg instanceof ForwardEventMessage)
					return forwardEventMessage((ForwardEventMessage) wimsg, coordinator, proxy
							.getUser());
			}
		}

		return new UnknownRequest();
	}

	/**
	 * response
	 * 
	 * @return List
	 * @todo Implement this nl.tue.declare.execution.msg.engine.handle.IEngineMessageHandler method
	 */
	public IMessage requestEventMessage(final WorkItemMessage msg,
			final EngineAssignmentCoordinator coordinator, final User user) {
		final WorkItem item = createItem(coordinator.getAssignment(), user, msg);
		final Event event = new Event(item.getUser(), item.getActivity(), Event.Type.valueOf(msg
				.getEventName()));
		try {
			if (item != null && event != null && coordinator != null) {
				final boolean ok = coordinator.event(item, event);
				if (ok)
					return new EngineEventAccept(item, event);
				else
					return new EngineEventReject(item, event, "");
			} else
				return new UnknownRequest();
		} catch (final Throwable t) {
			t.printStackTrace();
			return new EngineEventReject(item, event, "");
		}
	}

	/**
	 * 
	 * @param assignmentCoordinator
	 *            AssignmentCoordinator
	 */
	protected void initiateAssignment(final EngineAssignmentCoordinator assignmentCoordinator) {
		synchronized (worklists) {
			for (int i = 0; i < worklists.size(); i++) {
				final RemoteWorklist worklist = worklists.get(i);
				match(assignmentCoordinator, worklist);
			}
		}
	}

	/**
	 * 
	 * @param worklist
	 *            RemoteWorklist
	 */
	protected void initiateWorklist(final RemoteWorklist worklist) {
		synchronized (assignments) {
			for (int i = 0; i < assignments.size(); i++) {
				final EngineAssignmentCoordinator assignmentCoordinator = assignments.get(i);
				match(assignmentCoordinator, worklist);
			}
		}
	}

	/**
	 * 
	 * @param message
	 *            AssignmentCoordinator
	 */
	protected void sendToAll(final IMessage message) {
		synchronized (worklists) {
			for (int i = 0; i < worklists.size(); i++) {
				final RemoteWorklist worklist = worklists.get(i);
				worklist.sendInformation(message);
			}
		}
	}

	/* public void change(AssignmenCoordinator coordinator){
	   coordinator.reInitiate();
	   initiateAssignment(coordinator);
	 }*/

	/**
	 * response
	 * 
	 * @return List
	 * @todo Implement this nl.tue.declare.execution.msg.engine.handle.IEngineMessageHandler method
	 */
	private IMessage closeMessage(final EngineAssignmentCoordinator cooordinator) {
		final Assignment assignment = cooordinator.getAssignment();
		String reason = "";
		if (assignment != null) {
			boolean ok = false;
			try {
				ok = FrameworkCoordinator.singleton().complete(assignment);
			} catch (final Exception ex) {
				reason = ex.getMessage();
			}
			if (ok)
				return new EngineCloseAssignmentAccept(assignment);
			else
				return new EngineCloseAssignmentReject(assignment,
						"Assignment cannot be closed at this moment. " + reason);
		} else
			return new UnknownRequest();
	}

	/**
	 * 
	 * @param activityID
	 *            String
	 * @param itemID
	 *            String
	 * @param xml
	 *            String
	 * @return WorkItem
	 */
	private WorkItem createItem(final Assignment assignment, final User user,
			final WorkItemMessage msg) {
		WorkItem workItem = null;
		if (assignment != null) {
			final Activity activity = (Activity) assignment.activityDefinitionWithId(Integer
					.parseInt(msg.getActivityId()));
			final int id = Integer.parseInt(msg.getItemId());
			workItem = new WorkItem(id, activity, user);

			final XMLWorkItemBroker broker = new XMLWorkItemBroker(workItem);
			broker.readDocumentString(msg.getXML());
			broker.fromElement();
		}
		return workItem;
	}

	private RemoteWorklist getWorklist(final int id, final User user) {
		RemoteWorklist worklist = null;
		synchronized (worklists) {
			final Iterator<RemoteWorklist> iterator = worklists.iterator();
			boolean found = false;
			while (iterator.hasNext() && !found) {
				worklist = iterator.next();
				found = worklist.getId() == id && worklist.forUser(user);
			}
			if (!found) {
				worklist = new RemoteWorklist(nextWorklistId(), user, this);
				synchronized (worklists) {
					worklists.add(worklist);
				}
			}
		}
		return worklist;
	}

	private boolean handleForward(final WorkItem item, final ExternalCase ec) {
		return FrameworkCoordinator.singleton().forwardWorkItemToYawl(item, ec);
	}

	private IMessage logInMessage(final AbstractLoggingMessage msg, final IWorklistRemoteProxy proxy) {
		IMessage response = null;
		final User user = Control.singleton().getOrganization().getUserWithUserName(
				msg.getUsername());
		if (user == null) {
			response = new EngineLogInReject("Username " + msg.getUsername() + "does not exist.");
		} else if (!user.checkPasswod(msg.getPassword())) {
			response = new EngineLogInReject("Password is not correct.");
		} else {
			final int id = connected(Integer.parseInt(msg.getId()), user, proxy);
			// response = new EngineLogInAccept(Integer.toString(id));
			response = new EngineLogInAccept(Integer.toString(id));
		}
		return response;
	}

	private IMessage logOutMesage(final AbstractLoggingMessage msg) {
		IMessage response = null;
		final User user = Control.singleton().getOrganization().getUserWithUserName(
				msg.getUsername());
		if (user != null) {
			if (logout(Integer.parseInt(msg.getId()), user)) {
				response = new ApprovedRequest();
			} else {
				response = new RejectedRequest();
			}
		}
		if (response == null) {
			response = new UnknownRequest();
		}
		return response;
	}

	/**
	 * 
	 * @param coordinator
	 *            AssignmentCoordinator
	 * @param worklist
	 *            RemoteWorklist
	 * @return boolean
	 */
	private boolean match(final EngineAssignmentCoordinator coordinator,
			final RemoteWorklist worklist) {
		boolean ok = false;
		if (coordinator != null && worklist != null) {
			ok = coordinator.match(worklist);
			if (ok) {
				coordinator.addWorklist(worklist);
			}
		}
		return ok;
	}

	private IMessage monitor(final EngineAssignmentCoordinator coordinator, final QueryType type,
			final String query) {
		return coordinator.monitor(type, query);
	}

	private int nextWorklistId() {
		int id = 1;
		synchronized (worklists) {
			if (worklists.size() > 0) {
				id = worklists.get(worklists.size() - 1).getId() + 1;
			}
		}
		return id;
	}

	private IMessage query(final EngineAssignmentCoordinator coordinator, final QueryType type,
			final String query) {
		return coordinator.query(type, -1, query);
	}

	private IMessage removeMonitor(final EngineAssignmentCoordinator coordinator, final int id) {
		return coordinator.removeMonitor(id);
	}
}
