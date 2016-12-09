package nl.tue.declare.logging;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nl.tue.declare.domain.instance.Activity;
import nl.tue.declare.domain.instance.Assignment;
import nl.tue.declare.domain.instance.DataField;
import nl.tue.declare.domain.instance.Event;
import nl.tue.declare.domain.model.ActivityDataDefinition;
import nl.tue.declare.domain.model.ActivityDefinition;
import nl.tue.declare.domain.model.AssignmentModel;
import nl.tue.declare.domain.model.DataElement;
import nl.tue.declare.domain.organization.User;
import nl.tue.declare.utils.prom.ProM;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XLifecycleExtension;
import org.deckfour.xes.extension.std.XOrganizationalExtension;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.buffered.XAttributeMapBufferedImpl;
import org.deckfour.xes.model.impl.XAttributeBooleanImpl;
import org.deckfour.xes.model.impl.XAttributeContinuousImpl;
import org.deckfour.xes.model.impl.XAttributeDiscreteImpl;
import org.deckfour.xes.model.impl.XAttributeLiteralImpl;

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
class ModelLogWriter {

	/*
	 * private LogCollection col; private LogSetRandom set;
	 */ 
	private LogsRepository repository;
	 
    private XFactory factory;
    private XConceptExtension concept;
	private XLog log;	
	//private String model;
	private HashMap<Assignment, XTrace> traces;

	/**
	 * 
	 * @param out
	 *            OutputStream
	 * @param assignment
	 *            Assignment
	 */
	ModelLogWriter(/*OutputStream out,*/ final AssignmentModel assignment) {
		super();
		factory = XFactoryRegistry.instance().currentDefault();
		concept = XConceptExtension.instance();
		log = factory.createLog();
		traces = new HashMap<Assignment, XTrace>();
		concept.assignName(log, assignment.getName());
		repository = new LogsRepository();
		/*
		 * col = LogCollection.createLogCollection(new LogPersistencyStream(out,
		 * false)); try { set = col.accessRandom(); } catch (LogException ex) {
		 * set = null; ex.printStackTrace(); } 
		 */
		//this.model = assignment.getName();
	}

	/*boolean forAssignment(Assignment assignment) {
		return model.equals(assignment.getName());
	}*/

	/**
	 * 
	 * @param assignment
	 *            Assignment
	 * @param event
	 *            AbstractEvent
	 */
	public void add(Assignment assignment, Event event) {
		if (assignment != null && event != null) {
			XEvent ate = getEntry(event);
			add(assignment, ate);
		}
	}

	private void add(Assignment assignment, XEvent ate) {
		if (assignment != null && ate != null ) {
			//if (assignment.getName().equals(model)) {
					String piid = Integer.toString(assignment.getId());
					XTrace trace = traces.get(assignment);
					if (trace == null){
						  // create new trace
						trace = factory.createTrace();
						  // add instance ID to the trace						
						concept.assignName(trace, piid);
						
						  // add the trace to the traces map and log
						traces.put(assignment, trace);
						log.add(trace);
						
					}
					trace.add(ate);
					repository.get(assignment).add(ate);
			//}
		}
		//repository.get(assignment).add(ate);
	}

	public AssignmentLog getAssignmnetLog(Assignment assignment) {
		AssignmentLog log = null;
		if (assignment != null) {
			log = repository.get(assignment);
		}
		return log;
	}

 /**
  * Writes the Log to a file
  */
/*	public void finish() {
		try {
			//set.finish();
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
	}*/

	/**
	 * 
	 * @param wiEvent
	 *            AbstractEvent
	 * @return AuditTrailEntry
	 */
	private XEvent getEntry(Event event) {
		 // first get the task name, event type and originator
		String task = getWFMElement(event.getActivity()); // task
		XLifecycleExtension.StandardModel type = getType(event); // event type
		String originator = getOriginator(event.getUser()); // originator
		String workItemId = event.getAttributes().get(Event.WORKITEM_ID);
		  // create the OpenXES event
		XEvent ate = createEvent(task, type, System.currentTimeMillis(), originator, workItemId);

		 // create attributes for the OpenXES event
		
		XAttributeMap attributeMap = new XAttributeMapBufferedImpl();
		
		// add event-related attributes (e.g., workitem ID, etc...)
		eventAttributes(attributeMap, event);
        
		// add event-related attributes (e.g., workitem ID, etc...)
		if (event.getActivity() instanceof Activity) {
			dataAttributes(attributeMap, (Activity) event.getActivity()); 
		}
		  // add attributes to the OpenXES event
		ate.getAttributes().putAll(attributeMap);
		return ate;
	}
	
	private XEvent createEvent(String task, XLifecycleExtension.StandardModel type, long time, String originator, String workItemId){
		  // create the OpenXES event
		XEvent ate = XFactoryRegistry.instance().currentDefault().createEvent();
		XTimeExtension.instance().assignTimestamp(ate, time);
		XLifecycleExtension.instance().assignStandardTransition(ate, type);
		XConceptExtension.instance().assignName(ate, task);
		XOrganizationalExtension.instance().assignResource(ate, originator);
		if (workItemId != null) XConceptExtension.instance().assignInstance(ate, workItemId);
		return ate;
	}

