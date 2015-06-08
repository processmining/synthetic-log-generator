package nl.tue.declare.execution.msg.engine.msg;

import nl.tue.declare.domain.instance.Assignment;
import nl.tue.declare.execution.msg.AssignmentMessage;

import org.processmining.operationalsupport.messages.reply.ResponseSet;
import org.processmining.operationalsupport.xml.OSXMLConverter;

public class ResponseAssignmentMessage extends AssignmentMessage {

	private static OSXMLConverter converter = new OSXMLConverter();

	public static final String TYPE = "RESPONSE";

	private String recommendation = "";

	private final int id;

	public ResponseAssignmentMessage(final Assignment assignment, final int id,
			final ResponseSet<?> result) {
		super(assignment);
		this.id = id;
		recommendation = converter.toXML(result).replace('\n', ' ');
	}

	public ResponseAssignmentMessage(final String ID, final String id, final String recommendation) {
		super(ID);
		this.id = Integer.parseInt(id);
		if (recommendation != null) {
			this.recommendation = recommendation;
		}
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	public String getRecommendation() {
		return recommendation;
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
		return getId() + SEPARATOR + recommendation;
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
