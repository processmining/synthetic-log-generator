package nl.tue.declare.execution.msg.worklist;

import nl.tue.declare.domain.instance.Assignment;
import nl.tue.declare.execution.msg.AssignmentMessage;

/**
 * 
 * @author mwesterg
 * 
 */
public class RemoveMonitorMessage extends AssignmentMessage {

	public static final String TYPE = "REMOVEMONITOR";
	private final int id;

	public RemoveMonitorMessage(final Assignment assignment, final int id) {
		super(assignment);
		this.id = id;
	}

	public RemoveMonitorMessage(final String assignment, final String id) {
		super(assignment);
		this.id = Integer.valueOf(id);
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	@Override
	protected String info() {
		return "" + getId();
	}

	@Override
	protected String msgType() {
		return TYPE;
	}

}
