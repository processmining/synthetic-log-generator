package nl.tue.declare.appl.design.model.gui;

import info.clearthought.layout.TableLayout;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.*;

import nl.tue.declare.appl.util.*;
import nl.tue.declare.domain.model.ActivityDefinition;
import nl.tue.declare.domain.model.AssignmentModel;
import nl.tue.declare.domain.template.*;


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
public class FrmAddConstraintDefinition extends OkCancelDialog implements
		TemplatePanel.Listener, ParameterPanel.Listener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4536444794452889015L;
	
	private FrmSelectFromList frmSelectFromList;

	private TemplatePanel templatePanel = null;
	private ParametersPanel parametersPanel = null;
	
	private JPanel content;
	
	
	
	
	private static double p = TableLayout.PREFERRED;
	private static double size[][] = { { TableLayout.FILL },
			{ TableLayout.FILL, p } };
	
	private AssignmentModel model;

	/**
	 * 
	 * @param owner
	 *            Frame
	 * @param title
	 *            String
	 * @param modal
	 *            boolean
	 * @param aMonitorFrame
	 *            JInternalFrame
	 * @param aModelCoordinator
	 *            WorkCoordinator
	 */
	public FrmAddConstraintDefinition(Frame owner, JComponent aMonitorFrame,
			AssignmentModel model) {
		super(owner, "Constraint definition", aMonitorFrame);
		this.model = model;
		templatePanel = new TemplatePanel(model.getLanguage());
		templatePanel.setListener(this);
		parametersPanel = new ParametersPanel();
		parametersPanel.addParameterListener(this);
		
		content = new JPanel(new TableLayout(size));
		content.add(templatePanel, "0, 0");
		content.add(parametersPanel, "0, 1");
		
		itemSelected(templatePanel.getSelectedTemplate());
		
		frmSelectFromList = new FrmSelectFromList(this, "Select activities", this);
	}

	protected Dimension getExplicitSize(){
		return new Dimension(800,500);
	}
	
	public TemplatePanel getTemplate() {
		return templatePanel;
	}
	
	public ParametersPanel getParameters(){
		return parametersPanel;
	}

	public void itemSelected(Object item) {
		if (item instanceof ConstraintTemplate){
			parametersPanel.setTemplate((ConstraintTemplate) item);			
		} else{
			parametersPanel.setTemplate(null);			
		}		
		setOk();
	}

	@Override
	protected Component getContent() {
		return content;
	}
	
	public void setOk(){		
		if (templatePanel.getSelectedTemplate() instanceof ConstraintTemplate){
			setOkEnabled(parametersPanel.ok());
		} else {
			setOkEnabled(false);
		}
	}

	public void assigned(ParameterPanel panel) {
		setOk();		
	}
	
	public Iterable<ActivityDefinition> select(Parameter parameter, Iterable<ActivityDefinition> real) {
		Collection<ActivityDefinition> selected = new ArrayList<ActivityDefinition>();
		// user can select from form
		frmSelectFromList.fillList(model.getActivityDefinitions()); // offer activities
		frmSelectFromList.setSelected(real);
		if (parameter.isBranchable()) { // user can select more activities only
										// for branchable parameters
			frmSelectFromList.setMultipleSelectionMode();
			frmSelectFromList.setTitle("Select one or more activities");
		} else {
			frmSelectFromList.setSingleSelectionMode();
			frmSelectFromList.setTitle("Select one activity");
		}
		
		if (frmSelectFromList.showCentered()) {
			Object[] sel = frmSelectFromList.getSelectedMultiple();
			for (int i = 0; i < sel.length; i++) {
				Object s = sel[i];
				if (s instanceof ActivityDefinition) {
					selected.add((ActivityDefinition) s);
				}
			}
		}
		return selected;
	}
}
