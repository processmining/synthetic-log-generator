package nl.tue.declare.appl.verification;

import javax.swing.table.*;

import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.domain.model.*;
import nl.tue.declare.domain.template.Parameter;
import nl.tue.declare.verification.*;

class ViolationGroupTable extends TTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9101808402874185566L;

	/**
	 * 
	 * @param dm
	 *            ConstraintGroupTableModel
	 */
	public ViolationGroupTable() {
		super();
		setModel(new ViolationGroupTableModel());
	}

	/**
	 * 
	 * @return ConstraintDefinition
	 */
	public ConstraintDefinition getSelected() {
		ConstraintDefinition constraint = null;
		Object selected = super.getSelected();
		if (selected instanceof ConstraintDefinition) {
			constraint = (ConstraintDefinition) selected;
		}
		return constraint;
	}

	public ViolationGroupTableModel getModel() {
		ViolationGroupTableModel result = null;
		TableModel model = super.getModel();
		if (model instanceof ViolationGroupTableModel) {
			result = (ViolationGroupTableModel) model;
		}
		return result;
	}

	public void set(ViolationGroup group) {
		ViolationGroupTableModel model = getModel();
		if (model != null) {
			model.clear();
			if (group != null) {
				for (int i = 0; i < group.size(); i++) {
					model.addRow(group.get(i));
				}
			}
		}
	}

	public void clear() {
		getModel().clear();
	}

	public class ViolationGroupTableModel extends TTableModel {

		private static final long serialVersionUID = -1799236916611032192L;

		ViolationGroupTableModel() {
			super(2, new Object[] { "constraint", "activities", "condition" });
		}

		/**
		 * 
		 * @param constraint
		 *            ConstraintDefinition
		 */
		void addRow(ConstraintDefinition constraint) {
			addRow(new Object[] { constraint.getName(), parameters(constraint),
					constraint });
		}

		/**
		 * 
		 * @param constraint
		 *            ConstraintDefinition
		 * @return String
		 */
		private String parameters(ConstraintDefinition constraint) {
			String result = "";
			for (Parameter parameter : constraint.getParameters()) {
				if (!result.equals("")) {
					result += ", ";
				}
				result += branches(constraint, parameter);

			}
			return result;
		}

		/**
		 * 
		 * @param parameter
		 *            Parameter
		 * @return String
		 */
		private String branches(ConstraintDefinition constraintDefinition,
				Parameter parameter) {
			String result = "";
			for (ActivityDefinition branch : constraintDefinition
					.getBranches(parameter)) {
				if (!result.equals("")) {
					result += ", ";
				}
				result += branch.getName();
			}
			return "[" + result + "]";
		}
	}
}
