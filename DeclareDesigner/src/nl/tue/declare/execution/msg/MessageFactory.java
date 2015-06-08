package nl.tue.declare.execution.msg;

import java.util.StringTokenizer;

import nl.tue.declare.domain.instance.Assignment;
import nl.tue.declare.domain.instance.Event;
import nl.tue.declare.execution.AssignmentState;
import nl.tue.declare.execution.Execution;
import nl.tue.declare.execution.WorkItem;
import nl.tue.declare.execution.msg.engine.msg.ChangeAssignmentMessage;
import nl.tue.declare.execution.msg.engine.msg.CloseAssignmentEngineMessage;
import nl.tue.declare.execution.msg.engine.msg.HistoryAssignmentMessage;
import nl.tue.declare.execution.msg.engine.msg.MonitorRemovedMessage;
import nl.tue.declare.execution.msg.engine.msg.OpenAssignmentMessage;
import nl.tue.declare.execution.msg.engine.msg.ResponseAssignmentMessage;
import nl.tue.declare.execution.msg.engine.msg.StateAssignmentMessage;
import nl.tue.declare.execution.msg.engine.response.EngineCloseAssignmentAccept;
import nl.tue.declare.execution.msg.engine.response.EngineCloseAssignmentReject;
import nl.tue.declare.execution.msg.engine.response.EngineEventAccept;
import nl.tue.declare.execution.msg.engine.response.EngineEventReject;
import nl.tue.declare.execution.msg.engine.response.EngineLogInAccept;
import nl.tue.declare.execution.msg.engine.response.EngineLogInReject;
import nl.tue.declare.execution.msg.worklist.ForwardEventMessage;
import nl.tue.declare.execution.msg.worklist.MessageCloseAssignment;
import nl.tue.declare.execution.msg.worklist.MessageLogIn;
import nl.tue.declare.execution.msg.worklist.MessageLogOut;
import nl.tue.declare.execution.msg.worklist.MonitorMessage;
import nl.tue.declare.execution.msg.worklist.RemoveMonitorMessage;
import nl.tue.declare.execution.msg.worklist.QueryMessage;
import nl.tue.declare.execution.msg.worklist.RequestEventMessage;
import nl.tue.declare.execution.msg.worklist.QueryMessage.QueryType;

import org.processmining.operationalsupport.messages.reply.ResponseSet;

import yawlservice.ExternalCase;

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
public class MessageFactory {

	/**
	 * 
	 * @param assignment
	 *            AssignmentState
	 * @param model
	 *            String
	 * @return StateAssignmentMessage
	 */
	public static IMessage createChangeAssignmentMassage(final Assignment assignment,
			final String model) {
		return new ChangeAssignmentMessage(assignment, model);
	}

	public static IMessage createHistoryAssignmentMassage(final Execution execution) {
		return new HistoryAssignmentMessage(execution.getAssignment(), execution);
	}

	/**
	 * 
	 * @param assignment
	 *            Assignment
	 * @param model
	 *            String
	 * @return OpenAssignmentMessage
	 */
	public static IMessage createOpenAssignementMessage(final Assignment assignment,
			final String model) {
		return new OpenAssignmentMessage(assignment, model);
	}

	/**
	 * 
	 * @param state
	 *            AssignmentState
	 * @return StateAssignmentMessage
	 */
	public static IMessage createStateAssignmentMassage( // Assignment assignment,
			final AssignmentState state) {
		final Assignment assignment = state.getAssignment();
		return new StateAssignmentMessage(assignment, state);
	}

	public static IMessage eventForward(final WorkItem wi, final Event event, final ExternalCase ec) {
		return new ForwardEventMessage(wi, event, ec);
	}

	public static IMessage eventRequest(final WorkItem wi, final Event event) {
		return new RequestEventMessage(wi, event);
	}

	/**
	 * 
	 * @param msg
	 *            String
	 * @return IMessage
	 */
	public static IMessage getEngineResponse(final String msg) {
		if (msg == null) return new UnknownMessage();
		final StringTokenizer st = new StringTokenizer(msg, IMessage.SEPARATOR);

		// the first token in every message is CLASS
		final String CLASS = st.nextToken();

		if (CLASS.equals(Ack.CLASS)) {
			return new Ack();
		} else if (CLASS.equals(UserMessage.CLASS)) { 
			// the second token in every message is TYPE
			final String TYPE = st.nextToken();
			if (TYPE.equals(EngineLogInAccept.TYPE)) { // TYPE == accepted
				final String id = st.nextToken();
				return logInAccept(id);
			} else if (TYPE.equals(EngineLogInReject.TYPE)) { // TYPE == rejected
				final String description = st.nextToken();
				return logInReject(description);
			}
		} else if (CLASS.equals(AssignmentMessage.CLASS)) { // // CLASS == AssignmentMessage
			// the second token in every message is TYPE
			final String TYPE = st.nextToken();
			final String caseId = st.nextToken();
			if (TYPE.equals(OpenAssignmentMessage.TYPE)) { // TYPE == OpenAssignment
				final String model = st.nextToken();
				return new OpenAssignmentMessage(caseId, model);
			} else if (TYPE.equals(CloseAssignmentEngineMessage.TYPE))
				return new CloseAssignmentEngineMessage(caseId);
			else if (TYPE.equals(StateAssignmentMessage.TYPE)) { // TYPE == StateAssignment
				final String state = st.nextToken();
				return new StateAssignmentMessage(caseId, state);
			} else if (TYPE.equals(ChangeAssignmentMessage.TYPE)) { // TYPE == ChangeAssignment
				final String state = st.nextToken();
				return new ChangeAssignmentMessage(caseId, state);
			} else if (TYPE.equals(ResponseAssignmentMessage.TYPE)) { // TYPE ==
				// RecommendationAssignment
				final String id = st.nextToken();
				final String recommendation = st.nextToken();
				return new ResponseAssignmentMessage(caseId, id, recommendation);
			} else if (TYPE.equals(MonitorRemovedMessage.TYPE)) { // TYPE ==
				final String id = st.nextToken();
				return new MonitorRemovedMessage(caseId, id);
			} else if (TYPE.equals(HistoryAssignmentMessage.TYPE)) { // TYPE == HistoryAssignment
				final String history = st.nextToken();
				return new HistoryAssignmentMessage(caseId, history);
			} else if (TYPE.equals(WorkItemMessage.TYPE)) { // TYPE = WorkItem EventAccept
				final String wiId = st.nextToken();
				final String actId = st.nextToken();
				final String type = st.nextToken();
				final String ACTION = st.nextToken();
				final String xml = st.nextToken();
				if (ACTION.equals(EngineEventAccept.ACTION))
					return new EngineEventAccept(caseId, actId, type, wiId, xml);
				else if (TYPE.equals(WorkItemMessage.TYPE)) { // TYPE = EventReject
					final String reason = st.hasMoreTokens() ? st.nextToken() : "";
					return new EngineEventReject(caseId, wiId, actId, type, xml, reason);
				}
			} else if (TYPE.equals(EngineCloseAssignmentAccept.TYPE))
				return new EngineCloseAssignmentAccept(caseId);
			else if (TYPE.equals(EngineCloseAssignmentReject.TYPE)) { // TYPE ==
				// CloseAssignmentAccept
				final String info = st.nextToken();
				return new EngineCloseAssignmentReject(caseId, info);
			}
		}
		return new UnknownMessage();
	}

