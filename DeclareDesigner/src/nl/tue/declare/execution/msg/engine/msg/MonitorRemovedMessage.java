package nl.tue.declare.execution.msg.engine.msg;

import nl.tue.declare.domain.instance.Assignment;
import nl.tue.declare.execution.msg.AssignmentMessage;

/**
 * 
 * @author mwesterg
 *
 */
public class MonitorRemovedMessage extends AssignmentMessage {
	public static final String TYPE = "MONITORREMOVED";

	private final int id;

	public MonitorRemovedMessage(final Assignment assignment, final int id) {
		super(assignment);
		this.id = id;
	}

	public MonitorRemovedMessage(final String ID, final String id) {
		super(ID);
		this.id = Integer.parseInt(id);
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * info
	 * 
	 * @return String
	 * @todo Implement this nl.tue.declare.execution.msg.AssignmentMessage method
	 */
	@Override
	protected String info() {
		// return recommendationResult.toString().replaceAll(this.newLine, "");
		return "" + getId();
	}

	/**
	 * msgType
	 * 
	 * @return String
	 * @todo Implement this nl.tue.declare.execution.msg.Message method
	 */
	@Override
	protected String msgType() {
		return TYPE;
	}
}
