package nl.tue.declare.execution.msg.worklist;

import nl.tue.declare.domain.instance.Assignment;
import nl.tue.declare.execution.msg.AssignmentMessage;
import nl.tue.declare.execution.msg.worklist.QueryMessage.QueryType;

/**
 * 
 * @author mwesterg
 *
 */
public class MonitorMessage extends AssignmentMessage {

	public static final String TYPE = "MONITOR";
	private final QueryType type;
	private final String query;

	public MonitorMessage(Assignment assignment, QueryType type, String query) {
		super(assignment);
		this.type = type;
		this.query = query;
	}

	public MonitorMessage(String assignment, String type, String query) {
		super(assignment);
		this.type = QueryType.valueOf(type);
		this.query = query;
	}
	
	@Override
	protected String info() {
		return getType() + SEPARATOR + getQuery();
	}

	@Override
	protected String msgType() {
		return TYPE;
	}

	/**
	 * @return the type
	 */
	public QueryType getType() {
		return type;
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

}