	public static IMessage getWorklistRequest(final String msg) {
		if (msg == null) return new UnknownMessage();
		final StringTokenizer st = new StringTokenizer(msg, IMessage.SEPARATOR);

		// the first token in every message is CLASS
		final String CLASS = st.nextToken();

		if (CLASS.equals(Ack.CLASS)) { 
			return new Ack();
		} else
			if (CLASS.equals(UserMessage.CLASS)) {
			// the second token in a UserMessage is TYPE
			final String TYPE = st.nextToken();
			final String id = st.nextToken();
			final String userName = st.nextToken();
			final String password = st.nextToken();
			if (TYPE.equals(MessageLogIn.TYPE))
				return new MessageLogIn(id, userName, password);
			else if (TYPE.equals(MessageLogOut.TYPE))
				return new MessageLogOut(id, userName, password);
		} else if (CLASS.equals(AssignmentMessage.CLASS)) { // CLASS == ASSIGNMENT
			// the second token in a AssignmentMessage is TYPE
			final String TYPE = st.nextToken();
			// the third token in a AssignmentMessage is assignmentId
			final String assignmentId = st.nextToken();
			if (TYPE.equals(QueryMessage.TYPE)) { // close assignment
				final String type = st.nextToken();
				final String query = st.nextToken();
				return new QueryMessage(assignmentId, type, query);
			} else if (TYPE.equals(MonitorMessage.TYPE)) { // close assignment
				final String type = st.nextToken();
					final String query = st.nextToken();
					return new MonitorMessage(assignmentId, type, query);
			} else if (TYPE.equals(RemoveMonitorMessage.TYPE)) { // close assignment
				final String id = st.nextToken();
					return new RemoveMonitorMessage(assignmentId, id);
			} else if (TYPE.equals(MessageCloseAssignment.TYPE))
				return new MessageCloseAssignment(assignmentId);
			else if (TYPE.equals(WorkItemMessage.TYPE)) { // TYPE == WORKITEM
				final String itemId = st.nextToken(); // work item id
				final String activity = st.nextToken(); // take the activity ID
				final String event = st.nextToken(); // take the event type
				final String ACTION = st.nextToken();
				final String xml = st.nextToken(); // take xml
				if (ACTION.equals(RequestEventMessage.ACTION))
					return new RequestEventMessage(assignmentId, activity, event, itemId, xml);
				else {
					if (ACTION.equals(ForwardEventMessage.ACTION)) {
						final String ec = st.nextToken();
						return new ForwardEventMessage(assignmentId, activity, event, itemId, xml,
								ec);
					}
				}
			}
		}
		return new UnknownMessage();
	}

	public static IMessage logInAccept(final String id) {
		return new EngineLogInAccept(id);
	}

	public static IMessage logInReject(final String reason) {
		return new EngineLogInReject(reason);
	}

	public static IMessage logOut(final String id, final String user, final String pass) {
		return new MessageLogOut(id, user, pass);
	}

	public static IMessage messageCloseAssignment(final Assignment assignemnt) {
		return new MessageCloseAssignment(assignemnt);
	}

	public static IMessage messageCloseAssignmentEngineMessage(final Assignment assignment) {
		return new CloseAssignmentEngineMessage(assignment);
	}

	public static IMessage messageQuery(final Assignment assignment, final QueryType type,
			final String query) {
		return new QueryMessage(assignment, type, query);
	}
	
	public static IMessage messageMonitor(final Assignment assignment, final QueryType type, final String query) {
		return new MonitorMessage(assignment, type, query);
	}

	public static IMessage messageResponseAssignment(final Assignment assignment,
			int id, final ResponseSet<?> result) {
		return new ResponseAssignmentMessage(assignment, id, result);
	}

	public static IMessage messageRemoveMonitor(Assignment assignment, int id) {
		return new RemoveMonitorMessage(assignment, id);
	}

	public static IMessage messageMonitorRemoved(final Assignment assignment,
			int id) {
		return new MonitorRemovedMessage(assignment, id);
	}
}
