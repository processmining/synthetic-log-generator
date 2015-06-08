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
public class ConflictError extends VerificationError {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8005556128916356552L;

	public ConflictError(ViolationGroup group) {
		super(group);
	}

	public ConflictError() {
		this(null);
	}

	public String toString() {
		return "There is a conflict";
	}
}
