package nl.tue.declare.appl.worklist.gui;


import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nl.tue.declare.appl.util.*;
import nl.tue.declare.appl.util.swing.TSplitPane;
import nl.tue.declare.domain.instance.*;

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
public class FrmWarning extends OkCancelDialog {

	private static final long serialVersionUID = -5467726658399303432L;
	private JList constraints = new JList();
	private JPanel panel = new JPanel(new BorderLayout());

	public FrmWarning(Frame parent, Container aMonitorFrame) {
		super(parent, "",aMonitorFrame, true);
		setup();
	}

	public FrmWarning(Frame parent, String title, Container aMonitorFrame) {
		super(parent, title, aMonitorFrame, true);
		setup();
	}
	
	private void setup(){
		constraints.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {
				constraintSelected();
			}});
	}

	private void constraintSelected(){
		ConstraintWrapper selected = (ConstraintWrapper) constraints.getSelectedValue();
		if (selected != null) {
			panel.removeAll();
			panel.add(new ViolationPanel(selected.constraint));
		}			
		repaint();
	}

	public void fill(String title, Iterable<Constraint> constraints) {
		this.setTitle(title);
		ArrayList<ConstraintWrapper> temp = new ArrayList<ConstraintWrapper>();
		for (Constraint c: constraints){
			temp.add(new ConstraintWrapper(c));
		}
		super.fillList(temp, this.constraints);
		constraintSelected();
		//repaint();
		//this.panel.removeAll();
	
		/*panel.setLayout(new GridLayout(constraints.size(), 1));
		Iterator<Constraint> iterator = constraints.iterator();
		while (iterator.hasNext()) {
			Constraint constraint = iterator.next();
			JPanel p = new ViolationPanel(constraint);
			panel.add(p);
		}*/
	}

	@Override
	protected Component getContent() {
		JPanel content = new JPanel(new BorderLayout(2, 2));
		content.add(new TSplitPane(JSplitPane.HORIZONTAL_SPLIT, constraints,panel),
				BorderLayout.CENTER);
		JLabel temp = new JLabel("Do you want to proceed?");
		temp.setHorizontalAlignment(JLabel.CENTER);
		temp.setVerticalAlignment(JLabel.CENTER);
		content.add(temp, BorderLayout.SOUTH);
		return content;
	}
	
	@Override
	protected Dimension getExplicitSize(){
		return new Dimension(800,500);
	}	

	private class ConstraintWrapper{
		Constraint constraint;
		ConstraintWrapper(Constraint c){
		 super();
		 constraint = c; 
		}
		
		public String toString(){
			return constraint.getCaption();
		}
	}
}
