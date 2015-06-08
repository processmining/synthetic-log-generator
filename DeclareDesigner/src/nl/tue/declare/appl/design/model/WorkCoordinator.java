package nl.tue.declare.appl.design.model;

/**
 * <p>Title: DECLARE</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: TU/e</p>
 *
 * @author Maja Pesic
 * @version 1.0
 */

import java.util.*;

import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

import org.jgraph.graph.*;

import nl.tue.declare.appl.design.model.gui.*;
import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.control.*;
import nl.tue.declare.domain.model.*;
import nl.tue.declare.domain.template.*;
import nl.tue.declare.graph.*;
import nl.tue.declare.graph.model.*;

public class WorkCoordinator extends ModelInternalCoordinator implements
		GraphListener, ActionListener, IActivityDataPanelListener,
		IConditionSytaxCheckerListener {
	private AssignmentModel model;
	private AssignmentModelView modelView;
	private AssignmentPanel panel;

	private FrmActivityDefiniton frmActivityDefinition;
	private FrmAddConstraintDefinition frmAddConstraintDefinition;
	private FrmEditConstraintDefinition frmEditConstraintDefinition;
	private FrmActivityDataDefinition frmActivityDataDefinition;

	public WorkCoordinator(JFrame aMainFrame, AssignmentPanel panel,
			AssignmentModel aModel) {
		super(panel, aModel);
		this.model = aModel;
		modelView = new AssignmentModelView(model);
		this.panel = panel;
		this.panel.preview(new GraphPane(this, modelView.getGraph(), model
				.getLanguage().getName()));

		frmActivityDefinition = new FrmActivityDefiniton(aMainFrame, panel);
		frmActivityDefinition.getDataPanel().addListener(this);

		frmAddConstraintDefinition = new FrmAddConstraintDefinition(aMainFrame,
				panel, model);
		frmEditConstraintDefinition = new FrmEditConstraintDefinition(
				aMainFrame, panel);

		frmActivityDataDefinition = new FrmActivityDataDefinition(aMainFrame,
				this.frmActivityDefinition);

		frmEditConstraintDefinition.addSyntaxListener(this);
		this.fillConstraintGroups();
	}

	public DGraph getGraph() {
		return this.getView().getGraph();
	}

	public AssignmentModelView getView() {
		return this.modelView;
	}

	/**
	 * start
	 */
	public void start() {

	}

	/**
	 * end
	 */
	public void end() {
	}

	private void fillConstraintGroups() {
		int count = Control.singleton().getConstraintTemplate()
				.constraintGroupCount();
		for (int i = 0; i < count; i++) {
			frmEditConstraintDefinition.addGroup(Control.singleton()
					.getConstraintTemplate().getGroupAt(i));
		}

		Integer[] possible = ConstraintWarningLevel.possible();
		for (int i = 0; i < possible.length; i++) {
			frmEditConstraintDefinition.addPriority(possible[i]);
		}
	}

	public void insertVertex(Point2D point) {
		this.addActivityDefinition(point);
	}

	/**
	 * addactivityDefinition
	 * 
	 * @param point
	 *            Point2D
	 */
	public void addActivityDefinition(Point2D point) {
		getControl().getAssignmentModel().addActivityDefinition(this.model);
	}

	/**
	 * 
	 * @param list
	 *            Port
	 */
	private void removeActivityDefinitions(List<ActivityDefinition> list) {
		if (list == null) {
			return;
		}
		Iterator<ActivityDefinition> iterator = list.iterator();
		while (iterator.hasNext()) {
			ActivityDefinition job = iterator.next();
			boolean response = MessagePane.ask(panel,
					"Do you want to delete the activity definition \'" + job
							+ "\'?");
			if (response) {
				model.deleteActivityDefinition(job);
			}
		}
	}

	/**
	 * 
	 * @param list
	 *            List
	 */
	private void removeConstraintDefinitions(List<ConstraintDefinition> list) {
		if (list == null) {
			return;
		}
		Iterator<ConstraintDefinition> iterator = list.iterator();
		while (iterator.hasNext()) {
			ConstraintDefinition constraint = iterator.next();
			boolean response = MessagePane.ask(panel,
					"Do you want to delete the constraint \'"
							+ constraint.getName() + "\'?");
			if (response) {
				model.deleteConstraintDefinition(constraint);
			}
		}
	}

	/**
	 * 
	 * @param cells
	 *            Object[]
	 */
	public void remove(Object[] cells) {
		List<ActivityDefinition> jobs = new ArrayList<ActivityDefinition>();
		List<ConstraintDefinition> constraints = new ArrayList<ConstraintDefinition>();
		for (int i = 0; i < cells.length; i++) {
			Object cell = cells[i];
			if (cell instanceof DVertex) {
				DVertex vertex = (DVertex) cell;
				Object vertexObject = vertex.getUserObject();
				if (vertexObject instanceof ActivityDefinition) {
					ActivityDefinition job = (ActivityDefinition) vertexObject;
					if (!jobs.contains(job)) {
						jobs.add(job);
					}
				}
			} else if (cell instanceof DEdge) {
				DEdge edge = (DEdge) cell;
				Object edgeObject = edge.getUserObject();
				if (edgeObject instanceof ConstraintDefinition) {
					ConstraintDefinition constraint = (ConstraintDefinition) edgeObject;
					if (!constraints.contains(constraint)) {
						constraints.add(constraint);
					}
				}
			}
		}
		removeActivityDefinitions(jobs);
		removeConstraintDefinitions(constraints);
	}

	/**
	 * editActivityDefinition
	 * 
	 * @param activityDefinition
	 *            ActivityDefinition
	 */
	public void editActivityDefinition(ActivityDefinition activityDefinition) {
		if (activityDefinition != null) {
			frmActivityDefinition.fromActivity(activityDefinition);
			frmActivityDefinition.setTitle("Edit activity definition");
			if (frmActivityDefinition.showCentered()) {
				this.editActivityDefinitionConfirmed(activityDefinition);
			}
		} else {
			JOptionPane
					.showInternalMessageDialog(
							panel,
							"No activity definition is selected. You must first select activity definition you want to edit.",
							"information", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * editActivityDefinitionConfirmed
	 * 
	 * @param activityDefinition
	 *            ActivityDefinition
	 */
	public void editActivityDefinitionConfirmed(
			ActivityDefinition activityDefinition) {
		ActivityDefinition copy = (ActivityDefinition) activityDefinition
				.clone();
		frmActivityDefinition.toActivity(copy);
		if (this.getControl().getAssignmentModel().editActivityDefinition(copy)) {
			frmActivityDefinition.toActivity(activityDefinition);
		} else {
			JOptionPane
					.showInternalMessageDialog(
							panel,
							"This name is already assigned to another activity definition! ",
							"error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * getVertexObject
	 * 
	 * @param port
	 *            Port
	 * @return ActivityDefinition
	 */
	public ActivityDefinition getVertexObject(Port port) {
		ActivityDefinition result = null;
		modelView.getModel();
		Object vertexObject = modelView.getModel().getParent(port);
		if (vertexObject != null) {
			if (vertexObject instanceof DVertex) {
				DVertex vertex = (DVertex) vertexObject;
				Object object = vertex.getUserObject();
				if (object != null) {
					if (object instanceof ActivityDefinition) {
						result = (ActivityDefinition) object;
					}
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @param cell
	 *            Object
	 */
	public void edit(Object cell) {
		if (cell instanceof DVertex) {
			DVertex vertex = (DVertex) cell;
			Object vertexObject = vertex.getUserObject();
			if (vertexObject instanceof ActivityDefinition) {
				ActivityDefinition job = (ActivityDefinition) vertexObject;
				this.editActivityDefinition(job);
			}
		} else if (cell instanceof DEdge) {
			DEdge edge = (DEdge) cell;
			Object edgeObject = edge.getUserObject();
			if (edgeObject instanceof ConstraintDefinition) {
				ConstraintDefinition constraintDefinition = (ConstraintDefinition) edgeObject;
				this.editConstraintDefinition(constraintDefinition);
			}
		}
	}

	/**
	 * editConstraintDefinition
	 * 
	 * @param constraintDefinition
	 *            ConstraintDefinition
	 */
	public void editConstraintDefinition(
			ConstraintDefinition constraintDefinition) {
		if (constraintDefinition != null) {
			/*
			 * this.fillTemplates();
			 * frmConstraintDefinition.getTemplate().selectTemplate(
			 * constraintDefinition);
			 */
			// frmConstraintDefinition.getTemplate().setEnabledTemplate(false);
			// fillFormFromConstraintDefinition(constraintDefinition,
			// frmConstraintDefinition);
			frmEditConstraintDefinition.fromConstraint(constraintDefinition);
			frmEditConstraintDefinition.setTitle("Edit constraint definition");
			if (frmEditConstraintDefinition.showCentered()) {
				editConstraintDefinitionConfirmed(constraintDefinition);
			}
		} else {
			JOptionPane
					.showInternalMessageDialog(
							panel,
							"No constraint definition is selected. You must first select a constraint definition you want to edit.",
							"information", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * editConstraintDefinition
	 * 
	 * @param constraintDefinition
	 *            ConstraintDefinition
	 */
	public void editConstraintDefinitionConfirmed(
			ConstraintDefinition constraintDefinition) {
		// this.fillConstraintDefinitionFromForm(constraintDefinition,
		// frmConstraintDefinition);
		frmEditConstraintDefinition.toConstraint(constraintDefinition);
		this.model.editConstraintDefinition(constraintDefinition);
	}

	public void fillParametersFromConstraintDefintion(
			ConstraintDefinition constraint, ParametersPanel panel) {
		ConstraintTemplate template = constraint;
		panel.setTemplate(template);
		for (Parameter parameter : template.getParameters()) {
			panel.get(parameter).set(constraint, parameter);
		}
	}

	/**
	 * 
	 * @param constraint
	 *            ConstraintDefinition
	 * @param panel
	 *            ParametersPanel
	 */
	public void fillConstraintDefintionFromParameters(
			ConstraintDefinition constraint, ParametersPanel panel) {
		ConstraintTemplate template = constraint;
		constraint.cleanAllParameterBranches();
		for (Parameter parameter : template.getParameters()) {
			List<ActivityDefinition> real = panel.get(parameter).getReal();
			for (ActivityDefinition branch : real) {
				constraint.addBranch(parameter, branch);
			}
		}
	}

	/**
	 * setView
	 * 
	 * @param aView
	 *            AssignmentModelView
	 */
	public void setView(AssignmentModelView aView) {
		this.modelView = aView;
		this.modelView.updateUI();
	}

	private void fillFormFromActivityDataDefinition(
			FrmActivityDataDefinition form, ActivityDefinition activity,
			ActivityDataDefinition data) {
		if (form != null) {
			if (activity != null) {
				form.fillData(activity.availableDataElements());
				form.fillType(ActivityDataDefinition.Type.values());
				if (data != null) {
					form.setType(data.getType());
					form.setData(data.getDataElement());
				}
			}
		}
	}

	private void fillActivityDataDefinitionFromForm(
			FrmActivityDataDefinition form, ActivityDataDefinition data) {
		if (form != null && data != null) {
			Object dataObject = form.getSelectedData();
			Object typeObject = form.getSelectedType();
			if (dataObject != null && typeObject != null) {
				if (dataObject instanceof DataElement
						&& typeObject instanceof ActivityDataDefinition.Type) {
					DataElement dataElement = (DataElement) dataObject;
					ActivityDataDefinition.Type type = (ActivityDataDefinition.Type) typeObject;

					data.setDataElement(dataElement);
					data.setType(type);
				}
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
	}

	public ActivityDataDefinition add(ActivityDefinition activity) {
		ActivityDataDefinition data = null; // new activity data definition
		if (activity != null) {
			this.fillFormFromActivityDataDefinition(
					this.frmActivityDataDefinition, activity, data); // fill
			// form
			this.frmActivityDataDefinition.setTitle("new activity data");
			this.frmActivityDataDefinition.setDataElementEnabled(true); // allow
			// to
			// seelct
			// data
			// element
			if (this.frmActivityDataDefinition.showCentered()) { // if modal
				// form
				// closed with
				// OK button
				Object dataObject = frmActivityDataDefinition.getSelectedData(); // get
				// selected
				// data
				// element
				if (dataObject != null) {
					if (dataObject instanceof DataElement) {
						data = activity
								.addDataElement((DataElement) dataObject); // create
						// new
						// activity
						// data
						// definiton
						this.fillActivityDataDefinitionFromForm(
								this.frmActivityDataDefinition, data); // fill
						// it
						// from
						// form
					}
				}
			}
		}
		return data; // return new activity data definiton
	}

	public boolean delete(ActivityDataDefinition data,
			ActivityDefinition activity) {
		boolean response = MessagePane.ask(panel,
				"Do you want to delete the activity data \'" + data + "\'?");
		if (response) {
			activity.remove(data);
		}
		return response;
	}

	public boolean edit(ActivityDataDefinition data, ActivityDefinition activity) {
		boolean ok = false;
		if (activity != null) {
			this.fillFormFromActivityDataDefinition(
					this.frmActivityDataDefinition, activity, data); // fill
			// form
			this.frmActivityDataDefinition.setTitle("edit "
					+ data.getDataElement().getName());
			this.frmActivityDataDefinition.setDataElementEnabled(false); // do
			// not
			// allow
			// to
			// chenge
			// data
			// element
			if (ok = this.frmActivityDataDefinition.showCentered()) { // if
				// modal
				// form
				// closed
				// with OK
				// button
				this.fillActivityDataDefinitionFromForm(
						this.frmActivityDataDefinition, data); // fill it from
				// form
			}
		}
		return ok;
	}

	public boolean checkSyntax(String expression) {
		return ConditionSyntaxChecker.check(expression, this.model,
				this.frmEditConstraintDefinition);
	}

	public void checkSyntaxNotify(String expression) {
		ConditionSyntaxChecker.checkNotify(expression, this.model,
				this.frmEditConstraintDefinition);
	}

	public void addConstraint() {
		frmAddConstraintDefinition.setTitle("Add constraint definition");
		if (frmAddConstraintDefinition.showCentered()) {
			addConstraintDefinitionConfirmed();
		}
	}

	public void addConstraintDefinitionConfirmed() {
		Object selected = frmAddConstraintDefinition.getTemplate()
				.getSelectedTemplate();
		if (selected instanceof ConstraintTemplate) {
			ConstraintTemplate template = (ConstraintTemplate) selected;
			ConstraintDefinition constraint = model
					.createConstraintDefinition(template);
			fillConstraintDefintionFromParameters(constraint,
					frmAddConstraintDefinition.getParameters());
			model.addConstraintDefiniton(constraint);
		}
	}
}
