package nl.tue.declare.verification.impl;

import nl.tue.declare.verification.*;

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
public class HistoryError extends VerificationError {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8893211536014281088L;

	public HistoryError(ViolationGroup group) {
		super(group);
	}

	public HistoryError() {
		this(null);
	}

	public String toString() {
		return "Instance history is violated";
	}

}
