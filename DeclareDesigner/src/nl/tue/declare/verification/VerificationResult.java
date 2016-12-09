package nl.tue.declare.verification;

import java.util.*;

import nl.tue.declare.domain.model.AssignmentModel;

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
public class VerificationResult extends ArrayList<VerificationError> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8987655868824572670L;
	private AssignmentModel model;

	public VerificationResult(AssignmentModel model) {
		super();
		this.model = model;
	}

	public void filter() {
		// sort on increasing group size
		for (int i = 0; i < size() - 1; i++) {
			VerificationError first = get(i);
			for (int j = i + 1; j < size(); j++) {
				VerificationError second = get(j);
				if (first.getGroup().size() > second.getGroup().size()) {
					set(i, second);
					set(j, first);
				}
			}
		}

		// find all supersets for each group
		VerificationResult remove = new VerificationResult(model);
		for (int i = 0; i < size() - 1; i++) {
			VerificationError first = get(i);
			for (int j = i + 1; j < size(); j++) {
				VerificationError second = get(j);
				if (second.isSuper(first)) {
					remove.add(second);
				}
			}
		}
		// remove all supersets
		removeAll(remove);
	}

	public boolean add(VerificationError o) {
		VerificationResult sub = getSubEq(o);
		if (sub.size() == 0) { // add obly if no subgroup exists
			VerificationResult sup = getSuper(o);
			this.removeAll(sup); // remove all supergroups
			return super.add(o);
		}
		return false;
	}

	private VerificationResult getSuper(VerificationError error) {
		VerificationResult result = new VerificationResult(model);
		for (int i = 0; i < size(); i++) {
			VerificationError curr = get(i);
			if (curr.isSuper(error)) {
				result.add(curr);
			}

		}
		return result;
	}

	private VerificationResult getSubEq(VerificationError error) {
		VerificationResult result = new VerificationResult(model);
		for (int i = 0; i < size(); i++) {
			VerificationError curr = get(i);
			if (curr.equals(error) || curr.isSub(error)) {
				result.add(curr);
			}
		}
		return result;
	}

	public String toString() {
		String str = "\"" + model.toString() + "\"";
		switch (size()) {
		case 0:
			str += " has no errors.";
			break;
		case 1:
			str += " has 1 error.";
			break;
		default:
			str += " has " + size() + " errors.";
			break;
		}
		return str;
	}

	public int hashCode() {
		return model.hashCode();
	}

	public AssignmentModel getModel() {
		return model;
	}
}
