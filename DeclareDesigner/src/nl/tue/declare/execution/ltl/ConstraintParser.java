package nl.tue.declare.execution.ltl;

import java.util.*;

import nl.tue.declare.domain.instance.Event;
import nl.tue.declare.domain.model.*;
import nl.tue.declare.domain.template.Parameter;

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
 * @author not attributable
 * @version 1.0
 */
public class ConstraintParser extends SimpleParser {

	public ConstraintParser() {
		super();
	}

	
	public synchronized String createFormula(
			Iterable<ConstraintDefinition> constraints) {
		String result = null;
		for (ConstraintDefinition constraint : constraints) {
			try {
				String formula = parse(constraint);

				if (result == null) {
					result = formula;
				} else {
					result = result + " /\\ "+ formula; 
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (result == null) {
			result = "true";
		}
		return result;
	}

	public synchronized String parse(ConstraintDefinition constraint)
			throws Exception {
		String ltlText = constraint.getText();
		try {
			for (Parameter parameter : constraint.getParameters()) {
				ltlText = replaceParameterWithActivities(ltlText, parameter, constraint
						.getBranches(parameter));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
		return ltlText;
	}

	private String replaceParameterWithActivities(String formula,
			Parameter parameter, Collection<ActivityDefinition> realBranches) {
		String msg = new String(formula);
		msg = replaceParameterDefaultWithActivities(msg, parameter, realBranches);
		for (Event.Type event : Event.Type.values()) {
			msg = this.replaceParameterEventWithActivities(msg, parameter,
					event, realBranches);
		}
		return msg;
	}

	private String replaceParameterEventWithActivities(String formula,
			Parameter parameter, Event.Type event,
			Collection<ActivityDefinition> realBranches) {
		String msg = new String(formula);
		String real = "";
		for (ActivityDefinition branch : realBranches) {
			if (!real.equals("")) {
				real += " || ";
			}
			Event e = new Event(null, branch, event);
			real += "\"" + e.getProposition() + "\"";
		}
		String temp = "\"" + parameter.getName() + "." + event.name()+ "\"";
		msg = msg.replaceAll(temp, real);
		temp = "\"" + parameter.getName() + "." + event.name().toLowerCase()+ "\"";
		msg = msg.replaceAll(temp, real);
		return msg;
	}

}
