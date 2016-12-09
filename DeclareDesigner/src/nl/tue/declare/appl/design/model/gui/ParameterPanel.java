package nl.tue.declare.appl.design.model.gui;

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
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import nl.tue.declare.domain.model.ActivityDefinition;
import nl.tue.declare.domain.model.ConstraintDefinition;
import nl.tue.declare.domain.template.*;

public class ParameterPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3797188307495592042L;

	private JLabel name = new JLabel();
	private Color color;

	private JButton btn = new JButton("assign activities");

	private List<ActivityDefinition> real = new ArrayList<ActivityDefinition>();
	private Parameter parameter;

	private Listener listener = null;

	public ParameterPanel(Parameter param, Color line) {
		super(new BorderLayout());
		parameter = param;

		color = name.getForeground();

		btn.setText("assign activities to " + parameter.getName());

		name.setBorder(BorderFactory.createLineBorder(line));
		setLabel();

		add(name, BorderLayout.CENTER);
		add(btn, BorderLayout.EAST);

		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (listener != null) {
					Iterable<ActivityDefinition> selected = listener.select(
							parameter, real);
					if (selected.iterator().hasNext()) {
						real.clear();
						for (ActivityDefinition activity : selected) {
							real.add(activity);
						}
						setLabel();
						listener.assigned(ParameterPanel.this);
					}
				}
			}
		});
	}

	public void setListener(Listener l) {
		listener = l;
	}

	private void setLabel() {
		String label = "No activities are assigned to " + parameter.getName()
				+ (parameter.isBranchable() ? " (branchable)" : "");
		Color col = Color.RED;
		if (!real.isEmpty()) {
			label = "Activities assigned to " + parameter.getName()
					+ (parameter.isBranchable() ? " (branchable)" : "") + ": "
					+ getlabel();
			col = color;
		} else {

		}
		name.setText(label);
		name.setForeground(col);
	}

	private String getlabel() {
		String label = "";
		for (int i = 0; i < real.size(); i++) {
			if (!label.equals("")) {
				label += ", ";
			}
			label += real.get(i).toString();
		}
		if (label.equals("")) {
			return "[...]";
		} else {
			return "[" + label + "]";
		}
	}

	public List<ActivityDefinition> getReal() {
		return real;
	}

	public Parameter getFormal() {
		return parameter;
	}

	public void set(ConstraintDefinition constraintDefinition,
			Parameter parameter) {
		real.clear();
		for (ActivityDefinition branch : constraintDefinition
				.getBranches(parameter)) {
			real.add(branch);
		}
	}

	boolean ok() {
		return !real.isEmpty();
	}

	interface Listener {
		public Iterable<ActivityDefinition> select(Parameter parameter,
				Iterable<ActivityDefinition> real);

		public void assigned(ParameterPanel panel);
	}

}
