package nl.tue.declare.appl.design.organization.gui;

import java.awt.*;
import javax.swing.*;

import nl.tue.declare.appl.util.*;
import nl.tue.declare.domain.organization.Role;

public class FrmRole extends OkCancelDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8155696265398079138L;

	private JTextField name = new JTextField("");

	public FrmRole(Frame owner, String title, JInternalFrame aMonitorFrame) {
		super(owner, title, aMonitorFrame);
	}

	public void toRole(Role role) {
		if (role != null) {
			role.setName(name.getText());
		}
	}

	public void fromRole(Role role) {
		if (role != null) {
			name.setText(role.getName());
		} else {
			name.setText("");
		}
	}

	@Override
	protected Component getContent() {
		JPanel topPanel = new JPanel(new GridLayout(1, 2, 2, 2));
		topPanel.add(new JLabel("name"));
		topPanel.add(name);
		return topPanel;
	}

}