	/**
	 * Returns an XAttribute of the provided type with the given key and value
	 * 
	 * @param dataType
	 *            Type of the attribute to return
	 * @param dataName
	 *            Key of the attribute to return
	 * @param dataValue
	 *            Value of the attribute to return
	 * @return XAttribute
	 */
	private XAttribute dataElementToXAttribute(DataElement.Type dataType,
			String dataName, String dataValue) {
		// First, test if we can find the given type
		if (dataType == DataElement.Type.BOOLEAN) {
			return new XAttributeBooleanImpl(dataName, Boolean
					.parseBoolean(dataValue));
		} else if (dataType == DataElement.Type.DOUBLE) {
			return new XAttributeContinuousImpl(dataName, Double
					.parseDouble(dataValue));
		} else if (dataType == DataElement.Type.INTEGER) {
			return new XAttributeDiscreteImpl(dataName, Long
					.parseLong(dataValue));
		}
		// We must have a default, literals/strings are a good candidate
		// But we must prevent empty strings
		if (dataValue.trim().length() == 0)
			dataValue = "UNDEFINED";
		return new XAttributeLiteralImpl(dataName, dataValue);
	}

	/**
	 * 
	 * @param activity
	 *            AbstractEvent
	 * @param user
	 *            User
	 * @param type
	 *            EventType
	 * @return AuditTrailEntry
	 */
/*	private XEvent getEntry(ActivityDefinition activity, User user,
			EventType type) {
		HashMap<String, String> attributes = new HashMap<String, String>(); // attributes
																			// empty
		String wfme = getWFMElement(activity); // workflow model element
		Timestamp time = new Timestamp(System.currentTimeMillis()); // timestamp
		String originator = this.getOriginator(user); // originato

		return  new AuditTrailEntry(attributes, wfme, type, time, originator);
	}*/

	/**
	 * 
	 * @param attributes
	 *            HashMap
	 * @param activity
	 *            Activity
	 */
	private void dataAttributes(XAttributeMap attributeMap, Activity activity) {
		if (activity != null) {
			Iterator<ActivityDataDefinition> iterator = activity.data();
			while (iterator.hasNext()) {
				DataElement data = iterator.next().getDataElement();
				if (data instanceof DataField) {
					DataField field = (DataField) data;
					XAttribute attribute = dataElementToXAttribute(field
							.getType(), field.getName(), field.getValue());
					attributeMap.put(data.getName(), attribute);
				}
			}
		}
	}

	/**
	 * 
	 * @param attributes
	 *            HashMap
	 * @param activity
	 *            Activity
	 */
	private void eventAttributes(XAttributeMap attributeMap, Event event) {
		if (event != null) {
			for (Map.Entry<String, String> entry : event.getAttributes()
					.entrySet()) {
				if (!Event.WORKITEM_ID.equals(entry.getKey()))  {
				XAttribute attribute = new XAttributeLiteralImpl(
						entry.getKey(), entry.getValue());
				attributeMap.put(entry.getKey(), attribute);
				}
			}
		}
	}

	/**
	 * 
	 * @param event
	 *            AbstractEvent
	 * @return EventType
	 */
	private XLifecycleExtension.StandardModel getType(Event event) {
		return ProM.getType(event);
	}

	/**
	 * 
	 * @param user
	 *            AbstractEvent
	 * @return String
	 */
	private String getOriginator(User user) {
		String originator = "unknown";
		if (user != null) {
			originator = user.getUserName();
		}
		return originator;
	}

	/**
	 * 
	 * @param activity
	 *            AbstractEvent
	 * @return String
	 */
	private String getWFMElement(ActivityDefinition activity) {
		String wfme = "unknown";
		if (activity != null) {
			wfme = activity.getName();
		}
		return wfme;
	}

	/**
	 * 
	 * @param assignment
	 *            Assignment
	 * @param activity
	 *            Activity
	 */
	public void schedule(Assignment assignment, ActivityDefinition activity) {
		/*
		 * AT THE MOMENT SCHEDULING IS DISABLED - DO NOT DELETE THIS CODE !!!!
		 * 
		 * if (set != null && assignment != null && activity != null) {
		 * AuditTrailEntry ate = this.getEntry(activity, null,
		 * EventType.SCHEDULE); add(assignment, ate); }
		 */
	}

	/**
	 * 
	 * @param assignment
	 *            Assignment
	 * @param activity
	 *            Activity
	 * @param user
	 *            User
	 */
	public void assign(Assignment assignment, ActivityDefinition activity,
			User user) {
		if (assignment != null && activity != null
				&& user != null) {
			XEvent ate = createEvent(getWFMElement(activity),XLifecycleExtension.StandardModel.ASSIGN, System.currentTimeMillis(),
					getOriginator(user), assignment.getAttributes().get(Event.WORKITEM_ID));
			add(assignment, ate);
		}
	}
	
   XLog getLog(){
	   return log;
   }
	
 

	/**
	 * 
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
	class LogsRepository {
		HashMap<Assignment, AssignmentLog> repository;


		public LogsRepository() {
			super();
			repository = new HashMap<Assignment, AssignmentLog>();
		}

		AssignmentLog get(Assignment assignment) {
			AssignmentLog log = this.repository.get(assignment); // get log for
																	// the
																	// assignment
			if (log == null) { // if des not exist add new
				log = new AssignmentLog(assignment);
				this.repository.put(assignment, log);
			}
			return log;
		}
	}
}
