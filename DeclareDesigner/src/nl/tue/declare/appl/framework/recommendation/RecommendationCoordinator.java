package nl.tue.declare.appl.framework.recommendation;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import nl.tue.declare.domain.instance.Assignment;
import nl.tue.declare.domain.organization.User;
import nl.tue.declare.execution.AssignmentState;
import nl.tue.declare.logging.AssignmentLog;
import nl.tue.declare.logging.ProcessLogWriter;
import nl.tue.declare.utils.prom.OperationalSupportUtils;
import nl.tue.declare.utils.prom.ProM;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XLifecycleExtension;
import org.deckfour.xes.extension.std.XOrganizationalExtension;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.operationalsupport.client.InvocationException;
import org.processmining.operationalsupport.client.Language;
import org.processmining.operationalsupport.client.SessionHandle;
import org.processmining.operationalsupport.client.language.XQueryLanguage;
import org.processmining.operationalsupport.messages.reply.Prediction;
import org.processmining.operationalsupport.messages.reply.Recommendation;
import org.processmining.operationalsupport.messages.reply.ResponseSet;
import org.processmining.operationalsupport.messages.reply.ResponseSet.Failure;
import org.w3c.dom.Document;

/**
 * @author michael
 */
public class RecommendationCoordinator {

	private final SessionHandle<Object, Document, String> handle;
	private final AssignmentState state;
	private final Set<User> availableUsers;

	private int skipEvents = 0;

	/**
	 * @param state
	 * @param host
	 * @param port
	 * @throws UnknownHostException
	 */
	public RecommendationCoordinator(final AssignmentState state, final String host, final int port)
			throws UnknownHostException {
		super();
		this.state = state;
		handle = SessionHandle.create(host, port, new XQueryLanguage());
		handle.setModel(Language.create(String.class, "test/test"), "hello");
		availableUsers = Collections.synchronizedSet(new HashSet<User>());
	}

	/**
	 * @param user
	 */
	public void addUser(final User user) {
		availableUsers.add(user);
	}

	/**
	 * @param query
	 * @return
	 */
	public synchronized ResponseSet<Prediction<Document>> compare(final String query) {
		try {
			return handle.compare(query, getAvailableEvents());
		} catch (final Exception e) {
			return new ResponseSet<Prediction<Document>>(Collections.<String, Collection<Prediction<Document>>> emptyMap(), Collections.<String, Collection<Failure>> emptyMap());
		}
	}

	/**
	 * @return
	 */
	public SessionHandle<Object, Document, String> getRecommendationProvider() {
		return handle;
	}

	/**
	 * @param query
	 * @return
	 */
	public synchronized ResponseSet<Prediction<Document>> predict(final String query) {
		try {
			return handle.predict(query, getAvailableEvents());
		} catch (final Exception e) {
			return new ResponseSet<Prediction<Document>>(Collections.<String, Collection<Prediction<Document>>> emptyMap(), Collections.<String, Collection<Failure>> emptyMap());
		}
	}

	/**
	 * @param query
	 * @return
	 */
	public synchronized ResponseSet<Recommendation<Document>> recommend(final String query) {
		try {
			return handle.recommend(query, getAvailableEvents());
		} catch (final Exception e) {
			return new ResponseSet<Recommendation<Document>>(Collections.<String, Collection<Recommendation<Document>>> emptyMap(), Collections.<String, Collection<Failure>> emptyMap());
		}
	}

	/**
	 * @param user
	 */
	public void removeUser(final User user) {
		availableUsers.remove(user);
	}

	public synchronized ResponseSet<Document> simple(final String query) {
		try {
			ResponseSet<Document> simple = handle.simple(query, getAvailableEvents());
			return simple;
		} catch (final Exception e) {
			return new ResponseSet<Document>(Collections.<String, Collection<Document>> emptyMap(), Collections.<String, Collection<Failure>> emptyMap());
		}
	}

	/**
	 * @return RecommendationResult
	 */
	private XLog getAvailableEvents() {
		final XLog available = XFactoryRegistry.instance().currentDefault().createLog();
		final Assignment assignment = state.getAssignment();
		// id
		for (final AssignmentState.EventState eventState : state.getPossibleEvents()) {
			synchronized (availableUsers) {
				for (final User user : availableUsers) { // available resources
					final XEvent event = XFactoryRegistry.instance().currentDefault().createEvent();
					XConceptExtension.instance().assignName(event,
							eventState.event().getActivity().getName());
					XLifecycleExtension.instance().assignStandardTransition(event,
							ProM.getType(eventState.event()));
					XOrganizationalExtension.instance().assignResource(event,
							OperationalSupportUtils.getUser(user));
					final XTrace trace = XFactoryRegistry.instance().currentDefault().createTrace();
					trace.add(event);
					available.add(trace);
				}
			}
		}

		// add incomplete log
		final AssignmentLog log = ProcessLogWriter.singleton().getAssignmentLog(assignment);
		int eventCounter = 0;
		for (final XEvent event : log.log()) {
			if (eventCounter++ >= skipEvents) {
				skipEvents = eventCounter;
				try {
					handle.addEvent(event);
				} catch (final InvocationException e) {
				}
			}
			eventCounter++;
		}
		return available;
	}

	public void done() {
		try {
			handle.close();
		} catch (Exception e) {
			// Ignore
		}
	}
}
