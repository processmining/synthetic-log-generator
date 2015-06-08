package nl.tue.declare.appl.design.model.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import nl.tue.declare.appl.design.model.IConditionSytaxCheckerListener;
import nl.tue.declare.appl.util.*;
import nl.tue.declare.appl.util.swing.*;
import nl.tue.declare.domain.model.ConstraintDefinition;
import nl.tue.declare.domain.model.ConstraintLevel;
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
public class FrmEditConstraintDefinition extends OkCancelDialog implements
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2419858471185671571L;

	private IConditionSytaxCheckerListener syntaxListener = null;

	// components
	private JTextField condition = new JTextField("");
	private JButton btnSyntax = new JButton("check syntax");
	private JTextField name = new JTextField("");

	private JRadioButton mandatory = new JRadioButton("mandatory");
	private JRadioButton optional = new JRadioButton("optional");

	private JComboBox groups = new JComboBox();
	private JTextArea decription = new TTextArea();
	private JTextArea message = new TTextArea();
	private JComboBox priority = new JComboBox();

	private JPanel optionalPanel;

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
	public FrmEditConstraintDefinition(Frame owner, JComponent aMonitorFrame) {
		super(owner, "Constraint definition", aMonitorFrame);

		name.setToolTipText("Enter constraint name.");
		name.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					FrmEditConstraintDefinition.this.positive.actionPerformed(null);
				}
			}

			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.optionalPanel = new JPanel(new GridLayout(2, 1));
		this.optionalPanel.add(this.groupsPanel());
		this.optionalPanel.add(this.priorityPanel());		
		optionalChanged();
	}
	
	protected Dimension getExplicitSize(){
		return new Dimension(500,500);
	}

	private JPanel typePanel() {
		JPanel main = new JPanel(new BorderLayout());
		main.add(this.radioPanel(), BorderLayout.NORTH);
		main.add(optionalPanel, BorderLayout.CENTER);
		return main;
	}

	private JPanel priorityPanel() {
		JPanel main = new JPanel(new BorderLayout(2, 2));

		JPanel priorityPanel = new JPanel(new BorderLayout());
		JPanel priorityCaption = new JPanel(new BorderLayout());
		priorityCaption.add(new JLabel("level        "), BorderLayout.NORTH);
		priorityPanel.add(priorityCaption, BorderLayout.WEST);
		priorityPanel.add(this.priority, BorderLayout.CENTER);

		JPanel messagePanel = new JPanel(new BorderLayout());
		JPanel messageCaption = new JPanel(new BorderLayout());
		messageCaption.add(new JLabel("message  "), BorderLayout.NORTH);
		messagePanel.add(messageCaption, BorderLayout.WEST);
		messagePanel.add(new JScrollPane(this.message), BorderLayout.CENTER);

		main.add(priorityPanel, BorderLayout.NORTH);
		main.add(messagePanel, BorderLayout.CENTER);
		return main;
	}

	private JPanel groupsPanel() {
		JPanel main = new JPanel(new BorderLayout(2, 2));

		this.groups.setForeground(this.getForeground());
		this.groups.setBackground(this.getBackground());
		this.groups.addActionListener(this);

		JPanel comboPanel = new JPanel(new BorderLayout());
		JPanel comboCaption = new JPanel(new BorderLayout());
		comboCaption.add(new JLabel("group       "), BorderLayout.NORTH);
		comboPanel.add(comboCaption, BorderLayout.WEST);
		comboPanel.add(this.groups, BorderLayout.CENTER);

		this.decription.setEditable(false);
		this.decription.setForeground(this.getForeground());
		this.decription.setBackground(this.getBackground());

		JPanel decriptionPanel = new JPanel(new BorderLayout());
		JPanel decriptionCaption = new JPanel(new BorderLayout());
		decriptionCaption.add(new JLabel("decription "), BorderLayout.NORTH);
		decriptionPanel.add(decriptionCaption, BorderLayout.WEST);
		decriptionPanel.add(new JScrollPane(this.decription),
				BorderLayout.CENTER);

		main.add(comboPanel, BorderLayout.NORTH);
		main.add(decriptionPanel, BorderLayout.CENTER);
		return main;
	}

	private JPanel radioPanel() {
		this.optional.addActionListener(this);
		this.mandatory.addActionListener(this);

		ButtonGroup radio = new ButtonGroup();
		radio.add(this.mandatory);
		radio.add(this.optional);
		radio.setSelected(this.mandatory.getModel(), true);

		JPanel main = new JPanel(new GridLayout(1, 2));

		main.add(this.mandatory);
		main.add(this.optional);

		return main;
	}

	private void optionalChanged() {
		this.optionalPanel.setVisible(optional.isSelected());
		pack();
	}

	/**
	 * 
	 * @param e
	 *            ActionEvent
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == optional || source == mandatory) {
			this.optionalChanged();
		}
		if (source == groups) {
			this.groupChanged();
		}
		if (source == btnSyntax) {
			this.checkSyntaxNotify();
		}
	}

	/**
	 * checkSyntax
	 */
	private void checkSyntaxNotify() {
		if (this.syntaxListener != null) {
			this.syntaxListener.checkSyntaxNotify(condition.getText());
		}
	}

	public void addSyntaxListener(IConditionSytaxCheckerListener l) {
		this.syntaxListener = l;
	}

	private void groupChanged() {
		ConstraintGroup group = this.getSelectedGroup();
		if (group != null) {
			this.decription.setText(group.getDescription());
		}
	}

	/**
	 * setMandatory
	 * 
	 * @param mandatory
	 *            boolean
	 */
	private void setMandatory(boolean mandatory) {
		this.mandatory.setSelected(mandatory);
		this.optional.setSelected(!mandatory);
	}

	public void addGroup(ConstraintGroup group) {
		this.groups.addItem(group);
	}

	private ConstraintGroup getSelectedGroup() {
		Object selected = groups.getSelectedItem();
		ConstraintGroup group = null;
		if (selected != null) {
			if (selected instanceof ConstraintGroup) {
				group = (ConstraintGroup) selected;
			}
		}
		return group;
	}

	private void setGroup(ConstraintGroup group) {
		if (group != null) {
			groups.setSelectedItem(group);
		}
	}

	public void addPriority(int p) {
		this.priority.addItem(new Integer(p));
	}

	private int getPriority() {
		int p = 0;
		Object selected = priority.getSelectedItem();
		if (selected != null) {
			if (selected instanceof Integer) {
				p = ((Integer) selected).intValue();
			}
		}
		return p;
	}

	private void setPriority(int p) {
		int i = 0;
		int count = priority.getItemCount();
		boolean found = false;
		Integer integer = null;
		while (i++ < count && !found) {
			Object item = priority.getItemAt(i);
			if (item != null) {
				if (item instanceof Integer) {
					integer = ((Integer) item);
					found = integer.intValue() == p;
				}
			}
		}
		if (found) {
			priority.setSelectedItem(integer);
		} else if (priority.getItemCount() > 0) {
			priority.setSelectedIndex(0);
		}
	}

	/**
	 * fillFormFromConstraintDefinition
	 * 
	 * @param constraint
	 *            ConstraintDefinition
	 * @param form
	 *            FrmConstraintDefinition
	 */
	public void toConstraint(ConstraintDefinition constraint) {
		if (constraint != null) {
			constraint.setDisplay(name.getText());
			System.out
					.println(name.getText() + " = " + constraint.getDisplay());
			constraint.getCondition().setText(condition.getText());
			constraint.setMandatory(mandatory.isSelected());
			if (!constraint.getMandatory()) {
				ConstraintLevel level = constraint.getLevel();
				if (level == null) {
					level = new ConstraintLevel(getSelectedGroup());
					constraint.setLevel(level);
				} else {
					level.setGroup(getSelectedGroup());
				}
				level.setLevel(getPriority());
				level.setMessage(message.getText());
			}
		}
	}

	/**
	 * fillFormFromConstraintDefinition
	 * 
	 * @param constraint
	 *            ConstraintDefinition
	 * @param form
	 *            FrmConstraintDefinition
	 */
	public void fromConstraint(ConstraintDefinition constraint) {
		if (constraint != null) {
			name.setText(constraint.getName());
			condition.setText(constraint.getCondition().getText());
			setMandatory(constraint.getMandatory());
			optionalChanged();
			if (!constraint.getMandatory()) {
				ConstraintLevel level = constraint.getLevel();
				if (level != null) {
					setGroup(level.getGroup());
					setPriority(level.getLevel());
					message.setText(level.getMessage());
				} else {
					message.setText("");
				}
			}
		} else {
		}

	}

	@Override
	protected Component getContent() {
		// *** prepare the top part of the form with input elements
		JPanel conditionPanel = new JPanel(new BorderLayout());
		conditionPanel.add(new JLabel("condition "), BorderLayout.WEST);
		conditionPanel.add(this.condition, BorderLayout.CENTER);
		conditionPanel.add(this.btnSyntax, BorderLayout.EAST);
		this.btnSyntax.addActionListener(this);
		JPanel namePanel = new JPanel(new BorderLayout());
		namePanel.add(new JLabel("name       "), BorderLayout.WEST);
		namePanel.add(this.name, BorderLayout.CENTER);

		JPanel up = new JPanel(new GridLayout(2, 1));
		up.add(namePanel);
		up.add(conditionPanel);

		JPanel data = new TPanel(new BorderLayout(2, 2));
		data.add(up, BorderLayout.NORTH);
		data.add(this.typePanel(), BorderLayout.CENTER);

		return data;
	}
}
